/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.template;

import name.martingeisse.papyros.backend.PapyrosDataUtil;
import name.martingeisse.papyros.backend.RenderTemplateAction;
import name.martingeisse.papyros.entity.QPreviewDataSet;
import name.martingeisse.papyros.entity.Template;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
import name.martingeisse.papyros.frontend.components.Iframe;
import name.martingeisse.sql.EntityConnectionManager;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.json.simple.JSONValue;

import com.mysema.query.sql.SQLQuery;

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
		final Template template = PapyrosDataUtil.loadTemplate(pageParameters);
		
		// TODO currently loads first preview data set
		Object previewData;
		{
			final QPreviewDataSet qpds = QPreviewDataSet.previewDataSet;
			final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
			String previewDataJson = query.from(qpds).where(qpds.templateFamilyId.eq(template.getTemplateFamilyId())).singleResult(qpds.data);
			previewData = JSONValue.parse(previewDataJson);
		}
		
		// fake rendering
		final RenderTemplateAction renderTemplateAction = new RenderTemplateAction();
		renderTemplateAction.setTemplate(template.getContent());
		renderTemplateAction.setData(previewData);
		final String renderedContent = renderTemplateAction.render();
		add(new Iframe("iframe", Model.of(renderedContent)));
		
	}
	
}
