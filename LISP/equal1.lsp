(defun equal1 (l1 l2)
	(if (or (atom l1) (atom l2)) 
		(eql l1 l2)
		(and (equal1 (car l1) (car l2)) (equal1 (cdr l1) (cdr l2))) 
	)
)


