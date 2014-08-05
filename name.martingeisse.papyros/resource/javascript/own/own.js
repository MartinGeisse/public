/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

// Enable fastclick. This must happen on-ready, but before registering other click handlers.
$(function() {
	FastClick.attach(document.body);
});

// Provide support to pre-select an accordion section.
function preselectAccordionSection(sectionSelector, accordionSelector) {
	
	// apply shortcut syntax for the section selector
	var queryResultIndex = 0;
	if ((typeof sectionSelector) == 'number') {
		queryResultIndex = sectionSelector;
		sectionSelector = '.collapse';
	}
	
	// apply default accordion selector
	if (!accordionSelector) {
		accordionSelector = '#accordion';
	}
	
	// pre-select the section
	$($(accordionSelector).find(sectionSelector)[queryResultIndex]).addClass('in').css('height', '');
	
}

// Mark loading links for styling
$(function() {
	var previousLoadTriggerQuery = null;
	$(document).on('click', '.load-trigger', function() {
		var q = $(this);
		if (previousLoadTriggerQuery != null) {
			previousLoadTriggerQuery.removeClass('loading');
		}
		q.addClass('loading');
		previousLoadTriggerQuery = q;
	});
});
