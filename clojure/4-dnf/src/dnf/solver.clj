(ns dnf.solver
  (:use [dnf.core])
  )

(defn elim-implic
  "Eliminates all implications in logical expression expr."
  [expr]
  (if (impl? expr)
    (disjn (invr (elim-implic (nth expr 1))) (elim-implic (nth expr 2)))
    (if (or (constant? expr) (variable? expr))
      expr
      (cons (first expr) (map elim-implic (next expr))))))

(defmulti invr-inside
  "Moves logical inversions from upper lever to atoms in logical expression expr.
  Implementation is polymorphic."
  (fn [expr]
    (if (not (invr? expr))
      :default
      (first (second expr)))))

(defmethod invr-inside :default [expr]

  (if (or (variable? expr) (constant? expr))
    expr
    (cons (first expr) (map invr-inside (next expr)))))

(defmethod invr-inside :invr [expr]
  (invr-inside (second (second expr))))

(defmethod invr-inside :disj [expr]
  (conjn (invr-inside (invr (nth (second expr) 1))) (invr-inside (invr (nth (second expr) 2)))))

(defmethod invr-inside :conj [expr]
  (disjn (invr-inside (invr (nth (second expr) 1))) (invr-inside (invr (nth (second expr) 2)))))

(defmethod invr-inside :var [expr]
  expr)

(defmethod invr-inside :const [expr]
  (if (true? (second (second expr)))
    (list :const false)
    (list :const true)))


(defn collapse
  "Moves conjunction or disjunction in expression on the higher level possible.
  An example it makes (a&b&c) from (a&(b&c))), or (a|b|c|d) from (a|(b|(c|d))).
  Predicate conjn? or disjn? can be used."
  [predicate expr]
  (if (or (constant? expr) (variable? expr))
    expr
    (let [ collapsed (map (partial collapse predicate) (next expr))
           kwrd (first expr)]
      (if (predicate expr)
        (cons kwrd (reduce (fn [s i]
                  (if (predicate i)
                    (concat s (next i))
                    (concat s (list i))))
                 `() collapsed))
        expr))))


(defn fix-conjunction
  "Uses distributivity to open disjunction and make a disjunction of several conjunctions."
  [expr]
  (let [disjuction (first (filter disjn? (next expr)))
        clause-part (remove (fn [x] (= x disjuction)) (next expr))]
    (apply disjn (map (fn [x] (apply conjn x clause-part)) (next disjuction)))))


(defn fix-one-diff
  "Finds the first ``wrong'' conjunction (with disjunction inside) and manages it."
  [expr]
  (if (primitive? expr)
    expr
    (if (conjn? expr)
      (if (every? primitive? (next expr))
        expr
        (collapse conjn? (fix-conjunction expr)))
      (let [wrong-conj (first (filter
                                (fn [x] (and (conjn? x) (not (every? primitive? (next x)))))
                                (next expr)))
            the-rest (remove (fn [x] (= x wrong-conj)) (next expr))]
        (collapse disjn? (collapse conjn?
          (apply disjn (fix-conjunction wrong-conj) the-rest)))))))


(defn last-dnf-step [expr]
  "Fixes every ``wrong'' conjunction until given formula becomes dnf-formula."
  (if (dnf? expr)
    expr
    (last-dnf-step (fix-one-diff expr))))

(defn dnf [expr]
  "Converts any logical expression with constants, variables, conjunctions,
  disjunctions, inversions and implications to dnf-form."
  (last-dnf-step (collapse disjn? (collapse conjn? (invr-inside (elim-implic expr))))))

(defn assign-value
  "Assigns some fixed value to variable ``name'' in expression expr."
  [name value expr]
  {:pre [(and (keyword? name) (or (true? value) (false? value)))]}
  (if (and (variable? expr) (= name (variable-name expr)))
    (constant value)
    (if (atom? expr)
      expr
      (cons (first expr) (map (partial assign-value name value) (next expr))))))