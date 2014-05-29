/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.miner.startmenu;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.javascript.jsonbuilder.JsonBuilder;
import name.martingeisse.common.javascript.jsonbuilder.JsonObjectBuilder;
import name.martingeisse.httpclient.NullCookieStore;
import name.martingeisse.miner.common.Faction;
import name.martingeisse.miner.util.UserVisibleMessageException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

/**
 * This class wraps a {@link HttpClient} that fetches actual content from
 * the PHP server.
 */
public final class AccountApiClient {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AccountApiClient.class);

	/**
	 * the instance
	 */
	private static final AccountApiClient instance = new AccountApiClient();

	/**
	 * Getter method for the instance.
	 * @return the instance
	 */
	public static AccountApiClient getInstance() {
		return instance;
	}

	/**
	 * the httpClient
	 */
	private final HttpClient httpClient;

	/**
	 * the accountAccessToken
	 */
	private String accountAccessToken;

	/**
	 * the playerAccessToken
	 */
	private String playerAccessToken;

	/**
	 * Constructor.
	 */
	public AccountApiClient() {
		final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(20);
		this.httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultCookieStore(new NullCookieStore()).build();
	}
	
	/**
	 * 
	 */
	private JsonAnalyzer request(final String action, final String requestData) {
		try {
			final HttpPost post = new HttpPost("http://localhost:8888/" + action);
			post.setEntity(new StringEntity(requestData, StandardCharsets.UTF_8));
			final HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException(IOUtils.toString(response.getEntity().getContent(), "ascii"));
			}
			final JsonAnalyzer json = JsonAnalyzer.parse(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
			final int errorCode = json.analyzeMapElement("errorCode").expectInteger();
			if (errorCode != 0) {
				throw new UserVisibleMessageException("error (" + errorCode + "): " + json.analyzeMapElement("errorMessage").expectString());
			}
			return json.analyzeMapElement("data");
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 */
	private JsonObjectBuilder<String> createLoggedInRequest() {
		return new JsonBuilder().object().property("accountAccessToken").string(accountAccessToken);
	}

	/**
	 * 
	 */
	private JsonObjectBuilder<String> createPlayerRequest(final long playerId) {
		return createLoggedInRequest().property("playerId").number(playerId);
	}

	/**
	 * Sends username and password to the server, asking for an account access token.
	 * 
	 * @param username the username
	 * @param password the password
	 */
	public void login(final String username, final String password) {
		logger.debug("trying to log in as user " + username);
		final String request = new JsonBuilder().object().property("username").string(username).property("password").string(password).end();
		final JsonAnalyzer response = request("login", request);
		this.accountAccessToken = response.analyzeMapElement("accountAccessToken").expectString();
		logger.info("logged in as user " + username + ", account access token: " + accountAccessToken);
	}

	/**
	 * Fetches the list of players for the logged-in user.
	 * 
	 * @return the list of players
	 */
	public JsonAnalyzer fetchPlayers() {
		logger.debug("trying to fetch players");
		JsonAnalyzer json = request("getPlayers", createLoggedInRequest().end());
		logger.debug("players fetched");
		return json;
	}

	/**
	 * Creates a new player for the current user.
	 * 
	 * @param faction the player's faction
	 * @param name the player's name
	 * @return the player's ID
	 */
	public long createPlayer(final Faction faction, final String name) {
		logger.debug("creating player with faction " + faction + ", name " + name);
		final JsonObjectBuilder<String> builder = createLoggedInRequest();
		builder.property("faction").number(faction.ordinal());
		builder.property("name").string(name);
		final JsonAnalyzer response = request("createPlayer", builder.end());
		final long id = response.analyzeMapElement("id").expectLong();
		logger.info("created player with faction " + faction + ", name " + name + ": id " + id);
		return id;
	}

	/**
	 * Fetches detailed data for a single player.
	 * 
	 * @param playerId the player's ID
	 * @return the player data
	 */
	public JsonAnalyzer fetchPlayerDetails(final long playerId) {
		logger.debug("trying to fetch player details for player " + playerId);
		JsonAnalyzer json = request("getPlayerDetails", createPlayerRequest(playerId).end());
		logger.debug("fetched player details for player " + playerId);
		return json;
	}

	/**
	 * Obtains a player access token. The token is returned by {@link #getPlayerAccessToken()}.
	 * 
	 * @param playerId the player's ID
	 */
	public void accessPlayer(final long playerId) {
		logger.debug("trying to create player access token for player " + playerId);
		final JsonAnalyzer response = request("accessPlayer", createPlayerRequest(playerId).end());
		playerAccessToken = response.analyzeMapElement("playerAccessToken").expectString();
		logger.info("created player access token for player " + playerId + ": " + playerAccessToken);
	}

	/**
	 * Deletes a player character.
	 * 
	 * @param playerId the player's ID
	 */
	public void deletePlayer(final long playerId) {
		logger.debug("trying to delete player " + playerId);
		request("deletePlayer", createPlayerRequest(playerId).end());
		logger.info("player " + playerId + " deleted");
	}

	/**
	 * Getter method for the playerAccessToken.
	 * @return the playerAccessToken
	 */
	public String getPlayerAccessToken() {
		return playerAccessToken;
	}

}
