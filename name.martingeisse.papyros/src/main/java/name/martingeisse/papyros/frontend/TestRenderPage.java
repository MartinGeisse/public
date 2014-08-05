/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend;

import name.martingeisse.papyros.entity.QTemplate;
import name.martingeisse.papyros.entity.QTemplateFamily;
import name.martingeisse.papyros.entity.Template;
import name.martingeisse.sql.EntityConnectionManager;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.mysema.query.sql.SQLQuery;

/**
 *
 */
public final class TestRenderPage extends WebPage {

	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public TestRenderPage(PageParameters pageParameters) {
		String key = pageParameters.get("key").toString("");
		String language = pageParameters.get("language").toString("");
		final QTemplate qt = QTemplate.template;
		final QTemplateFamily qtf = QTemplateFamily.templateFamily;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		final Object[] result = query.from(qt, qtf).where(qt.templateFamilyId.eq(qtf.id), qtf.key.eq(key), qt.languageKey.eq(language)).singleResult(qt, qtf);
		if (result == null) {
			throw new RuntimeException("template not found; key='" + key + "'; language='" + language + "'");
		}
		final Template template = (Template)result[0];
		// final TemplateFamily templateFamily = (TemplateFamily)result[1];
		
		// fake rendering
		final String renderedContent = template.getContent();
		add(new Label("iframeContent", renderedContent));
		
	}
	
}
