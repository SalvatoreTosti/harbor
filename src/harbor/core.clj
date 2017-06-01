(ns harbor.core
  (:gen-class)
  (:require [harbor.secureRandom :as sec-rand]
            [clojure.tools.cli :as cli]
            [clojure.java.io :as io]))

 (defn read-datab []
     (slurp (io/resource "wordDatabase.txt")))

(defn map-from-datab []
  (->> (read-datab)
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
  (take pass-length (repeatedly #(get-single-word datab (generate-number-string key-length key-hi))))))
  )

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

(defn display-to-console [word-list]
  (println (clojure.string/join " " word-list)))

(defn valid-arguments?
  "Returns true if given argument can be cast to a positive integer, false otherwise."
  [num-words]
  (try
    (and
     (string? num-words)
     (number? (read-string num-words))
     (integer? (read-string num-words))
     (pos? (read-string num-words)))
  (catch Exception e false)))

;; (defn generate-passphrase
;;   [num-words]
;;    (let [default-size "5"
;;           defaulted-argument (if (or (nil? num-words) (empty? num-words)) default-size num-words)]
;;       (cond
;;        (not (valid-arguments? defaulted-argument)) (println "Invalid argument, argument must be a positive integer.")
;;        :else  (->> (construct-password (read-string defaulted-argument))
;;               (display-to-console)))))

(defn generate-passphrase
  [num-words]
  (->> (construct-password num-words)
       (display-to-console)))

(def cli-options
  [["-r" "--repeat COUNT" "Repeats random generation"
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be a number greater than 0"]]
   ["-l" "--length LENGTH" "Length of passphrase sequence"
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be a number greater than 0"]]
   ["-v" "--version" "Version information"]
   ["-h" "--help"]])

(defn -main [& args]
  "returns a passphrase of a given length."
  (let [{:keys [options arguments summary errors]} (cli/parse-opts args cli-options)]
     (cond
      (some? errors) (apply println errors)
      (:help options) (println
                       "harbor is a commandline password generation tool.\n"
                       summary)
      (:version options) (println
                          "harbor v0.2.0")
      :else
      (->>
       #(generate-passphrase (or (:length options) 5))
       (repeatedly (or (:repeat options) 1))
       (doall)))))
