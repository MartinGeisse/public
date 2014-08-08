
initializeCodeMirrorAutocompiler = function(id, options, serverCallback) {
	var q = $('#' + id);
	q.data('autocompiler', function(codeMirror) {
		console.log('autocompiling...');
		serverCallback(codeMirror.getDoc().getValue());
	});
};

codeMirrorAutocompilerClearMarkers = function(id) {
	var q = $('#' + id);
	var codeMirror = q.data('codeMirror');
	var markers = codeMirror.getAllMarks();
	for (i in markers) {
		markers[i].clear();
	}
}

addCodeMirrorAutocompilerMarkerToDocument = function(id, startLine, startColumn, endLine, endColumn, errorLevel, message) {
	var q = $('#' + id);
	var codeMirror = q.data('codeMirror');
	codeMirror.markText({line: startLine, ch: startColumn}, {line: endLine, ch: endColumn}, {
		className: 'error-underline',
		inclusiveLeft: false,
		inclusiveRight: false,
		title: message,
	});
}
