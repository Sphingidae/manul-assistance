(ns strings_n.test.core
  (:use [strings_n.core])
  (:use [clojure.test]))

(deftest cartesian_product_test
  (is (= (cartesian_product `() `("1" "2")) `()))
  (is (= (cartesian_product `("") `("1" "2")) `("1" "2")))
  (is (= (cartesian_product `("a" "b") `("1" "2")) `("a1" "a2" "b1" "b2")))
  )

(deftest strings_n_test
  (is (= (set (strings_n 2 `("a" "b" "c"))) #{"ac" "ab" "ba" "bc" "ca" "cb"}))
  (is (= (strings_n 0 `("a" "b" "c")) `("")))
  (is (= (strings_n 2 `()) `()))
  (is (= (strings_n 0 `()) `("")))

  )