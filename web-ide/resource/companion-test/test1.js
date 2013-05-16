
var net = require('net'),
	JsonSocket = require('json-socket');

var port = 8082;
var host = '127.0.0.1';
var socket = new JsonSocket(new net.Socket());
socket.connect(port, host);
socket.on('connect', function() {
	
	var a = 0, b = 0;
	function sendNext() {
		var message = {a: a, b: b};
		console.log('Sending: ' + message);
		socket.sendMessage(message);
		a += 1;
		b += 3;
	}
	socket.on('message', function(message) {
		console.log('The result is: ' + message.result);
		setTimeout(sendNext, 2500);
	});
	sendNext();
	
});
