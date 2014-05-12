/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;

/**
 * This class wraps an {@link IRequestMapper} and forwards all calls.
 * This allows to hot-replace the mapper.
 */
public class RequestMapperWrapper implements IRequestMapper {

	/**
	 * the wrappedMapper
	 */
	private IRequestMapper wrappedMapper;

	/**
	 * Constructor.
	 */
	public RequestMapperWrapper() {
	}

	/**
	 * Constructor.
	 * @param wrappedMapper the wrapped mapper
	 */
	public RequestMapperWrapper(final IRequestMapper wrappedMapper) {
		this.wrappedMapper = wrappedMapper;
	}

	/**
	 * Getter method for the wrappedMapper.
	 * @return the wrappedMapper
	 */
	public IRequestMapper getWrappedMapper() {
		return wrappedMapper;
	}

	/**
	 * Setter method for the wrappedMapper.
	 * @param wrappedMapper the wrappedMapper to set
	 */
	public void setWrappedMapper(final IRequestMapper wrappedMapper) {
		this.wrappedMapper = wrappedMapper;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#mapRequest(org.apache.wicket.request.Request)
	 */
	@Override
	public IRequestHandler mapRequest(final Request request) {
		return (wrappedMapper == null ? null : wrappedMapper.mapRequest(request));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#getCompatibilityScore(org.apache.wicket.request.Request)
	 */
	@Override
	public int getCompatibilityScore(final Request request) {
		return (wrappedMapper == null ? Integer.MIN_VALUE : wrappedMapper.getCompatibilityScore(request));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#mapHandler(org.apache.wicket.request.IRequestHandler)
	 */
	@Override
	public Url mapHandler(final IRequestHandler requestHandler) {
		return (wrappedMapper == null ? null : wrappedMapper.mapHandler(requestHandler));
	}

}
