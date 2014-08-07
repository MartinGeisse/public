
initializeCodeMirrorAutocompiler = function(id, options, serverCallback) {
	var q = $('#' + id);
	q.data('autocompiler', function(codeMirror) {
		console.log('autocompiling...');
		serverCallback(codeMirror.getDoc().getValue());
	});
};

addCodeMirrorAutocompilerMarkerToDocument = function(id, startLine, startColumn, endLine, endColumn, errorLevel, message) {
	
}