
// log uncaught exceptions
process.on('uncaughtException', function (exc) {
  console.error(exc);
});

// load modules
var ot = require('ot');
var socketIO = require('socket.io');
var path = require('path');

// patch the EditorSocketIOServer module
ot.EditorSocketIOServer.prototype.removeClient = function(socket) {
	var clientId = socket.id;
	delete this.users[clientId];
	socket.broadcast['in'](this.docId).emit('client_left', clientId);
}
ot.EditorSocketIOServer.prototype.logClientIds = function() {
	console.info('client IDs:');
	for (var clientId in this.users) {
		console.info('* ' + clientId);
	}
}

// start socket.io server
var io = socketIO.listen(8081);
io.configure('production', function () {
  io.set('transports', ['xhr-polling']);
  io.set('polling duration', 10);
});

// editor servers / documents are stored here while being edited
var editorServers = {};

// TODO should fetch from the IDE
var defaultDocumentContent = "# This is a Markdown heading\n\n"
    + "1. un\n"
    + "2. deux\n"
    + "3. trois\n\n"
    + "Lorem *ipsum* dolor **sit** amet.\n\n"
    + "    $ touch test.txt";

// the server itself
io.sockets.on('connection', function (socket) {
	socket.on('login', function (obj) {
		
		// check "username" parameter
		if (typeof obj.username !== 'string') {
			console.error('invalid "username" parameter');
			return;
		}
		var username = obj.username;

		// check "documentId" parameter
		if (typeof obj.documentId !== 'string') {
			console.error('invalid "documentId" parameter');
			return;
		}
		var documentId = obj.documentId;
		
		// TODO fetch document, find or create server
		if (!(documentId in editorServers)) {
			var documentContent = defaultDocumentContent;
			editorServers[documentId] = new ot.EditorSocketIOServer(documentContent, [], documentId, function (socket, cb) {
				cb(!!socket.editorServer);
			});
		}
		var editorServer = editorServers[documentId];
		
		// remove the client from any previous server
		if (socket.editorServer) {
			var oldEditorServer = socket.editorServer;
			socket.removeAllListeners('operation');
			socket.removeAllListeners('cursor');
			socket.removeAllListeners('disconnect');
			socket.leave(oldEditorServer.docId);
			if (socket.manager.sockets.clients(oldEditorServer.docId).length === 0) {
				oldEditorServer.emit('empty-room');
			}
			oldEditorServer.removeClient(socket);
		}
		
		// add the new client to that server
		socket.editorServer = editorServer;
		editorServer.addClient(socket);
		editorServer.setName(socket, username);
		
	});
});
