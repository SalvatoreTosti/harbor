(ns harbor.core
  (:gen-class)
  (:require
   [clj-http.client :as client]
   [harbor.secureRandom :as sec-rand]
   [harbor.output :as output]
   [clj-kit.string-kit :as string-kit]
   [clojure.tools.cli :as cli]
   [clojure.java.io :as io]
   [clojure.string :as string]))

(defn capitalize-letters
  [st ct]
  (cond
   (not (pos? ct)) st
   (string-kit/is-upper? st) st
   (not (string-kit/contains-alpha? st)) st

   :else (let [split (split-nth st (rand-int (count st)))]
           (if (and
                (string-kit/is-alpha? (str (:target split)))
                (string-kit/is-lower? (str (:target split))))
             (capitalize-letters
              (apply str
                     (concat
                      (:head split)
                      (string/capitalize (:target split))
                      (:tail split)))
              (dec ct))

             (capitalize-letters st ct)))))

;(clint/get GET /repos/:owner/:repo/releases/latest
;           "http://example.com/resources/3")

;(println (client/get "https://api.github.com/repos/SalvatoreTosti/harbor/releases/tags/v0.1"))

(defn map-from-datab []
  "Reads word database into a map."
  (->> (slurp (io/resource "wordDatabase.txt"))
       (clojure.string/split-lines)
       (map #(clojure.string/split % #"\t"))
       (into {})))

(defn generate-number-string
  "Creates a string of random numbers from 1 to hi, of length 'length'"
  [length hi]
  {:pre [(number? length)
         (pos? length)
         (integer? length)
         (number? hi)
         (pos? hi)
         (integer? hi)
         (< hi 10)]}
  (clojure.string/join (take length (repeatedly #(sec-rand/secure-generate-number hi)))))

(defn get-single-word
  "Returns corresponding value associated with 'key-string' from map-datab"
  [map-datab key-string]
  {:pre [(not-empty map-datab)
         (not-empty key-string)]}
  (map-datab key-string))

(defn construct-password
  "Constructs a random password consisting of 'pass-length' number of words from the map associated with data found at 'location'.
  Note, 'key-length' and 'key-hi' refer to how many numbers / maximum numbers are associated with the keys found in the data at 'location'."
  ([pass-length]
   (construct-password pass-length 5 6))

  ([pass-length key-length key-hi]
  {:pre [(number? pass-length)
         (pos? pass-length)
         (number? key-length)
         (pos? key-length)
         (= key-length 5) ;current implementation requires that key length be 5
         (number? key-hi)
         (pos? key-hi)
         (< key-hi 10)]}

  (let [datab (map-from-datab)]
  (take pass-length (repeatedly #(get-single-word datab (generate-number-string key-length key-hi)))))))

(defn nickname-replace
  "Replaces difficult to pronounce characters with a corresponding word, for easy reading."
  [password]
  {:pre [(instance? String password)]}
  (let [replace-map {"!" "bang"
                     "\"" "quote"
                     "#" "hash"
                     "$" "bucks"
                     "%" "ears"
                     "&" "and"
                     "`" "ding"
                     "~" "twiddle"
                     "@" "at"
                     "^" "hat"
                     "*" "star"
                     "(" "frown"
                     ")" "smile"
                     "_" "under"
                     "-" "dash"
                     "+" "plus"
                     "=" "equals"
                     "{" "sneer"
                     "}" "smirk"
                     "[" "uh"
                     "]" "duh"
                     "|" "pole"
                     ":" "eyes"
                     ;"\\" "back"
                     }]
  (clojure.string/replace
    password #"!|\"|\#|\$|%|&|`|~|@|\^|\*|\(|\)|\_|\-|\+|=|\{|\}|\[|\]|\||:|\\." replace-map)))

(defn special-character
  "Returns a single random character from a list of common 'special' characters."
  []
  (->
  '("!","\"","#","$","%","&","`","~","@","^","*","(",")","_","-","+","=","{","}","[","]","|",":") ;"\\" "back"
   (rand-nth)))

(defn insert-special
  "Inserts a random 'special' character into a given collection."
  [coll]
  (let [split-list (-> (rand-int (inc (count coll)))
                     (split-at coll))]
    (concat (first split-list) (conj [] (special-character)) (second split-list))))

 (defn insert-special-rec
  "Inserts multiple random 'special' characters into a given collection."
   [coll, remaining-specials]
   (if (not (pos? remaining-specials))
     coll
     (insert-special-rec (insert-special coll) (dec remaining-specials))))

(defn generate-passphrase
  [num-words, num-specials, num-capitals]
  (-> (construct-password num-words)
      (insert-special-rec num-specials)
      (#(clojure.string/join " " %))
      (capitalize-letters num-capitals)
      (output/console-out)
      ))

(defn remove-nth
  [vect index]
  (cond
   (nil? vect) nil
   (empty? vect) nil
   (< index 0)
   (remove-nth vect 0)
   (>= index (count vect) )
   (remove-nth vect (dec (count vect)))
  :else
   (let [target (nth vect index)
        head (take index vect)
        tail (drop (inc index) vect)]
    {:target target :new-coll (concat head tail)})))

(defn remove-random
  [vect]
  (remove-nth vect (rand-int (count vect))))

(defn select-random-rec
  [coll selected number]
  (if (or (not (pos? number))
          (empty? coll))
    selected
  (let [removal (remove-random coll)]
    (select-random-rec (:new-coll removal) (conj selected (:target removal)) (dec number)))))

(defn select-random
  [coll number]
  (select-random-rec coll '() number))

(defn split-nth
  [vect index]
  (cond
   (nil? vect) nil
   (empty? vect) nil
   (< index 0)
   (split-nth vect 0)
   (>= index (count vect) )
   (split-nth vect (dec (count vect)))
  :else
   (let [target (nth vect index)
        head (take index vect)
        tail (drop (inc index) vect)]
    {:target target :head head :tail tail})))

(defn valid-arg-count?
  [args]
  (<= 0 (count args)))

(defn valid-arg-integer?
  [arg]
  (try
    (if (< 0 (Integer/parseInt arg)) true false)
    (catch Exception e false)))

(defn validate-arguments
  [args]
  (cond
   (or (nil? args) (zero? (count args)))
   {:message "" :action "continue"}

   (not (valid-arg-count? args))
   {:message "Invalid number of arguments, the harbor executable only takes 1 argument." :action "exit"}

   (not (valid-arg-integer? (first args)))
   {:message "Invalid argument supplied, argument must be a positive integer." :action "exit"}

   :else {:message "" :action "continue" :arguments (Integer/parseInt (first args))}))

(defn error-check
  [status-map]
   (if (= (:action status-map) "exit")
     (println (:message status-map))
    status-map))

(defn engine
  [options, arguments]
  (->>
   #(generate-passphrase
     (or (:length options) arguments 5)
     (or (:special options) 0)
     (or (:capital options) 0))
   (repeatedly (or (:repeat options) 1))
   (doall)))

(def cli-options
  [["-r" "--repeat COUNT" "Repeats random generation"
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be a number greater than 0"]]
   ["-l" "--length LENGTH" "Length of passphrase sequence"
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be a number greater than 0"]]
   ["-w" "--width WIDTH" "Minimum length for individual words in a sequence"
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 6) "Must be a number greater than 0 and less than 6"]]
   ["-s" "--special COUNT" "Inserts a given number of special characters into a sequence"
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %)]]
   ["-c" "--capital COUNT" "Capitalizes a given number of characters in the generated sequence"
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %)]]
   ["-v" "--version" "Version information"]
   ["-h" "--help" "Help information"]])

(defn -main [& args]
  "returns a passphrase of a given length."
  (let [{:keys [options arguments summary errors]} (cli/parse-opts args cli-options)
        status-map (->
                    (cond
                     (some? errors) {:message (string/join "\n" errors)
                                     :action "exit"}
                     (:help options) {:message (string/join "\n"
                                                            ["harbor is a commandline password generation tool.", summary])
                                      :action "exit"}
                     (:version options) {:message "harbor v0.2.1"
                                         :action "exit"}
                     :else (validate-arguments arguments))
      (error-check))]
    (when status-map (engine options (:arguments status-map)))))
