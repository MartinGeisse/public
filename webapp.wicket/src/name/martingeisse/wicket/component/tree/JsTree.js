
$.fn.createJsTree = function(options) {
	
	// apply default options
	options = $.extend({
		ajaxCallback: function() {},
		contextMenuData: null,
	}, options);
	
	// handle each tree separately
	this.each(function() {
		$this = $(this);
		
		// build the jsTree
		$this.jstree({
			plugins: ['themes', 'html_data', 'ui', 'dnd'],
			core: {
				animation: 0
			},
			themes: {
				theme: 'apple',
				url: '/wicket/resource/name.martingeisse.webide.workbench.WorkbenchPage/jstree.css',
				dots: false,
				icons: false,
			},
			ui: {
				select_multiple_modifier: false,
				select_range_modifier: false,
			},
		});

		// function to send an AJAX request using the supplied callback
		function getAjaxNodeIndexList() {
			var selectedNodes = $this.jstree('get_selected');
			var selectedNodeIndices = $.map(selectedNodes.children('div'), function(x) {
				return $(x).text();
			});
			return selectedNodeIndices.join(':');
		}
		function sendAjaxRequest(interaction, data) {
			options.ajaxCallback(interaction, getAjaxNodeIndexList(), data);
		}

		// the data object used to communicate with other sub-functions
		var storedData = {
			sendAjaxRequest: sendAjaxRequest,
			getAjaxNodeIndexList: getAjaxNodeIndexList,
		};
		$this.data('jstree', storedData);
		
		// node select helpers
		function selectSingleElement(element) {
			$this.jstree('deselect_all');
			$this.jstree('select_node', element, false);
		}
		function toggleSingleElement(element) {
			$this.jstree('toggle_select', element);
		}
		function selectForContextMenu(element, metaKeyPressed) {
			if (!$this.jstree('is_selected', element)) {
				if (!metaKeyPressed) {
					$this.jstree('deselect_all');
				}
				$this.jstree('select_node', element, false);
			}
		}
		
		// event handlers
		$this.undelegate('a', 'click.jstree');
		$this.delegate('a', 'click.jstree', $.proxy(function(event) {
			if (event.metaKey) {
				toggleSingleElement(event.currentTarget);
			} else if (event.shiftKey) {
				// TODO: range select
			} else {
				selectSingleElement(event.currentTarget);
			}
		}, $this));
		$this.delegate('a', 'dblclick.jstree', $.proxy(function(event) {
			selectSingleElement(event.currentTarget);
			sendAjaxRequest('dblclick', null);
		}));
		if (options.contextMenuData != null) {
			$this.delegate('a', 'contextmenu.jstree', $.proxy(function(event) {
				selectForContextMenu(event.currentTarget, event.metaKey);
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
			}));
		}
		
	});
	
}

$.fn.jstree_ajax = function(interaction, data) {
	this.data('jstree').sendAjaxRequest(interaction, data);
	return this;
};

$.fn.jstree_ajax_node_index_list = function(interaction, data) {
	return this.data('jstree').getAjaxNodeIndexList();
};
