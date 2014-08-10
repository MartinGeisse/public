/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.api.account;

import javax.servlet.http.Cookie;
import name.martingeisse.api.handler.jsonapi.AbstractJsonApiHandler;
import name.martingeisse.api.handler.jsonapi.JsonApiException;
import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.javascript.jsonbuilder.JsonValueBuilder;
import name.martingeisse.common.security.SecurityTokenUtil;
import name.martingeisse.miner.server.MinerServerSecurityConstants;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.webide.entity.QUserAccount;
import name.martingeisse.webide.entity.UserAccount;
import org.joda.time.Instant;
import com.mysema.query.sql.SQLQuery;

/**
 * Base class for handlers that require a valid account access token.
 */
public abstract class AbstractLoggedInHandler extends AbstractJsonApiHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.jsonapi.AbstractJsonApiHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.common.javascript.analyze.JsonAnalyzer, name.martingeisse.common.javascript.jsonbuilder.JsonValueBuilder)
	 */
	@Override
	protected final void handle(ApiRequestCycle requestCycle, JsonAnalyzer input, JsonValueBuilder<?> output) throws Exception {
		
		// find the presented token
		String presentedToken;
		if (!input.analyzeMapElement("accountAccessToken").isNull()) {
			presentedToken = input.analyzeMapElement("accountAccessToken").expectString();
		} else {
			presentedToken = null;
			for (Cookie cookie : requestCycle.getRequest().getCookies()) {
				if (cookie.getName().equals("accountAccessToken")) {
					presentedToken = cookie.getValue();
				}
			}
		}
		if (presentedToken == null) {
			throw new JsonApiException(1, "missing accountAccessToken");
		}
		
		// parse the token
		String username;
		try {
			String secret = MinerServerSecurityConstants.SECURITY_TOKEN_SECRET;
			username = SecurityTokenUtil.validateToken(presentedToken, new Instant(), secret);
		} catch (IllegalArgumentException e) {
			throw new JsonApiException(1, "invalid accountAccessToken: " + e.getMessage());
		}

		// load the user account
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		final QUserAccount qua = QUserAccount.userAccount;
		final UserAccount userAccount = query.from(qua).where(qua.username.eq(username)).singleResult(qua);
		
		// delegate to the subclass
		handle(requestCycle, input, output, userAccount);
		
	}
	
	/**
	 * The actual request handling, assuming the client has presented a valid
	 * account access token.
	 * 
	 * @param requestCycle the request cycle
	 * @param input the input data
	 * @param output the output builder
	 * @param userAccount the user's account
	 * @throws Exception on errors
	 */
	protected abstract void handle(ApiRequestCycle requestCycle, JsonAnalyzer input, JsonValueBuilder<?> output, UserAccount userAccount) throws Exception;
	
	
}
