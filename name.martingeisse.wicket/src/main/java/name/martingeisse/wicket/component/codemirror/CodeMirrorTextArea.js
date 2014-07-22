
$.fn.createCodeMirrorForTextArea = function(mode, options) {
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
		
	});
}
