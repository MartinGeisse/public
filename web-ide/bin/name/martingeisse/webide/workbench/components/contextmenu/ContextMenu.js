
function createContextMenu(selector, callback, items) {
	$.contextMenu({
		selector: selector,
		build: function() {
			return {
				callback: callback,
				items: items
			};
		}
	});
}

function createContextMenuItemWithPrompt(name, promptText, callback) {
	return {
		name: name,
		callback: function(key, options) {
			$(document).queue('mainLoopModal', function(next)) {
				callback(key, JSON.stringify(prompt(promptText)), options);
				next();
			}
		}
	};
}
