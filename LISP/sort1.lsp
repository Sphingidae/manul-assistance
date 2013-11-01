(defun mysort (l1 p)
	(_sort1 l1 p)
)


(defun _sort (l pr)
	(if (null (cdr l))
		l
		
		(let ((head (car l)) (less nil) (greater nil) (same nil))
			(remove head l :count 1)
			(dolist (item l)
				(if (eq head item)
					(setf same (append (list item) same))

					(if (funcall pr head item)
						(let ()
							(setf less (append (list item) less))
							(setf l (remove item l :count 1))
						)
						
						(let ()
							(setf greater (append (list item) greater))
							(setf l (remove item l :count 1))
						)
					)
				)
			)

			(append (_sort greater pr) same (_sort less pr))
		)
	)

)

