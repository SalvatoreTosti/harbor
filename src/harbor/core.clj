(ns harbor.core
  (:gen-class)
  (:require [harbor.secureRandom :as sec-rand]
            [me.raynes.fs :as fs]))

(defn read-datab [location]
  {:pre [(fs/exists? location)]}
  (slurp location))

(defn map-from-datab [location]
  {:pre [(fs/exists? location)]}
  (->> (read-datab location)
       (clojure.string/split-lines)
       (map #(clojure.string/split % #"\t"))
       (into {})))

(defn generate-number-string
  "Creates a string of random numbers from 1 to hi, of length 'length'"
  [length hi]
  {:pre [(number? length)
         (pos? length)
         (number? hi)
         (pos? hi)
         (< hi 10)]}
  (clojure.string/join(take length (repeatedly #(sec-rand/secure-generate-number hi)))))

(defn get-single-word
  "Returns corresponding value associated with 'key-string' from map-datab"
  [map-datab key-string]
  {:pre [(not-empty map-datab)
         (not-empty key-string)]}

  (map-datab key-string))

(defn construct-password
  "Constructs a random password consisting of 'pass-length' number of words from the map associated with data found at 'location'.
  Note, 'key-length' and 'key-hi' refer to how many numbers / maximum numbers are associated with the keys found in the data at 'location'."
  [pass-length key-length key-hi location]
  {:pre [(number? pass-length)
         (pos? pass-length)
         (number? key-length)
         (pos? key-length)
         (= key-length 5) ;current implementation requires that key length be 5
         (number? key-hi)
         (pos? key-hi)
         (< key-hi 10)
         (fs/exists? location)]}

  (let [datab (map-from-datab location)]
  (take pass-length (repeatedly #(get-single-word datab (generate-number-string key-length key-hi))))))

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

(defn default-database-location []
  (str (System/getProperty "user.home") "/bin/harbor/wordDatabase.txt"))

(defn valid-arguments?
  "Returns true if given argument is valid, nil otherwise."
  [num-words]
  {:pre [(string? num-words)
         (and (number? (read-string num-words))
         (pos? (read-string num-words)))]}
  true)

(defn -main
  "returns a password of length num-words."
  [num-words]
  (try
    (let [preped-argument (if (or (nil? num-words) (empty? num-words)) "3" num-words)]
      (valid-arguments? preped-argument)
      (when (fs/exists? (default-database-location))
        (->> (construct-password (read-string preped-argument) 5 6 (default-database-location))
             (display-to-console))
      ))
  (catch Exception e (str "Invalid argument, argument must be a positive integer."))))

(-main "3")
