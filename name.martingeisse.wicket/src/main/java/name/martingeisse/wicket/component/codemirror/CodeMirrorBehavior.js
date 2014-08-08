
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
			gutter: true,
			matchBrackets: true,
			extraKeys: {
				'Cmd-S': function() {},
			},
		}, options);
		
		// create the CodeMirror instance
		var codeMirror = CodeMirror.fromTextArea(this, options);
		
		// update the underlying text area automatically
		codeMirror.on('changes', function() {
			codeMirror.save();
			var autocompiler = $(textField).data('autocompiler');
			if (autocompiler) {
				autocompiler(codeMirror);
			}
		});
		
		$(this).data('codeMirror', codeMirror);
		result = codeMirror;
	});
	return result;
}
