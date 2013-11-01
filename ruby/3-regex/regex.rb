ips=[]

def stats(entry) 
	getpost='(GET|POST|HEAD|CONNECT)'
	rx = 	/
		([\d\.]+)				#IP 1
                \ -\ -\ 				#formatting " - - "
		\[([\w\/]+):([\d\:]+)\ ([\d\+-]+)\]	#Date:Time GMT 2 3 4
		\ \"#{getpost}\ (.*?)\"			#GET|POST "chunk of URL" 5
		\ (\d+)\ (\d+)				#Code ProcessingTime 6 7
		\ \"(.*?)\"				#Full URL 8
		\ \"(.*?)\"				#Info
		/x

	if (md = entry.match(rx))
		md.captures
	else
		nil
	end
	
	#print md[1]+"\n"
	#print "Date:"+md[2]+"; time:"+md[3]+"; GMT "+md[4]+"\n"
end

lines=File.open("nginx.access.log").readlines
length=lines.length
ips = lines.reduce([]){|res, entry| res + [stats(entry)[0]]}

ip_hash = Hash.new {|hash, key| hash[key] = 0}
ips.each {|ip| ip_hash[ip] += 1}
result =  ip_hash.sort_by{|key, value| value}
l = result.length
print "Most frequent IP is ", result[l-1][0], " (", result[l-1][1], ")" "\n"
print "Least frequent IP is ", result[0][0], " (", result[0][1], ")" "\n"


