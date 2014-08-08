
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
	codeMirror.clearGutter('marker-gutter');
}

addCodeMirrorAutocompilerMarkerToDocument = function(id, startLine, startColumn, endLine, endColumn, errorLevel, message) {
	
	// prepare
	errorLevel = errorLevel.toLowerCase();
	var q = $('#' + id);
	var codeMirror = q.data('codeMirror');
	
	// add a text marker
	codeMirror.markText({line: startLine, ch: startColumn}, {line: endLine, ch: endColumn}, {
		className: errorLevel + '-underline',
		inclusiveLeft: false,
		inclusiveRight: false,
		title: message,
	});
	
	// add a gutter marker
	// TODO: overwrites previous marker for the same line; should be merged
	if (errorLevel == 'info') {
		var gutterMarkerGlyphicon = 'info-sign';
		var gutterMarkerColor = 'blue';
	} else if (errorLevel == 'warning') {
		var gutterMarkerGlyphicon = 'warning-sign';
		var gutterMarkerColor = '#f08000';
	} else {
		var gutterMarkerGlyphicon = 'exclamation-sign';
		var gutterMarkerColor = 'red';
	}
	var lineInfo = codeMirror.lineInfo(startLine);
	if (lineInfo.gutterMarkers && lineInfo.gutterMarkers['marker-gutter']) {
		var $element = $(lineInfo.gutterMarkers['marker-gutter']);
		$element.attr('title', $element.attr('title') + '\n' + message);
	} else {
		var gutterMarker = document.createElement('div');
		gutterMarker.setAttribute('title', message);
		gutterMarker.setAttribute('class', 'glyphicon glyphicon-' + gutterMarkerGlyphicon);
		gutterMarker.setAttribute('style', 'padding-left: 3px; color: ' + gutterMarkerColor);
		codeMirror.setGutterMarker(startLine, 'marker-gutter', gutterMarker);
	}
	
	// add a message to the error view, if any
	var errorView = q.data('autocompile-errorview');
	if (errorView) {
		var $icon = $('<span/>').addClass('glyphicon glyphicon-' + gutterMarkerGlyphicon);
		var $entry = $('<div/>').css('color', gutterMarkerColor).append($icon).append(message);
		$(errorView).append($entry);
	}
	
}
