
$.fn.selectableElements = function(options) {

	// apply default options
	options = $.extend({
		elementSelector: null,
		valueExtractor: function(element) {
			return element;
		},
		ajaxCallback: function() {
		},
		notSelectedClass: null,
		selectedClass: null,
		notSelectedStyle: null,
		selectedStyle: null,
		contextMenuData: null,
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
		function sendAjaxRequest(interaction, data) {
			options.ajaxCallback(interaction, JSON.stringify(getSelectedValues()), data);
		}

		// the data object used to communicate with other sub-functions
		var storedData = {
			selected : selected,
			getSelectedValues : getSelectedValues,
			sendAjaxRequest : sendAjaxRequest,
		};
		$this.data('selectableElements', storedData);

		// helper functions for event handlers
		function selectNewElement(element) {
			selected.push(element);
			$(element).applyStyle(options.notSelectedClass, options.selectedClass, options.selectedStyle);
		}
		function deselectSelectedElement(element, index) {
			selected.splice(index, 1);
			$(element).applyStyle(options.selectedClass, options.notSelectedClass, options.notSelectedStyle);
		}
		function deselectAllElements() {
			$(selected).applyStyle(options.selectedClass, options.notSelectedClass, options.notSelectedStyle);
			selected = [];
		}
		function selectSingleElementForEvent(event) {
			var element = event.delegateTarget;
			deselectAllElements();
			selectNewElement(element);
		}
		function toggleSingleElementForEvent(event) {
			var element = event.delegateTarget;
			var index = $.inArray(element, selected);
			if (index == -1) {
				selectNewElement(element);
			} else {
				deselectSelectedElement(element, index);
			}
		}
		function selectForContextMenu(event) {
			var element = event.delegateTarget;
			var index = $.inArray(element, selected);
			if (index == -1) {
				if (!event.metaKey) {
					deselectAllElements();
				}
				selectNewElement(element);
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
			sendAjaxRequest('dblclick', null);
		});
		if (options.contextMenuData != null) {
			$allElements.bind('contextmenu', function(event) {
				selectForContextMenu(event);
				var handlers = $(this).contextMenu('handlers');
				var fakeEvent = {
					preventDefault: function() {},	
					stopImmediatePropagation: function() {},
					data: options.contextMenuData.options,
					originalEvent: null,
					pageX: event.pageX,
					pageY: event.pageY,
				};
				var trigger = $(options.contextMenuData.options.selector)[0];
				handlers.contextmenu.call(trigger, fakeEvent);
			});
		}
	});

	return this;
}

$.fn.selectableElementsGet = function() {
	return this.data('selectableElements').getSelectedValues();
};

$.fn.selectableElementsGetElements = function() {
	return this.data('selectableElements').selected;
};

$.fn.selectableElementsAjax = function(interaction, data) {
	this.data('selectableElements').sendAjaxRequest(interaction, data);
	return this;
};
