/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.papyros.backend;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.resource.JtwigResource;

/**
 * A {@link JtwigResource} that represents a Papyros template.
 */
public final class PapyrosTemplateJtwigResource implements JtwigResource {

	/**
	 * the content
	 */
	private final String content;

	/**
	 * Constructor.
	 * @param content the template content
	 */
	public PapyrosTemplateJtwigResource(String content) {
		this.content = content;
	}

	/* (non-Javadoc)
	 * @see com.lyncode.jtwig.resource.JtwigResource#retrieve()
	 */
	@Override
	public InputStream retrieve() throws ResourceException {
		return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
	}

	/* (non-Javadoc)
	 * @see com.lyncode.jtwig.resource.JtwigResource#resolve(java.lang.String)
	 */
	@Override
	public JtwigResource resolve(String relativePath) throws ResourceException {
		throw new ResourceException("resolving resources not implemented yet for PapyrosTemplateJtwigResource");
	}

}
