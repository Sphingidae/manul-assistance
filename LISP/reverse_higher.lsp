(defun reverse2 (l)
	(if (null l)
		l
		(if (null (cdr l))
		l	
			(cons 
				(car (reverse2 (cdr l))) 
				(reverse2 (cons (car l) (reverse2 (cdr (reverse2 (cdr l))))))
			)
		)
	)
)
