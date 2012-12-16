
$.fn.selectableElements = function(options) {

	// apply default options
	options = $.extend({
		elementSelector : null,
		valueExtractor : function(element) {
			return element;
		},
		ajaxCallback : function() {
		},
		notSelectedClass : null,
		selectedClass : null,
		notSelectedStyle : null,
		selectedStyle : null
	}, options);

	// handle each selection context separately
	this.each(function() {
		var $this = $(this);
		var selected = [];

		// function to extract the values from the selected elements
		function getSelectedValues() {
			return $.map(selected, options.valueExtractor);
		}

		// function to send an AJAX request using the supplied callback
		function sendAjaxRequest(interaction) {
			options.ajaxCallback(interaction, JSON
					.stringify(getSelectedValues()));
		}

		// the data object used to communicate with other sub-functions
		var storedData = {
			selected : selected,
			getSelectedValues : getSelectedValues,
			sendAjaxRequest : sendAjaxRequest,
		};
		$this.data('selectableElements', storedData);

		// helper functions for event handlers
		function selectSingleElementForEvent(event) {
			var element = event.delegateTarget;
			$(selected).applyStyle(options.selectedClass,
					options.notSelectedClass, options.notSelectedStyle);
			$(element).applyStyle(options.notSelectedClass,
					options.selectedClass, options.selectedStyle);
			selected = [ element ];
		}
		function toggleSingleElementForEvent(event) {
			var element = event.delegateTarget;
			var $element = $(element);
			var index = $.inArray(element, selected);
			if (index == -1) {
				selected.push(element);
				$element.applyStyle(options.notSelectedClass,
						options.selectedClass, options.selectedStyle);
			} else {
				selected.splice(index, 1);
				$element.applyStyle(options.selectedClass,
						options.notSelectedClass, options.notSelectedStyle);
			}
		}

		// add event handlers
		var $allElements = $(options.elementSelector, this);
		$allElements.click(function(event) {
			if (event.metaKey) {
				toggleSingleElementForEvent(event);
			} else if (event.shiftKey) {
				// TODO: range select
			} else {
				selectSingleElementForEvent(event);
			}
		});
		$allElements.dblclick(function(event) {
			selectSingleElementForEvent(event);
			sendAjaxRequest('dblclick');
		});
	});

	return this;
}

$.fn.selectableElements_get = function() {
	return this.data('selectableElements').getSelectedValues();
};

$.fn.selectableElements_getElements = function() {
	return this.data('selectableElements').selected;
};

$.fn.selectableElements_ajax = function(interaction) {
	this.data('selectableElements').sendAjaxRequest(interaction);
	return this;
};
