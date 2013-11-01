class ParallelizedArray < Array

	def initialize(*args)
		super *args
		@threads = 4
	end

	def parallelize &f 
		slice_size = self.size.to_f/@threads.ceil
		slices = each_slice(slice_size)
		threads = slices.map{|slice| Thread.new {f.call(slice)}}
		threads.map{|thread| thread.value}
	end
	
	def map &f
		self.parallelize{|slice| slice.map(&f)}.reduce([]){|f, i| f+i}
	end

	def any? &f
		self.parallelize{|slice| slice.any?(&f)}.any?
	end

        def all? &f
                self.parallelize{|slice| slice.all?(&f)}.all?   
        end

	def select &f
                self.parallelize{|slice| slice.select(&f)}.reduce([]){|f, i| f+i}
        end

	#USE ONLY if &f is assocoative, AT YOUR OWN RISK. s is used only for the last reduce.
        def reduce (s, &f)
                self.parallelize{|slice| slice.reduce(&f)}.reduce(s, &f)
        end



end

p_array = ParallelizedArray.new(8) {|i| i}

print p_array.map{|i| i*i}, "\n"
print p_array.any?{|i| i.odd?}, "\n"
print p_array.all?, "\n"
print p_array.select{|i| i.even?}, "\n"
print p_array.reduce(12){|f, i| f+i}, "\n"
