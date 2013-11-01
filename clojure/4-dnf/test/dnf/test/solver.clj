(ns dnf.test.solver
  (:use [dnf.core])
  (:use [dnf.solver])
  (:use [clojure.test]))

(deftest elim-implic-test
  (is (= (disjn (invr (variable :x)) (variable :y)) (elim-implic (impl (variable :x) (variable :y)))))
  (is (= (invr (disjn (invr (variable :x)) (variable :y))) (elim-implic (invr (impl (variable :x) (variable :y))))))
  )

(deftest invr-inside-test
  (is (= (constant true) (invr-inside (constant true))))
  (is (= (disjn (variable :x) (variable :x)) (invr-inside (disjn (variable :x) (variable :x)))))
  (is (= (conjn (variable :x) (variable :x)) (invr-inside (conjn (variable :x) (variable :x)))))
  (is (= (conjn (variable :x) (variable :x) (variable :y)) (invr-inside (conjn (variable :x) (variable :x) (variable :y)))))

  (is (= (constant false) (invr-inside (invr (constant true)))))
  (is (= (constant true) (invr-inside (invr (invr (constant true))))))
  (is (= (constant true) (invr-inside (invr (constant false)))))

  (is (= (variable :x) (invr-inside (invr (invr (variable :x))))))
  (is (= (invr (variable :x)) (invr-inside (invr (invr (invr (variable :x)))))))
  (is (= (variable :x) (invr-inside (invr (invr (invr (invr (variable :x))))))))
  (is (= (variable :x) (invr-inside (variable :x))))

  (is (= (disjn (variable :x) (variable :x)) (invr-inside (invr (conjn (invr (variable :x)) (invr (variable :x)))))))
  (is (= (conjn (variable :x) (variable :x)) (invr-inside (invr (disjn (invr (variable :x)) (invr (variable :x)))))))

  (is (= (disjn (invr (variable :x)) (variable :y)) (invr-inside (invr (conjn (variable :x) (invr (variable :y)))))))
  (is (= (conjn (invr (variable :x)) (variable :y)) (invr-inside (invr (disjn (variable :x) (invr (variable :y)))))))

  (is (= (conjn (invr (variable :x)) (disjn (invr (variable :y)) (variable :z))) (invr-inside (invr (disjn (variable :x) (conjn (variable :y) (invr (variable :z))))))))

  )

(deftest collapse-test
  (is (= (conjn (invr (variable :x)) (invr (variable :y)) (variable :z)) (collapse conjn? (conjn (invr (variable :x)) (conjn (invr (variable :y)) (variable :z))))))
  (is (= (disjn (invr (variable :x)) (invr (variable :y)) (variable :z)) (collapse disjn? (disjn (invr (variable :x)) (disjn (invr (variable :y)) (variable :z))))))
  (is (= (conjn (invr (variable :x)) (invr (variable :y)) (variable :z1) (variable :z2)) (collapse conjn? (conjn (invr (variable :x)) (conjn (invr (variable :y)) (conjn (variable :z1) (variable :z2)))))))
  )

(deftest fix-conjunction-test
  (is (=
        (disjn (conjn (variable :y) (variable :x) (variable :w)) (conjn (variable :z) (variable :x) (variable :w)))
        (fix-conjunction (conjn (variable :x) (disjn (variable :y) (variable :z)) (variable :w)))
        ))
  (is (=
        (disjn (conjn (variable :y) (variable :x) (variable :w)) (conjn (variable :z) (variable :x) (variable :w)) (conjn (variable :v) (variable :x) (variable :w)))
        (fix-conjunction (conjn (variable :x) (disjn (variable :y) (variable :z) (variable :v)) (variable :w)))
        ))
  )

(deftest fix-one-diff-test
  (is (= (disjn (conjn (variable :y) (variable :x) (variable :w)) (conjn (variable :z) (variable :x) (variable :w)))) (fix-one-diff (conjn (variable :x) (disjn (variable :y) (variable :z)) (variable :w))))
  (is (= (disjn (conjn (variable :y) (variable :x) (variable :w)) (conjn (variable :z) (variable :x) (variable :w)) (conjn (variable :v) (variable :x) (variable :w)))) (fix-one-diff (conjn (variable :x) (disjn (variable :y) (variable :z) (variable :v)) (variable :w))))
  (is (=
        (disjn
          (conjn (variable :y01) (variable :x) (variable :z))
          (conjn (variable :y02) (variable :x) (variable :z))
          (conjn
            (variable :x1)
            (variable :y1)
            (disjn (variable :z11) (variable :z12))))
        (fix-one-diff
          (disjn
            (conjn
              (variable :x)
              (disjn (variable :y01) (variable :y02))
              (variable :z))
            (conjn
              (variable :x1)
              (variable :y1)
              (disjn (variable :z11) (variable :z12)))))))
  )

(deftest last-dnf-step-test
  (is (= (disjn (conjn (variable :a) (variable :a) (variable :d)) (conjn (variable :b) (variable :a) (variable :d)) (conjn (variable :c) (variable :a) (variable :b)) (conjn (variable :d) (variable :a) (variable :b)))
        (last-dnf-step (disjn (conjn (variable :a) (variable :b) (disjn (variable :c) (variable :d))) (conjn (variable :a) (variable :d) (disjn (variable :a) (variable :b)))))))
  )

(deftest dnf-test
  (is (=
        (disjn
          (conjn
            (variable :a) (variable :e) (variable :a))
          (conjn
            (variable :b) (variable :e) (variable :a))
          (conjn
            (variable :a) (invr (variable :d)) (variable :a))
          (conjn
            (variable :b) (invr (variable :d)) (variable :a))
          (conjn
            (variable :c) (constant true) (variable :a))
          (conjn
            (variable :d) (constant true) (variable :a))
          (conjn
            (variable :c) (invr (variable :b)) (variable :a))
          (conjn
            (variable :d) (invr (variable :b)) (variable :a)))

        (dnf (disjn
                    (conjn
                      (variable :a)
                      (impl (variable :b) (constant true))
                      (disjn
                        (variable :c)
                        (variable :d)))
                    (conjn
                      (variable :a)
                      (invr (conjn (variable :d) (invr (variable :e))))
                      (disjn
                        (variable :a)
                        (variable :b)))))))
  (is (= (constant true) (dnf (constant true))))

  )

(deftest assign-value-test
  (is (= (constant true) (assign-value :x true (variable :x))))
  (is (= (variable :y) (assign-value :x true (variable :y))))
  )