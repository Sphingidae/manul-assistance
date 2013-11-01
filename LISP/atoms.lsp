(defun atoms (l1)
	(if (null l1)
		nil
		(if (atom (car l1))
			(cons (car l1) (atoms (cdr l1)))
			(append (atoms (car l1)) (atoms (cdr l1)))
		) 	
	)
)
