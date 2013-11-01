(ns int_memo.test.core
  (:use [int_memo.core])
  (:use [clojure.test]))

(deftest int_trapeze_test
  (is (= 0.5 (int_trapeze (fn [x] x) 1 1)) "int(x) from 0 to 1 must equal 0.5.")
  (is (= 18.0 (int_trapeze (fn [x] x) 5 2)) "int(x) from 8 to 10 must equal 18.0.")
  (is (= 0.3125 (int_trapeze (fn [x] (* x x)) 2 0.5)) "int(x^2) from 0.5 to 1 must equal 0.3125.")
  )

(deftest integral_on_grid_test
  (is (= 0.5 (memoized_integral_on_grid (fn [x] x) 1 0.5 memoized_integral_on_grid)) "int(x) from 0 to 1 must equal 0.5.")
  (is (= 12.5 (memoized_integral_on_grid (fn [x] x) 5 1 memoized_integral_on_grid)) "int(x) from 0 to 5 must equal 12.5.")
  (is (= (+ 0.3125 0.0625) (memoized_integral_on_grid (fn [x] (* x x)) 1 0.5 memoized_integral_on_grid)) "int(x^2) from 0 to 1 must equal 0.3125+0.0625.")
  )

(deftest x_on_grid_test
  (is (= 0.5 (x_on_grid 0.7 0.5)))
  (is (= 1.0 (x_on_grid 1.2 0.5)))
  (is (= 1.5 (x_on_grid 1.4 0.5)))
  )
