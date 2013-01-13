
$.fn.createJsTree = function() {
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
		
		// node select helpers
		function selectSingleElement(element) {
			$this.jstree('deselect_all');
			$this.jstree('select_node', element, false);
		}
		function toggleSingleElement(element) {
			$this.jstree('toggle_select', element);
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
		
	});
}
