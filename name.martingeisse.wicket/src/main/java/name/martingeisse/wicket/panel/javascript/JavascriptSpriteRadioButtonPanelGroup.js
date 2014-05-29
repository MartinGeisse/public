/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

(function() {

	initializeJavascriptSpriteRadioButtonPanelGroup = function(deselectedSpriteData, selectedSpriteData, callback, noSelectionUserDataExpression, members) {
		
		/** prepare global state **/
		var selection = -1;
		
		/** this function is needed both for initialization and to update the sprites **/
		function applySpriteStyle() {
			for (var j in members) {
				var spriteData = ((j + '') == (selection + '')) ? selectedSpriteData : deselectedSpriteData;
				var linkQuery = members[j].linkQuery;
				linkQuery.css('background-image', 'url("' + spriteData.url + '")');
				linkQuery.css('background-position', '-' + spriteData.x + 'px -' + spriteData.y + 'px');
				linkQuery.css('width', spriteData.width + 'px');
				linkQuery.css('height', spriteData.height + 'px');
			}
		}
		
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
				
				/** install click handler **/
				member.linkQuery.click(function() {
	
					/** determine whether this member was just selected or de-selected **/
					var nowSelected = ((selection + '') != (member.index + ''));
					
					/** set new selection **/
					selection = nowSelected ? member.index : -1;
					
					/** set sprite of all buttons **/
					for (var j in members) {
						var spriteData = ((j + '') == (selection + '')) ? selectedSpriteData : deselectedSpriteData;
						var linkQuery = members[j].linkQuery;
						linkQuery.css('background-image', 'url("' + spriteData.url + '")');
						linkQuery.css('background-position', '-' + spriteData.x + 'px -' + spriteData.y + 'px');
						linkQuery.css('width', spriteData.width + 'px');
						linkQuery.css('height', spriteData.height + 'px');
					}
					
					/** invoke the callback **/
					callback(nowSelected ? member.userData : noSelectionUserDataExpression);
					
					return false;
				});

			})();
				
		}
		
		// apply initial style
		applySpriteStyle();
		
	}
	
})();