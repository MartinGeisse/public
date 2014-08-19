// --- main application ------------------------------------------------------------

var http = require('http');
var fs = require('fs');
var sandbox = require('sandbox');
var querystring = require('querystring');

function writeResponse(response, subResponseBody) {
	subResponseBody = (subResponseBody ? subResponseBody : '');
	response.writeHead(200, {
		'Content-Type' : 'text/html; charset=utf-8'
	});
	var selectHtml = '<select name="customization" size="1"><option selected="selected" value="">raw</option><option value="custom-1">test</option></select>';
	var inputHtml = '<input type="text" name="url" value="http://www.heise.de">';
	var formHtml = '<form method="POST">' + inputHtml + selectHtml + '<input type="submit"></form>';
	var completeHtml = '<html><body>' + formHtml + '<br /><hr /><br />' + subResponseBody + '</body></html>';
	response.end(completeHtml);
}

function initializeDataCollector(connection, limit, callback) {
	var accumulator = '', error = false;
	connection.on('data', function(data) {
		accumulator += data;
		if (error || (limit != null && accumulator.length > limit)) {
			error = true;
			accumulator = '';
		}
	});
	connection.on('end', function() {
		callback(accumulator);
	});
}

http.createServer(function(request, response) {
	var method = request.method.toUpperCase();
	if (method == 'POST') {
		initializeDataCollector(request, 1e6, function(queryData) {
			var parameters = querystring.parse(queryData);
			if (parameters.url) {
				var customizationName = '' + parameters.customization;
				var customizer;
				if (customizationName && customizationName.indexOf('custom-') == 0 && customizationName.indexOf('/') == -1) {
					customizer = function(x, customizerCallback) {
						try {
							new sandbox().run(fs.readFileSync(customizationName + '.js'), function(output) {
								
								// the result should contain a JSON-encoded object which is again wrapped in a string
								// expression (because Node-Sandbox says so), so we make sure it's in fact a string
								// literal and extract the string by evaluating it
								// NOTE: This is NOT accepting arbitrary input from the script via eval(), it's about
								// removing an encoding that was applied by Node-Sandbox! The encoding itself is
								// "trusted" because Nody-Sandbox is trusted.
								var encodedResultObject = (output.result.substr(0, 1) == '\'' ? eval(output.result) : 'null');
								var resultObject = JSON.parse(encodedResultObject);
								if (resultObject.responseBody) {
									customizerCallback(resultObject.responseBody);
								} else {
									console.log('no response body in customizer script output');
									console.log(output);
									customizerCallback('no response body');
								}
								
							});
						} catch (error) {
							console.log(error);
							customizerCallback(x);
						}
					};
				} else {
					customizer = function(x, customizerCallback) {
						customizerCallback(x);
					};
				}
				http.request(parameters.url, function(subResponse) {
					initializeDataCollector(subResponse, null, function(responseBody) {
						customizer(responseBody, function(customizedResponseBody) {
							writeResponse(response, customizedResponseBody);							
						});
					});
				}).on('error', function(error) {
					console.log(error);
					writeResponse(response);
				}).end();
			} else {
				writeResponse(response);
			}
		});
	} else {
		writeResponse(response);
	}
}).listen(8080);
