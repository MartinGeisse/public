
$.fn.createCodeMirrorWorkbenchEditor = function(mode, options, otDocumentId, otUsername) {
	this.each(function() {
		
		// determine CodeMirror creation options
		options = $.extend({
			mode: mode,
			indentWithTabs: true,
			indentUnit: 4,
			lineNumbers: true,
			gutter: true,
			matchBrackets: true,
			extraKeys: {
				'Cmd-S': function() {},
			},
			autoSaveDelay: 1000,
			autofocus: true,
		}, options);
		
		// create the CodeMirror instance
		var codeMirror = CodeMirror.fromTextArea(this, options);
		
		/*
		// this function actually sends a save request to the server
		function saveEditor() {
			var cursor = codeMirror.getCursor();
			codeMirror.save();
			$('#hidden-save-button').click();
			codeMirror.setCursor(cursor);
		}
		
		// automatically save 1 second after changing something
		var autosaveTriggered = false;
		codeMirror.on('change', function() {
			if (!autosaveTriggered) {
				autosaveTriggered = true;
				setTimeout(function() {
					autosaveTriggered = false;
					saveEditor();
				}, options.autoSaveDelay);
			}
		});
		*/
		
		// prepare OT
		var EditorClient = ot.EditorClient;
		var SocketIOAdapter = ot.SocketIOAdapter;
		var AjaxAdapter = ot.AjaxAdapter;
		var CodeMirrorAdapter = ot.CodeMirrorAdapter;

		codeMirror.setOption('readOnly', true);
		var socket = io.connect('http://' + selfDomain + ':8081/');
		socket.removeAllListeners('doc');
		socket.removeAllListeners('client_left');
		socket.removeAllListeners('set_name');
		socket.removeAllListeners('ack');
		socket.removeAllListeners('operation');
		socket.removeAllListeners('cursor');
		socket.removeAllListeners('reconnect');
		socket.emit('login', {
			documentId: otDocumentId,
			username : otUsername,
		}).on('doc', function(obj) {
			codeMirror.setOption('readOnly', false);
			codeMirror.setValue(obj.str);
			var revision = obj.revision;
			var clients = obj.clients;
			var serverAdapter = new SocketIOAdapter(socket);
			var codeMirrorAdapter = new CodeMirrorAdapter(codeMirror);
			var editorClient = new EditorClient(revision, clients, serverAdapter, codeMirrorAdapter);
			editorClient.serverAdapter.ownUserName = otUsername;
		});
		
	});
}
