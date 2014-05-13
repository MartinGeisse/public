/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

(function() {

	initializeJavascriptImageRadioButtonPanelGroup = function(deselectedIconUrl, selectedIconUrl, callback, noSelectionUserDataExpression, members) {
		
		/** prepare global state **/
		var selection = -1;
		
		/** prepare members **/
		for (var i in members) {
			
			/**
			 * For loops don't create a variable scope in Javascript, only functions do. For loops
			 * re-use the scope of their containing function. Since we define a nested function
			 * inside this for loop that we want to refer to the current iteration (the click
			 * handler), this is a problem for us. To solve the issue, we use another nested
			 * function to create a scope for the current iteration.
			 */
			(function() {
				var index = i;
				var member = members[index];
				
				/** prepare queries **/
				member.index = index;
				member.panelQuery = $(member.selector);
				member.panel = member.panelQuery[0];
				member.linkQuery = member.panelQuery.find('a');
				member.link = member.linkQuery[0];
				member.imageQuery = member.panelQuery.find('img');
				member.image = member.imageQuery[0];
				
				/** install click handler **/
				member.linkQuery.click(function() {
	
					/** determine whether this member was just selected or de-selected **/
					var nowSelected = ((selection + '') != (member.index + ''));
					
					/** set new selection **/
					selection = nowSelected ? member.index : -1;
					
					/** set image of all buttons **/
					for (var j in members) {
						var src = ((j + '') == (selection + '')) ? selectedIconUrl : deselectedIconUrl;
						members[j].imageQuery.attr('src', src);
					}
					
					/** invoke the callback **/
					callback(nowSelected ? member.userData : noSelectionUserDataExpression);
					
					return false;
				});

			})();
				
		}
		
	}
	
})();