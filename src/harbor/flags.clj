(ns harbor.flags
  (:gen-class)
  (:require
   [clj-kit.string-kit :as string-kit]
   [clj-kit.coll-kit :as coll-kit]
   [clojure.string :as string]))

(defn capitalize-letters
  [st ct]
  (cond
   (not (pos? ct)) st
   (string-kit/is-upper? st) st
   (not (string-kit/contains-alpha? st)) st

   :else (let [split (coll-kit/split-nth st (rand-int (count st)))]
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

 (defn number-character
   "Returns a single random numeric character."
   []
   (->
    '("0" "1" "2" "3" "4" "5" "6" "7" "8" "9")
    (rand-nth)))

 (defn insert-number
   "Inserts a random numeric character into a given collection."
  [coll]
  (let [split-list (->
                    (rand-int (inc (count coll)))
                    (split-at coll))]
    (concat (first split-list)
            (conj []
                  (number-character))
                  (second split-list))))

 (defn insert-number-rec
  "Inserts multiple random numeric characters into a given collection."
   [coll, remaining-numbers]
   (if (not (pos? remaining-numbers))
     coll
     (insert-number-rec (insert-number coll) (dec remaining-numbers))))


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
    (concat (first split-list)
            (conj [] (special-character))
            (second split-list))))

 (defn insert-special-rec
  "Inserts multiple random 'special' characters into a given collection."
   [coll, remaining-specials]
   (if (not (pos? remaining-specials))
     coll
     (insert-special-rec (insert-special coll) (dec remaining-specials))))

(defn remove-random
  [vect]
  (coll-kit/remove-nth vect (rand-int (count vect))))

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
