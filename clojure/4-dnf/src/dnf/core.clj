(ns dnf.core)

(defn constant
  "Constructor for constant val (true or false) in logical expression."
  [val]
  {:pre [(or (true? val) (false? val))]}
  (list :const val))

(defn constant?
  "Predicate checking if the expression is a constant."
  [expr]
  (= (first expr) :const))

(defn constant-value
  "Returns constant value is expr is a constant."
  [expr]
  {:pre [(constant? expr)]}
  (second expr))


(defn variable
  "Constructor for variable named ``name'' in logical expression."
  [name]
  {:pre [(keyword? name)]}
  (list :var name))

(defn variable?
  "Predicate checking if the expression is a variable."
  [expr]
  (= (first expr) :var))

(defn variable-name
  "Returns name of the variable if expr is a variable."
  [expr]
  {:pre [(variable? expr)]}
  (second expr))

(defn same-variables?
  "Checks variables for identity."
  [v1 v2]
  (and (variable? v1) (variable? v2) (=(variable-name v1) (variable-name v2))))


(defn conjn
  "Constructor for conjunction of expressions exprs."
  [& exprs]
  (cons :conj exprs))

(defn conjn?
  "Predicate checking if the expression is a conjunction."
  [expr]
  (= (first expr) :conj))


(defn disjn
  "Constructor for disjunction of expressions exprs."
  [& exprs]
  (cons :disj exprs))

(defn disjn? [expr]
  "Predicate checking if the expression is a disjunction."
  (= (first expr) :disj))


(defn impl
  "Constructor for implication of expressions expr1 and expr2."
  [expr1 expr2]
  (list :impl expr1 expr2))

(defn impl?
  "Predicate checking if the expression is an implication."
  [expr]
  (= (first expr) :impl))


(defn invr
  "Constructor for inversion of expression exprs."
  [expr]
  (list :invr expr))

(defn invr?
  "Predicate checking if the expression is an inversion."
  [expr]
  (= (first expr) :invr))

(defn atom?
  "Checks if expr is an atom (a variable or a constant)."
  [expr]
  (or (constant? expr) (variable? expr)))

(defn primitive?
  "Checks if expr is a primitive (an atom or inversion of an atom)."
  [expr]
  (or (atom? expr) (and (invr? expr) (atom? (second expr)))))

(defn clause?
  "Checks if expr is a clause (conjunctions of primitives or a primitive)."
  [expr]
  (or (primitive? expr) (and (conjn? expr) (every? primitive? (next expr)))))

(defn dnf?
  "Checks if expr is in dnf-form (a clause or a disjunction of clauses)."
  [expr]
  (or (clause? expr) (and (disjn? expr) (every? clause? (next expr)))))