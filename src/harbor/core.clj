(ns harbor.core
  (:gen-class)
  (:require [harbor.secureRandom :as sec-rand]))

(defn read-datab [location]
  (slurp location))

(defn map-from-datab [location]
  (->> (read-datab location)
       (clojure.string/split-lines)
       ;(map clojure.string/triml)
       (map #(clojure.string/split % #"\t"))
       (into {})))

#_(defn generate-number [hi]
  "Returns a string representation of a number.
  Selects numbers between 1 and hi, inclusive.
  NOTE: Is not crypto secure generation of numbers,
  use secureRandom.clj 'secure-generate-random' instead"
  (->> (rand-int hi)
       (+ 1)
       (str)))

(defn generate-number-seq [length hi]
  "Creates a string of random numbers from 1 to hi, of length 'length'"
  (clojure.string/join(take length (repeatedly #(sec-rand/secure-generate-number hi)))))

(defn get-single-word [map-datab key-string]
  "Returns corresponding value associated with 'key-string' from map-datab"
  (map-datab key-string))

(defn construct-password [pass-length key-length key-hi location]
  "Constructs a random password consisting of 'pass-length' number of words from the map associated with data found at 'location'.
  Note, 'key-length' and 'key-hi' refer to how many numbers / maximum numbers are associated with the keys found in the data at 'location'."
  (let [datab (map-from-datab location)]
  (take pass-length (repeatedly #(get-single-word datab (generate-number-seq key-length key-hi))))))

;(construct-password 4 5 6 "resources/wordDatabase.txt")

(defn nickname-replace [password]
  "Replaces difficult to pronounce characters with a corresponding word, for easy reading."
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
                     "\\" "back"}]
  (clojure.string/replace password #"!|
                          |\"
                          |\#" replace-map)))



;66632	!
;66633	!!
;66634	"
;66635	#
;66636	##
;66641	$
;66642	$$
;66643	%
;66644	%%
;66645	&
;66646	(
;66651	()
;66652	)
;66653	*
;66654	**
;66655	+
;66656	-
;66661	:
;66662	;
;66663	=
;66664	?
;66665	??
;66666	@


(defn display-to-console [word-list]
  (println (clojure.string/join " " word-list)))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (->> (construct-password 4 5 6 "resources/wordDatabase.txt")
       (display-to-console)))
