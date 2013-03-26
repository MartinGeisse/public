
// log uncaught exceptions
process.on('uncaughtException', function (exc) {
  console.error(exc);
});

// load modules
var ot = require('ot');
var socketIO = require('socket.io');
var path = require('path');

// start socket.io server
var io = socketIO.listen(8081);
io.configure('production', function () {
  io.set('transports', ['xhr-polling']);
  io.set('polling duration', 10);
});

// --- TODO below this line ---



var str = "# This is a Markdown heading\n\n"
        + "1. un\n"
        + "2. deux\n"
        + "3. trois\n\n"
        + "Lorem *ipsum* dolor **sit** amet.\n\n"
        + "    $ touch test.txt";
var socketIOServer = new ot.EditorSocketIOServer(str, [], 'demo', function (socket, cb) {
  cb(!!socket.mayEdit);
});
io.sockets.on('connection', function (socket) {
  socketIOServer.addClient(socket);
  socket.on('login', function (obj) {
    if (typeof obj.name !== 'string') {
      console.error('obj.name is not a string');
      return;
    }
    socket.mayEdit = true;
    socketIOServer.setName(socket, obj.name);
    socket.emit('logged_in', {});
  });
});

