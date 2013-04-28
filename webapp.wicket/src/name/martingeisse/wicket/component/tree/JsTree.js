
$.fn.createJsTree = function(options) {
	
	// apply default options
	options = $.extend({
		ajaxCallback: function() {},
		contextMenuData: null,
		interactions: {},
		hotkeys: {},
	}, options);
	
	// handle each tree separately
	this.each(function() {
		$this = $(this);

		// helper functions for default hotkeys
		function navigate(moveFunction) {
			var currentNode = this.data.ui.last_selected || -1;
			var newNode = moveFunction.call(this, currentNode);
			if (newNode) {
				this.hover_node(newNode);
				newNode.children("a:eq(0)").click();
			}
			return false;
		}
		function makeNavigationHandler(navigatorName) {
			return function() {
				return navigate.call(this, this[navigatorName]);
			};
		}
		var upHandler = makeNavigationHandler('_get_prev');
		var downHandler = makeNavigationHandler('_get_next');
		function openHandler() {
			var o = this.data.ui.last_selected;
			if (o && o.length && o.hasClass("jstree-closed")) {
				this.open_node(o);
			}
			return false;
		}
		function closeHandler() {
			var o = this.data.ui.last_selected;
			if (o) {
				if (o.hasClass("jstree-open")) {
					this.close_node(o);
				} else {
					var newNode = this._get_parent(o);
					this.hover_node(newNode);
					newNode.children("a:eq(0)").click();
				}
			}
			return false;
		}
		
		// apply default hotkeys
		var hotkeys = $.extend({
			'up': upHandler,
			'shift+up': upHandler,
			'ctrl+up': upHandler,
			'down': downHandler,
			'shift+down': downHandler,
			'ctrl+down': downHandler,
			'left': closeHandler,
			'shift+left': closeHandler,
			'ctrl+left': closeHandler,
			'right': openHandler,
			'shift+right': openHandler,
			'ctrl+right': openHandler,
		}, options.hotkeys);
		
		// build the jsTree
		$this.jstree({
			plugins: ['themes', 'html_data', 'ui', 'dnd', 'hotkeys'],
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
			hotkeys: hotkeys,
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
		
		// clicking on the tree but not on any specific node should still focus the tree
		$this.on('click', function(e) {
			$this.focus();
		});
		
		// enable / disable hotkeys based on focus (assuming no focus initially)
		$this.on('focusin', function() {
			$this.jstree('enable_hotkeys');
		});
		$this.on('focusout', function() {
			$this.jstree('disable_hotkeys');
		});
		$this.jstree('disable_hotkeys');
		
	});
	
}

$.fn.jstreeInteract = function(interaction, data) {
	this.data('jstree').interact(interaction, data);
	return this;
};

$.fn.jstreeAjaxNodeIndexList = function() {
	return this.data('jstree').getAjaxNodeIndexList();
};

jstreeSoftRefresh = function(treeMarkupId, updatedMarkup) {
	var $tree = $('#' + treeMarkupId);
	
	function getLocalId($node) {
		return $node.children('div').text();
	}
	
	function nodeListToNodeMap(nodeList) {
		var nodeMap = {};
		for (var i=0; i<nodeList.length; i++) {
			var node = nodeList[i];
			nodeMap[getLocalId($(node))] = node;
		}
		return nodeMap;
	}
	
	function mergeNodes($parentElement, $nodes, $patches) {
		var nodeMap = nodeListToNodeMap($nodes);
		var patchMap = nodeListToNodeMap($patches);
		
		// remove deleted nodes, i.e. nodes without patches
		for (var i=0; i<$nodes.length; i++) {
			var $node = $($nodes[i]);
			if (!patchMap[getLocalId($node)]) {
				$node.remove();
			}
		}
		
		// add missing nodes that have a patch, then merge all nodes recursively
		for (var i=0; i<$patches.length; i++) {
			var patch = $patches[i], $patch = $(patch);
			var localId = getLocalId($patch);
			var node = nodeMap[localId];
			
			// add missing nodes
			if (!node) {
				$node = $tree.jstree('create_node', $parentElement[0], 'last', {data: '...', state: 'closed'});
				$node.append('<div style="display: none"></div>');
			} else {
				$node = $(node);
			}
			
			// update the node handle and transfer callback info
			$node.children('a').empty().append($patch.children('a').contents());
			$node.children('div').empty().append($patch.children('div').contents());
			
			// update descendants
			var $subParentElement = $node.children('ul');
			mergeNodes($subParentElement, $subParentElement.children(), $patch.children('ul').children());
			
		}
		
	}
	
	var $parentElement = $tree.children();
	var $roots = $parentElement.children();
	var $rootPatches = $(updatedMarkup).children().children();
	mergeNodes($parentElement, $roots, $rootPatches);
}
