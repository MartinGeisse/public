/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.template;

import name.martingeisse.papyros.backend.PapyrosDataUtil;
import name.martingeisse.papyros.backend.RenderTemplateAction;
import name.martingeisse.papyros.entity.PreviewDataSet;
import name.martingeisse.papyros.entity.Template;
import name.martingeisse.papyros.entity.TemplateFamily;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
import name.martingeisse.papyros.frontend.components.Iframe;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.json.simple.JSONValue;

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
		final Pair<Template, TemplateFamily> pair = PapyrosDataUtil.loadTemplateAndTemplateFamily(pageParameters);
		final Template template = pair.getLeft();
		final TemplateFamily family = pair.getRight();
		add(new BookmarkablePageLink<>("templateLink", TemplatePage.class, new PageParameters().add("key", family.getKey()).add("language", template.getLanguageKey())));

		final PreviewDataSet previewDataSet = PapyrosDataUtil.loadPreviewDataSet(template.getTemplateFamilyId(), pageParameters);
		final Object previewData = (previewDataSet == null ? null : JSONValue.parse(previewDataSet.getData()));
		
		// fake rendering
		final RenderTemplateAction renderTemplateAction = new RenderTemplateAction();
		renderTemplateAction.setTemplate(template.getContent());
		renderTemplateAction.setData(previewData);
		final String renderedContent = renderTemplateAction.render();
		add(new Iframe("iframe", Model.of(renderedContent)));
		
	}
	
}
