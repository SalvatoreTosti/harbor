(ns harbor.secureRandom
  (:gen-class)
  (:import java.security.SecureRandom))

(defn secure-generate-backend [hi]
  (.nextInt (SecureRandom.) hi))

(defn secure-generate-number [hi]
  "Returns a randomly generated number between 1 and 'hi', inclusive."
  (inc (secure-generate-backend hi)))
