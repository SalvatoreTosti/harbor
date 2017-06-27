(ns harbor.output
  (:gen-class)
  (:require
   [harbor.clipboard :as clipboard]))

(defn console-out
  [st]
  (println st))

(defn wait-print
  ([period]
  (wait-print period "*"))

  ([period symb]
  (loop [x period]
    (when (> x 0)
      (print symb)
      (Thread/sleep 1000)
      (recur (dec x))))
  (println)))

(defn clipboard-out
  ([pass period]
   (let [original-clipping (clipboard/slurp-clipboard)]
    (println "password written to clipboard, valid for " period "seconds")
    (clipboard/spit-clipboard pass)
    (wait-print period)
    (clipboard/spit-clipboard ""))))
