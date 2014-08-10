/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.jsonapi;

import java.io.IOException;
import name.martingeisse.api.handler.AbstractRawPostHandler;
import name.martingeisse.api.handler.ApiNotImplementedException;
import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.api.request.ApiRequestPathChain;
import name.martingeisse.common.javascript.analyze.JsonAnalysisException;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.javascript.jsonbuilder.JsonBuilder;
import name.martingeisse.common.javascript.jsonbuilder.JsonObjectBuilder;
import name.martingeisse.common.javascript.jsonbuilder.JsonValueBuilder;

/**
 * Base class for handlers whose input and output are both JSON. The input
 * is automatically parsed and passed as a {@link JsonAnalyzer}. The output
 * is being built by a {@link JsonBuilder} that is also passed to the
 * subclass handler.
 */
public abstract class AbstractJsonApiHandler extends AbstractRawPostHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.AbstractRawPostHandler#handlePost(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	protected final void handlePost(ApiRequestCycle requestCycle, ApiRequestPathChain path) throws Exception {
		
		// prepare
		JsonAnalyzer input;
		try {
			input = requestCycle.getBodyAsJsonAnalyzer();
		} catch (Exception e) {
			emitError(requestCycle, 400, -1, "JSON syntax error");
			return;
		}

		// execute
		JsonObjectBuilder<String> output = new JsonBuilder().object();
		output.property("errorCode").number(0);
		output.property("errorMessage").nullLiteral();
		try {
			handle(requestCycle, input, output.property("data"));
		} catch (JsonAnalysisException e) {
			emitError(requestCycle, 400, -2, "JSON structural error: " + e.getMessage());
		} catch (ApiNotImplementedException e) {
			emitError(requestCycle, 500, -501, "This API function has not been implemented yet.");
		} catch (JsonApiException e) {
			emitError(requestCycle, 200, e.getErrorCode(), e.getErrorMessage());
		} catch (Exception e) {
			emitError(requestCycle, 500, -500, "internal server error");
		}
		
		// emit
		requestCycle.emitMessageResponse(200, output.end());
		
	}

	/**
	 * The actual request handling.
	 * @param requestCycle the request cycle
	 * @param input the input data
	 * @param output the output builder
	 * @throws Exception on errors
	 */
	protected abstract void handle(ApiRequestCycle requestCycle, JsonAnalyzer input, JsonValueBuilder<?> output) throws Exception;

	/**
	 * 
	 */
	private void emitError(ApiRequestCycle requestCycle, int httpStatus, int errorCode, String errorMessage) throws IOException {
		JsonObjectBuilder<String> output = new JsonBuilder().object();
		output.property("errorCode").number(errorCode);
		output.property("errorMessage").string(errorMessage);
		requestCycle.emitMessageResponse(httpStatus, output.end());
	}
	
}
