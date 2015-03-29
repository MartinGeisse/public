
$.fn.createCodeMirrorForTextArea = function(mode, options) {
	var result = null;
	this.each(function() {
		var textField = this;
		
		// determine CodeMirror creation options
		options = $.extend({
			mode: mode,
			indentWithTabs: true,
			indentUnit: 4,
			lineNumbers: true,
			gutters: ['marker-gutter', 'CodeMirror-linenumbers'],
			matchBrackets: true,
			extraKeys: {
				'Cmd-S': function() {},
			},
			autocompileDelay: 1000,
			// viewportMargin: parseFloat('Infinity'),
		}, options);
		
		// create the CodeMirror instance
		var codeMirror = CodeMirror.fromTextArea(this, options);
		
		// this function actually saves the editor and runs the auto-compiler
		function saveAndRunAutocompiler() {
			codeMirror.save();
			var autocompiler = $(textField).data('autocompiler');
			if (autocompiler) {
				autocompiler(codeMirror);
			}
		}
		
		// automatically compile 1 second after changing something
		var autocompileTriggered = false;
		codeMirror.on('changes', function() {
			if (!autocompileTriggered) {
				autocompileTriggered = true;
				setTimeout(function() {
					autocompileTriggered = false;
					saveAndRunAutocompiler();
				}, options.autocompileDelay);
			}
		});
		
		//
		$(this).data('codeMirror', codeMirror);
		result = codeMirror;
	});
	return result;
}
