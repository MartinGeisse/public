
ab -c 50 -n 1000 http://localhost:8080/

# HTTPS not properly supported!
# 
# -c concurrency (parallel requests)
# -b size (tcpbuffersize in bytes)
# -C name=value (cookies)
# -H headerline (HTTP headers)
# -i (HEAD instead of GET)
# -k (HTTP keepalive instead of single request)
# -n number of requests
# -p postfile (send POST instead of GET; also use -T !)
# -s
# -t timelimit (seconds; implies -n 50000)
# -T content-type (set POST/PUT content type)
# -u putfile (send PUT instead of GET)
# -v verbosity (1-4 ?)