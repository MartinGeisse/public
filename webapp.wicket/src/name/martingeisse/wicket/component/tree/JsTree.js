
$.fn.createJsTree = function(options) {
	
	// apply default options
	options = $.extend({
		ajaxCallback: function() {},
		contextMenuData: null,
		interactions: {},
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
		function interact(interaction, data) {
			function doAjaxCall() {
				options.ajaxCallback(interaction, getAjaxNodeIndexList(), data);
			}
			if (options.interactions[interaction]) {
				options.interactions[interaction](doAjaxCall);
			} else {
				doAjaxCall();
			}
		}

		// the data object used to communicate with other sub-functions
		var storedData = {
			interact: interact,
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
			interact('dblclick', null);
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

$.fn.jstreeInteract = function(interaction, data) {
	this.data('jstree').interact(interaction, data);
	return this;
};

$.fn.jstreeAjaxNodeIndexList = function() {
	return this.data('jstree').getAjaxNodeIndexList();
};
