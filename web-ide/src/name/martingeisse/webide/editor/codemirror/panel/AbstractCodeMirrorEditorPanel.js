
$.fn.createCodeMirrorWorkbenchEditor = function(mode, options) {
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
		var editorClient;
		var socket = io.connect('http://localhost:8081/');
		socket.on('doc', function(obj) {
			
			codeMirror.setValue(obj.str);
			var revision = obj.revision;
			var clients = obj.clients;
			var serverAdapter = new SocketIOAdapter(socket);
			var codeMirrorAdapter = new CodeMirrorAdapter(codeMirror);
			editorClient = new EditorClient(revision, clients, serverAdapter, codeMirrorAdapter);
			
			var username = 'foo';
			socket.emit('login', {
				name : username
			}).on('logged_in', function() {
				editorClient.serverAdapter.ownUserName = username;
				codeMirror.setOption('readOnly', false);
			});
			
		});
		
	});
}
