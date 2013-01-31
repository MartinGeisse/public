
function createContextMenu(selector, callback, items) {
	var options = {
		trigger: 'none',
		selector: selector,
		build: function() {
			return {
				callback: callback,
				items: items,
				position: function(options, x, y) {
					options.$menu.css({left: x + 2, top: y + 2});
				},
				zIndex: 10,
				events: {
					show: function(currentOptions) {
						currentOptions.$menu.children('.context-menu-html').removeClass('not-selectable');
						currentOptions.$menu.children('.context-menu-item').each(function() {
							var $this = $(this);
							var itemOptions = currentOptions.items[$this.data().contextMenuKey];
							if (itemOptions.onShow) {
								itemOptions.onShow.call($this);
							}
						});
					},
					hide: function(currentOptions) {
						currentOptions.$menu.children('.context-menu-item').each(function() {
							var $this = $(this);
							var itemOptions = currentOptions.items[$this.data().contextMenuKey];
							if (itemOptions.onHide) {
								itemOptions.onHide.call($this);
							}
						});
					}
				},
			};
		}
	};
	$.contextMenu(options);
	return {options: options};
}

function createContextMenuItemWithPrompt(name, promptText, callback) {
	return {
		name: name,
		callback: function(key, options) {
			$('.context-menu-root').hide();
			var data = prompt(promptText);
			if (data != null) {
				callback(key, JSON.stringify(data), options);
			}
		},
	};
}

function createComponentMenuItem(selector) {
	var $selector = $(selector);
	var $originalParent = $selector.parent();
	return {
		name: 'foo',
		type: 'html',
		html: selector,
		onShow: function() {
			this.bind('mouseup', function(e) {
				e.stopPropagation();
			});
			this.bind('keydown', function(e) {
				e.stopPropagation();
			});
		},
		onHide: function() {
			$originalParent.append(this.children());
		},
	};
}
