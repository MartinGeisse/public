/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.application.wicket;

import java.util.concurrent.ConcurrentHashMap;

import name.martingeisse.guiserver.configuration.multiverse.HyperspaceConfiguration;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

/**
 * Maps a request to a universe request mapper based on its "Host" header, using the
 * hyperspace configuration to map the header to a universe ID.
 */
public final class MultiverseRequestMapper implements IRequestMapper {
	
	/**
	 * the mappers
	 */
	private final ConcurrentHashMap<String, IRequestMapper> mappers = new ConcurrentHashMap<>();
	
	/**
	 * the hyperspaceConfiguration
	 */
	private HyperspaceConfiguration hyperspaceConfiguration;

	/**
	 * Constructor.
	 */
	public MultiverseRequestMapper() {
	}

	/**
	 * Sets the request mapper for a universe.
	 * @param universeId the universe ID
	 * @param universeMapper the mapper for this universe
	 */
	public void setUniverseMapper(String universeId, IRequestMapper universeMapper) {
		mappers.put(universeId, universeMapper);
	}
	
	/**
	 * Getter method for the hyperspaceConfiguration.
	 * @return the hyperspaceConfiguration
	 */
	public HyperspaceConfiguration getHyperspaceConfiguration() {
		return hyperspaceConfiguration;
	}
	
	/**
	 * Setter method for the hyperspaceConfiguration.
	 * @param hyperspaceConfiguration the hyperspaceConfiguration to set
	 */
	public void setHyperspaceConfiguration(HyperspaceConfiguration hyperspaceConfiguration) {
		this.hyperspaceConfiguration = hyperspaceConfiguration;
	}

	/**
	 * 
	 */
	private IRequestMapper getUniverseMapperForRequest(Request request) {
		if (hyperspaceConfiguration == null) {
			throw new RuntimeException("cannot map request -- no hyperspace configuration");
		}
		if (request instanceof WebRequest) {
			WebRequest webRequest = (WebRequest)request;
			String host = webRequest.getHeader("host");
			if (host != null) {
				String universeId = hyperspaceConfiguration.mapHostToUniverseId(host);
				if (universeId != null) {
					return mappers.get(universeId);
				}
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#mapRequest(org.apache.wicket.request.Request)
	 */
	@Override
	public IRequestHandler mapRequest(Request request) {
		IRequestMapper universeRequestMapper = getUniverseMapperForRequest(request);
		return (universeRequestMapper == null ? null : universeRequestMapper.mapRequest(request));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#getCompatibilityScore(org.apache.wicket.request.Request)
	 */
	@Override
	public int getCompatibilityScore(Request request) {
		IRequestMapper universeRequestMapper = getUniverseMapperForRequest(request);
		return (universeRequestMapper == null ? Integer.MIN_VALUE : universeRequestMapper.getCompatibilityScore(request));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#mapHandler(org.apache.wicket.request.IRequestHandler)
	 */
	@Override
	public Url mapHandler(IRequestHandler requestHandler) {
		Request request = RequestCycle.get().getRequest();
		IRequestMapper universeRequestMapper = getUniverseMapperForRequest(request);
		return (universeRequestMapper == null ? null : universeRequestMapper.mapHandler(requestHandler));
	}

}
