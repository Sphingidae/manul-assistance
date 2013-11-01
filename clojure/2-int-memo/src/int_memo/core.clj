(ns int_memo.core)

(defn int_trapeze
  "Calculates integral(f) from (pos-1)*s to pos*s using trapezoid rule with spacing s."
  [f, pos, s]
  (* (/ (+ (f (* pos (float s))) (f (* (dec pos) (float s)))) 2) s)
  )

(defn integral_on_grid
  "Calculates integral(f) from 0 to x using trapezioid rule with spacing s, where x is a point from grid."
  [f, x, s, memoized_integral_on_grid]
  (if (> x 0)
    (+ (int_trapeze f (int (/ x s)) s) (memoized_integral_on_grid f (- x s) s, memoized_integral_on_grid))
    0)
  )

(def memoized_integral_on_grid (memoize integral_on_grid))

(defn x_on_grid
  "Returns the closest grid point to x if the grid spacing is equal to x."
  [x, s]
  (* (Math/round (/ x s)) s)
  )

(defn integral
  "Returns lambda-function(x) that returns integral from 0 to x for func using trapezioid rule with spacing s."
  [func, s]
  (fn [x] (memoized_integral_on_grid func (x_on_grid x s) s memoized_integral_on_grid))
  )


(def funct (fn [x] x))
(time (memoized_integral_on_grid funct 100 1 memoized_integral_on_grid))
(time (memoized_integral_on_grid funct 100 1 memoized_integral_on_grid))
(time (memoized_integral_on_grid funct 101 1 memoized_integral_on_grid))
(time (memoized_integral_on_grid funct 99 1 memoized_integral_on_grid))

(def intgrl (integral (fn [x] (* x x)) 0.5))

(time (print (intgrl 1)))
(time (print (intgrl 500)))
(time (print (intgrl 500.2)))
(time (print (intgrl 456.8)))
(time (print (intgrl 600)))
