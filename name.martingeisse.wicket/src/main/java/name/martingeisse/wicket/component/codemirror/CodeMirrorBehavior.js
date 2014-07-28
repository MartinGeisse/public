
$.fn.createCodeMirrorForTextArea = function(mode, options) {
	var result = null;
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
		}, options);
		
		// create the CodeMirror instance
		var codeMirror = CodeMirror.fromTextArea(this, options);
		
		// update the underlying text area automatically
		codeMirror.on('changes', function() {
			codeMirror.save();
		});
		
		result = codeMirror;
	});
	return result;
}
