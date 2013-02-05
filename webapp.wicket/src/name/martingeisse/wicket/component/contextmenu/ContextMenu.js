
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

function createCommandVerbContextMenuItem(name, callback) {
	return {
		name: name,
		callback: function(key, options) {
			callback();
		},
	};
}

function createComponentMenuItem(selector) {
	var $selector = $(selector);
	var $originalParent = $selector.parent();
	var markup = $selector.html();
	// console.log(markup);
	markup = markup.replace(/(wicket\:)?id\s*\=\s*\"[^"]*\"/g, '');
	// console.log(markup);
	return {
		name: 'foo',
		type: 'html',
		html: markup,
		onShow: function() {
			this.bind('mouseup', function(e) {
				e.stopPropagation();
			});
			this.bind('keydown', function(e) {
				e.stopPropagation();
			});
		},
		/*
		onHide: function() {
			$originalParent.append(this.children());
		},
		*/
	};
}

function createFileUploadMenuItem(name) {
	var inputMarkup = '<input type="file" class="absolute-cover" style="opacity: 0;" onclick="$(\'.context-menu-root\').hide()">';
	var markup = '<div style="position: relative">' + name + inputMarkup + '</div>';
	return {
		name: name,
		type: 'html',
		html: markup,
		onShow: function() {
			var $fileInput = this.find('input');
			console.log($fileInput);
			this.bind('mouseup', function(e) {
				e.stopPropagation();
			});
			this.bind('keydown', function(e) {
				e.stopPropagation();
			});
		},
	};
	
	// 			final String script = ("$('#" + component.getMarkupId() + "').fileupload({url: '" + url + "\'});");

}
