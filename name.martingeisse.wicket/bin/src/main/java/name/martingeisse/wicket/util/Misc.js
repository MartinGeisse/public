/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

jQuery.fn.loadPageBodyContents = function(url, data, callback) {
	var query = this;
	function onSuccess(docText) {
		var bodyOpenerStart = docText.indexOf('<body');
		var bodyOpenerEnd = docText.indexOf('>', bodyOpenerStart + 1);
		var bodyCloserStart = docText.indexOf('</body');
		var contentText = docText.substring(bodyOpenerEnd + 1, bodyCloserStart);
		query.html(contentText);
		if (typeof(callback) == 'function') {
			callback();
		}
	}
	$.get(url, data, onSuccess, 'text');
}
