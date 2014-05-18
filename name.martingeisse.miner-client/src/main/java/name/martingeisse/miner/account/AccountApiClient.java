/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.miner.account;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.javascript.jsonbuilder.JsonBuilder;
import name.martingeisse.common.javascript.jsonbuilder.JsonObjectBuilder;
import name.martingeisse.miner.common.Faction;
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
	private JsonAnalyzer request(String action, String requestData) {
		try {
			HttpPost post = new HttpPost("http://localhost:8888/" + action);
			post.setEntity(new StringEntity(requestData, StandardCharsets.UTF_8));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException(IOUtils.toString(response.getEntity().getContent(), "ascii"));
			}
			JsonAnalyzer json = JsonAnalyzer.parse(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
			int errorCode = json.analyzeMapElement("errorCode").expectInteger();
			if (errorCode != 0) {
				throw new UserVisibleMessageException("error (" + errorCode + "): " + json.analyzeMapElement("errorMessage").expectString());
			}
			return json.analyzeMapElement("data");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
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
	private JsonObjectBuilder<String> createPlayerRequest(long playerId) {
		return createLoggedInRequest().property("playerId").number(playerId);
	}
	
	/**
	 * Sends username and password to the server, asking for an account access token.
	 * 
	 * @param username the username
	 * @param password the password
	 */
	public void login(String username, String password) {
		String request = new JsonBuilder().object().property("username").string(username).property("password").string(password).end();
		JsonAnalyzer response = request("login", request);
		this.accountAccessToken = response.analyzeMapElement("accountAccessToken").expectString();
		System.out.println("obtained token: " + accountAccessToken);
	}

	/**
	 * Fetches the list of players for the logged-in user.
	 * 
	 * @return the list of players
	 */
	public JsonAnalyzer fetchPlayers() {
		return request("getPlayers", createLoggedInRequest().end());
	}
	
	/**
	 * Creates a new player for the current user.
	 * 
	 * @param faction the player's faction
	 * @param name the player's name
	 * @return the player's ID
	 */
	public long createPlayer(Faction faction, String name) {
		JsonObjectBuilder<String> builder = createLoggedInRequest();
		builder.property("faction").number(faction.ordinal());
		builder.property("name").string(name);
		JsonAnalyzer response = request("createPlayer", builder.end());
		return response.analyzeMapElement("id").expectLong();
	}

	/**
	 * Fetches detailed data for a single player.
	 * 
	 * @param playerId the player's ID
	 * @return the player data
	 */
	public JsonAnalyzer fetchPlayerDetails(long playerId) {
		return request("getPlayerDetails", createPlayerRequest(playerId).end());
	}
	
	/**
	 * Obtains a player access token.
	 * 
	 * @param playerId the player's ID
	 * @return the access token
	 */
	public String getPlayerAccessToken(long playerId) {
		JsonAnalyzer response = request("accessPlayer", createPlayerRequest(playerId).end());
		return response.analyzeMapElement("playerAccessToken").expectString();
	}

	/**
	 * Deletes a player character.
	 * 
	 * @param playerId the player's ID
	 */
	public void deletePlayer(long playerId) {
		request("deletePlayer", createPlayerRequest(playerId).end());
	}

}
