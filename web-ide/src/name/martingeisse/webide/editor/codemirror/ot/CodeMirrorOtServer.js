
// log uncaught exceptions
process.on('uncaughtException', function (exc) {
  console.error(exc);
});

// load modules
var ot = require('ot');
var socketIO = require('socket.io');
var path = require('path');
var http = require('http');

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

		// remove the client from the previous server, if any
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
			socket.editorServer = null;
		}

		// this function gets invoked either directly (if the document has already been
		// loaded) or when the document-load response arrives
		function onEditorServerReady(editorServer) {
			socket.editorServer = editorServer;
			editorServer.addClient(socket);
			editorServer.setName(socket, username);
		}
		
		// load the document if necessary
		if (documentId in editorServers) {
			onEditorServerReady(editorServers[documentId]);
		} else {
			var documentContent = '';
			var requestOptions = {
				hostname: 'localhost',
				port: 8080,
				path: '/wicket/resource/name.martingeisse.webide.resources.WorkspaceWicketResourceReference/' + documentId
			};
			http.get(requestOptions, function(response) {
				response.on('data', function(chunk) {
					documentContent += chunk;
				});
				response.on('end', function() {
					documentContent = documentContent.replace(/\r(\n)?/g, '\n');
					editorServers[documentId] = new ot.EditorSocketIOServer(documentContent, [], documentId, function (socket, cb) {
						cb(!!socket.editorServer);
					});
					editorServers[documentId].on('empty-room', function() {
						// TODO: keep some N minutes longer in case a user comes back (kind of caching)
						delete editorServers[documentId];
					});
					onEditorServerReady(editorServers[documentId]);
				});
			});
		}
		
	});
});
