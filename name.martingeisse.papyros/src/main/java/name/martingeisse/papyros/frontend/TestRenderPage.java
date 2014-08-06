/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend;

import name.martingeisse.papyros.entity.Template;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 */
public final class TestRenderPage extends AbstractFrontendPage {

	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public TestRenderPage(PageParameters pageParameters) {
		super(pageParameters);
		final Template template = FrontendDataUtil.loadTemplate(pageParameters);
		
		// fake rendering
		final String renderedContent = template.getContent();
		add(new Iframe("iframe", Model.of(renderedContent)));
		
	}
	
}
