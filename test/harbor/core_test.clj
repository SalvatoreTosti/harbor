(ns harbor.core-test
  (:require [clojure.test :refer :all]
            [harbor.core :refer :all]))

(deftest read-datab-test
  (testing "read-datab on bad location"
    (is (thrown? AssertionError (read-datab nil)))
    (is (thrown? AssertionError (read-datab "this-does-not-exist.txt")))
    ))

(deftest map-from-datab-test
  (testing "map-from-datab on bad location"
    (is (thrown? AssertionError (map-from-datab nil)))
    (is (thrown? AssertionError (map-from-datab "this-does-not-exist.txt")))
  ))

(deftest generate-number-string-test
  (testing "generate-number-string bad input"
    (is (thrown? AssertionError (generate-number-string nil nil)))
    (is (thrown? AssertionError (generate-number-string 0 1)))
    (is (thrown? AssertionError (generate-number-string 1 10)))
    (is (thrown? AssertionError (generate-number-string 1 0)))
    )
  (testing "generate-number-string regular input"
    (is (= "1"  (generate-number-string 1 1)))
    (is (= 1 (count (generate-number-string 1 1))))

    (is (re-matches #"[12]{1}"  (generate-number-string 1 2)))
    (is (re-matches #"[1-9]{2}"  (generate-number-string 2 9)))))

(deftest get-single-word-test
  (testing "get-single-word bad input"
    (is (thrown? AssertionError (get-single-word nil "test")))
    (is (thrown? AssertionError (get-single-word {} "test")))
    (is (thrown? AssertionError (get-single-word {:test "test"} nil)))
    (is (thrown? AssertionError (get-single-word {:test "test"} ""))))
  (testing "get-single-word regular input"
      (is (= :test  (get-single-word {"test" :test} "test")))))

(deftest construct-password-test
  (testing "construct-password bad input"
    (is (thrown? AssertionError (construct-password "1" 5 1 "resources/wordDatabaseShort.txt")))
    (is (thrown? AssertionError (construct-password 0 5 1 "resources/wordDatabaseShort.txt")))
    (is (thrown? AssertionError (construct-password -1 5 1 "resources/wordDatabaseShort.txt")))

    (is (thrown? AssertionError (construct-password 1 "1" 1 "resources/wordDatabaseShort.txt")))
    (is (thrown? AssertionError (construct-password 1 0 1 "resources/wordDatabaseShort.txt")))
    (is (thrown? AssertionError (construct-password 1 -1 1 "resources/wordDatabaseShort.txt")))
    (is (thrown? AssertionError (construct-password 1 4 1 "resources/wordDatabaseShort.txt")))
    (is (thrown? AssertionError (construct-password 1 6 1 "resources/wordDatabaseShort.txt")))

    (is (thrown? AssertionError (construct-password 1 5 "1" "resources/wordDatabaseShort.txt")))
    (is (thrown? AssertionError (construct-password 1 5 0 "resources/wordDatabaseShort.txt")))
    (is (thrown? AssertionError (construct-password 1 5 -1 "resources/wordDatabaseShort.txt")))

    (is (thrown? AssertionError (construct-password 1 5 1 "this-does-not-exist.txt")))
  )

  (testing "construct-password regular input"
    (is (every? #(re-matches #"[a-z]+" %) (construct-password 1 5 1 "resources/wordDatabase.txt")))
    (is (every? #(re-matches #"[a-z]+" %) (construct-password 2 5 1 "resources/wordDatabase.txt")))
    (is (every? #(re-matches #"[a-z]+" %) (construct-password 2 5 2 "resources/wordDatabase.txt")))
    )
  )

(deftest nickname-replace-test
  (testing "nickname-replace bad input"
    (is (thrown? AssertionError (nickname-replace 1)))
    (is (thrown? AssertionError (nickname-replace :not-a-string)))
  )
  (testing "nickname-replace regular input"
    (are [x y] (= (nickname-replace x) y)
    "!" "bang"
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
    )))

(deftest valid-arguments-test
  (testing "valid-arguments? bad input"
    (is (thrown? AssertionError (valid-arguments? "test"))
    (is (thrown? AssertionError (valid-arguments? "0"))
    (is (thrown? AssertionError (valid-arguments? "-1"))
    ))))
  (testing "valid-arguments regular input"
    (is (= true (valid-arguments? "1")))
    (is (= true (valid-arguments? "10")))
    ))
