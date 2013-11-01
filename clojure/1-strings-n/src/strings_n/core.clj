(ns strings_n.core)

(defn cartesian_product [s1, s2]

  (flatten
    (letfn [(cp1 [i]
              (map (fn [j] (str i j)) s2)
              )]
      (reduce (fn [f, s] (concat f s)) (map cp1 s1) `()))
    )

  )

(defn strings_n [m, sm]
  (let [ptrn (map (fn [i] (str i i)) sm)]
    (letfn
      [(cp_2 [f, s] (cartesian_product f, sm))
       (cp_3 [word]
         (not (some
                (fn [pt] (re-find (re-pattern pt) word))
                ptrn)))]
      (filter cp_3
        (reduce cp_2 `("") (range 1 (+ m 1)))
        )
      )
    )
  )
