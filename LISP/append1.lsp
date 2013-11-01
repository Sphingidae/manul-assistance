(defun append1 (l1 l2)
	(if (null l1)
		l2
	(cons (first l1) (append1 (rest l1) l2))))
