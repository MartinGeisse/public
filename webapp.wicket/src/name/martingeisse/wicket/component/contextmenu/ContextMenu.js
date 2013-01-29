
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
					hide: function(currentOptions) {
						console.log(currentOptions.$menu);
						for (var i in items) {
							var item = items[i];
							if (item.onHide) {
								item.onHide(item);
							}
						}
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
	var $originalParent = $(selector).parent();
	return {
		name: 'foo',
		type: 'html',
		html: selector,
		onHide: function(item) {
			console.log(item);
		},
	};
}
