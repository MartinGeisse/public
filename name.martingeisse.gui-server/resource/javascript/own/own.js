/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

// Enable fastclick. This must happen on-ready, but before registering other click handlers.
$(function() {
	FastClick.attach(document.body);
});
