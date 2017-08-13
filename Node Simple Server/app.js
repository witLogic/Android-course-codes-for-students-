var http = require('http');

var simple_html = '<html><header><title>This is title</title></header><body>Hello world</body></html>'

//create a server object:
http.createServer(function (req, res) {
  res.write(simple_html); //write a response to the client
  res.end(); //end the response
}).listen(4000); //the server object listens on port 4000