/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.api.account;

import javax.servlet.http.Cookie;

import name.martingeisse.api.handler.jsonapi.AbstractJsonApiHandler;
import name.martingeisse.api.handler.jsonapi.JsonApiException;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.javascript.jsonbuilder.JsonValueBuilder;
import name.martingeisse.common.security.SecurityTokenUtil;
import name.martingeisse.miner.server.MinerServerSecurityConstants;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.webide.entity.QUserAccount;
import name.martingeisse.webide.entity.UserAccount;

import org.joda.time.Instant;

import com.mysema.query.sql.SQLQuery;

import external.BCrypt;

/**
 * Handles "login" requests that exchange a username/password for an account access token.
 * The account access token is returned as a field in the response (for the actual API
 * client) and also as a Set-Cookie header (for testing in the browser). Downstream API
 * actions will accept the token either as a request field or as a cookie.
 */
public final class LoginHandler extends AbstractJsonApiHandler {

	/**
	 * the DUMMY_PASSWORD_HASH
	 */
	private static final String DUMMY_PASSWORD_HASH = "$2a$12$xxyZikdRn7HxYXlOqOvaXOLmtpaXoLxFjPDQpiSYZSZNxbzeV68Xy";
	
	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.jsonapi.AbstractJsonApiHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.common.javascript.analyze.JsonAnalyzer, name.martingeisse.common.javascript.jsonbuilder.JsonValueBuilder)
	 */
	@Override
	protected void handle(RequestCycle requestCycle, JsonAnalyzer input, JsonValueBuilder<?> output) throws Exception {
		
		// fetch the user record
		String username = input.analyzeMapElement("username").expectString();
		UserAccount userAccount;
		{
			QUserAccount qua = QUserAccount.userAccount;
			final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
			userAccount = query.from(qua).where(qua.username.eq(username)).singleResult(qua);
		}

		// Check the password. Even if we found no user account we check against a pre-generated
		// dummy hash to produce similar timing, to prevent timing attacks.
		String password = input.analyzeMapElement("password").expectString();
		if (!BCrypt.checkpw(password, userAccount == null ? DUMMY_PASSWORD_HASH : userAccount.getPasswordHash())) {
			throw new JsonApiException(1, "Invalid username or pasword");
		}
		
		// build the access token and add it to the response body and to the cookie header
		Instant expiryTime = new Instant().plus(MinerServerSecurityConstants.ACCOUNT_ACCESS_TOKEN_MAX_AGE_MILLISECONDS);
		String token = SecurityTokenUtil.createToken(username, expiryTime, MinerServerSecurityConstants.SECURITY_TOKEN_SECRET);
		output.object().property("accountAccessToken").string(token).end();
		requestCycle.getResponse().addCookie(buildCookie(token));
		
	}

	/**
	 * 
	 */
	private static Cookie buildCookie(String token) {
		Cookie cookie = new Cookie("accountAccessToken", token);
		cookie.setMaxAge(MinerServerSecurityConstants.ACCOUNT_ACCESS_TOKEN_MAX_AGE_SECONDS);
		cookie.setPath("/");
		return cookie;
	}
	
}
