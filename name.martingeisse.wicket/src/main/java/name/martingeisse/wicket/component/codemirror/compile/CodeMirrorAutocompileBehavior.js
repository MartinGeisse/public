
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
	var gutterMarkerGlyphicons = {
		info: 'glyphicon-info-sign',
		warning: 'glyphicon-warning-sign',
		error: 'glyphicon-exclamation-sign'
	};
	if (errorLevel != 'info' && errorLevel != 'warning') {
		errorLevel = 'error';
	}
	var gutterMarker, $gutterMarker, applyStyles = false;
	var lineInfo = codeMirror.lineInfo(startLine);
	if (lineInfo.gutterMarkers && lineInfo.gutterMarkers['marker-gutter']) {
		gutterMarker = lineInfo.gutterMarkers && lineInfo.gutterMarkers['marker-gutter'];
		$gutterMarker = $(gutterMarker);
		$gutterMarker.attr('title', $gutterMarker.attr('title') + '\n' + message);
		if (errorLevel == 'warning' && $gutterMarker.hasClass(gutterMarkerGlyphicons.info)) {
			$gutterMarker.removeClass(gutterMarkerGlyphicons.info);
			$gutterMarker.removeClass('autocompile-info-color');
			applyStyles = true;
		} else if (errorLevel == 'error' && !$gutterMarker.hasClass(gutterMarkerGlyphicons.error)) {
			$gutterMarker.removeClass(gutterMarkerGlyphicons.info);
			$gutterMarker.removeClass('autocompile-info-color');
			$gutterMarker.removeClass(gutterMarkerGlyphicons.warning);
			$gutterMarker.removeClass('autocompile-warning-color');
			applyStyles = true;
		}
	} else {
		gutterMarker = document.createElement('div');
		gutterMarker.setAttribute('title', message);
		gutterMarker.setAttribute('style', 'padding-left: 3px;');
		gutterMarker.setAttribute('class', 'glyphicon');
		codeMirror.setGutterMarker(startLine, 'marker-gutter', gutterMarker);
		$gutterMarker = $(gutterMarker);
		applyStyles = true;
	}
	if (applyStyles) {
		$gutterMarker.addClass(gutterMarkerGlyphicons[errorLevel]);
		$gutterMarker.addClass('autocompile-' + errorLevel + '-color');
	}
	
}
