
$.fn.createAjaxFileUploadField = function(url, options) {
	
	// apply default options
	options = $.extend({
		url: url,
	}, options);

	return this.fileupload(options);
};
