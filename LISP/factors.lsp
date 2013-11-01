(defun factors (n)
	(fact n nil n n)
)

(defun fact (n l it orig)
	(if (= 0 it)
		nil	
		(let ()
			(if (= 0 (rem n it))
				(if (= 1 it)
 					(if (= orig (reduce '* l))
						(print l)
					)
					(fact (/ n it) (cons it l) (/ n it) orig)
				)
			)
			(fact n l (1- it) orig)
		)
	)
)


