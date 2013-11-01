(defun permutation (l1)
	(permut l1 nil)
)

(defun permut (unused generated)
	(if (null unused)
		(print generated)
		(dolist (item unused)
			(permut (remove item unused :count 1) (cons item generated))
		)
		
	)
)
