
var FileSystem = require('fs');
var Sandbox = require('sandbox');

var customizationCode = FileSystem.readFileSync('customization.js');
var sandbox = new Sandbox();
sandbox.run(customizationCode, function(output) {
	console.log(output);
});
