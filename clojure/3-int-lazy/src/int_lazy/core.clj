(ns int_lazy.core)

(defn int_trapeze
  "Calculates integral(f) from (pos-1)*s to pos*s using trapezoid rule with spacing s."
  [f, pos, s]
  (* (/ (+ (f (* pos (float s))) (f (* (dec pos) (float s)))) 2) s)
  )

(defn x_on_grid
  "Returns the closest grid point to x if the grid spacing is equal to x."
  [x, s]
  (* (Math/round (/ x s)) s)
  )

(defn integrate
  "Returns lambda-function(x) that calculates integral(f) from 0 to x."
  [f, s]
  (let [integrals_x (iterate
                      (fn [y]
                        (list
                          (+ (int_trapeze f (second y) s) (first y)) (+ 1 (second y))))
                      (list 0 1))]
    (fn [n] (first (nth integrals_x (x_on_grid (/ n s) s))))
    )

  )

(def fnc (integrate (fn [x] x) 0.5))

(time fnc)
(time (fnc 1))
(time (fnc 500))
(time (fnc 500.2))
(time (fnc 456.8))
(time (fnc 6000))
