
$.fn.createJsTree = function() {
	this.jstree({
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
			select_multiple_modifier: 'meta',
		},
	});
}
