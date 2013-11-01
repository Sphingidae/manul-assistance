(defun find-in-list (l a)
	(let* ((path (remove-if 'null (find-the-way l a))))
		(lambda (l1)
			(dolist (i path)
				(if (atom l1)
					(break)
					(setq l1 (funcall i l1))
				)
			)
			l1
		)
	)
)

(defun find-the-way (l a)
	(cond
		((eq a l) 
			(list 'nil))

		((not (atom l))
			(cond
				((not (null (find-the-way (car l) a)))
					(cons 'car (find-the-way (car l) a))
				)
				
				((not (null (find-the-way (cdr l) a)))
					(cons 'cdr (find-the-way (cdr l) a))
				)
	
			(t nil)
			)
		)
		(t nil)
	)
)																																																																															
