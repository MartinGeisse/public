
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
		}
	};
}

function createContextMenuItemWithCustomMarkup(markup) {
	return {
		name: 'foo',
		type: 'html',
		html: markup,
	};
}
