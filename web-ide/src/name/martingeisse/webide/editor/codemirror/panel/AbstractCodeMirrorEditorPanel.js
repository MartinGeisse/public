
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
		
	});
}
