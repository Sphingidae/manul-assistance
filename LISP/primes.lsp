(defun primes (N)
	(remove-if 'compl? (loop for i from 2 to (eval N) collect i))
)

(defun compl? (a)
	(let ((_count 0))
		;(print (loop for j from 2 to a collect j))
		(dolist (item (loop for j from 2 to a collect j))
			(if (eq 0 (rem a item)) 
				(let ()
					(setf _count (+ 1 _count))
					(setf _count _count)
					;(print _count)
				)
			)
		)
		(if (eq 1 _count)
			nil
			t
		)
	)
)

