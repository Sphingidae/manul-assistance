m = 3
sm = ["a", "b", "c", "d", "e"]

ptrn = sm.map{|i| i+i}

def cartesian_product(s1, s2)
	unwr = s1.map{|i| s2.map{|j| i+j}}
	unwr.reduce([]){|f, s| f+s}
end

res = (1..m).reduce(['']){|f, s| cartesian_product(sm, f)}

result = res.select{|word| !ptrn.any?{|pt| word.match(/.*#{pt}.*/)}}

print result
