
// import modules
var net = require('net'),
	events = require("events"),
	JsonSocket = require('json-socket');

// parse command line
var companionId = parseInt(process.argv[2], 10);

// IDE library
var ide;
(function() {
	
	// create the IDE global object
	ide = {};
	
	// initialize the IPC subsystem
	var socket, connected = false;
	ide.ipc = new events.EventEmitter();
	ide.ipc.send = function(type, data) {
		if (connected) {
			socket.sendMessage({type: 'ipc', ipcEventType: type, ipcEventData: data});
		} else {
			console.error("ide.ipc.send: not yet connected");
		}
	};
	
	// establish the connection to the IDE process
	var port = 8082;
	var host = '127.0.0.1';
	socket = new JsonSocket(new net.Socket());
	socket.connect(port, host);
	socket.on('connect', function() {
		socket.sendMessage({type: 'init', companionId: companionId});
		connected = true;
		ide.ipc.emit('connect', {});
		socket.on('message', function(envelope) {
			if (envelope.type == 'ipc') {
				ide.ipc.emit(envelope.ipcEventType, envelope.ipcEventData);
			}
		});
	});
	
})();


// main
var a = 0, b = 0;
function sendNext() {
	
	// send messages
	var data = {a: a, b: b};
	console.log('Sending: %j', data);
	ide.ipc.send('add', data);
	ide.ipc.send('sub', data);
	ide.ipc.send('mul', data);
	
	// schedule next
	a += 1;
	b += 3;
	setTimeout(sendNext, 2500);
	
}
ide.ipc.on('connect', sendNext);
ide.ipc.on('result', function(result) {
	console.log('Result: %j', result);
});
