(defun reverse1 (l1)
(if (null (cdr l1))
		l1
		(append (reverse1 (rest l1)) (list (first l1)))
	)

)
