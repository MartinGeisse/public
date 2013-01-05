
$.fn.createJavaTextArea = function() {
	this.each(function() {
		
		// determine CodeMirror creation options
		var options = {
			indentWithTabs: true,
			indentUnit: 4,
			lineNumbers: true,
			gutter: true,
			matchBrackets: true,
			extraKeys: {
				// 'Cmd-S': saveEditor, TODO
			},
		};
		
		// create the CodeMirror instance
		var codeMirror = CodeMirror.fromTextArea(this, options);
		
		// save DOM data (currently not needed -- TODO might be needed for marker update)
		// var data = {
		// };
		// $(this).data('javaTextArea', data);
		
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
				}, 1000);
			}
		});
		
	});
}
