/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.slave_services.papyros.frontend.components;

import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.slave_services.common.frontend.components.Iframe;
import name.martingeisse.slave_services.entity.TemplatePreviewDataSet;
import name.martingeisse.slave_services.papyros.backend.RenderTemplateAction;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.json.simple.JSONValue;

/**
 * Implements an iframe that shows a preview for a template and data set.
 */
public class PreviewTemplateIframe extends Iframe {

	/**
	 * the templateContentModel
	 */
	private final IModel<String> templateContentModel;

	/**
	 * the templatePreviewDataSetModel
	 */
	private final IModel<TemplatePreviewDataSet> templatePreviewDataSetModel;

	/**
	 * the renderedPreview
	 */
	private String renderedPreview;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param templateContentModel the model for the template content
	 * @param templatePreviewDataSetModel the model for the preview data set
	 */
	public PreviewTemplateIframe(String id, final IModel<String> templateContentModel, final IModel<TemplatePreviewDataSet> templatePreviewDataSetModel) {
		super(id);
		setDefaultModel(new PropertyModel<>(this, "renderedPreview"));
		this.templateContentModel = ParameterUtil.ensureNotNull(templateContentModel, "templateContentModel");
		this.templatePreviewDataSetModel = ParameterUtil.ensureNotNull(templatePreviewDataSetModel, "templatePreviewDataSetModel");
		setOutputMarkupId(true);
	}

	/**
	 * 
	 */
	private void renderPreview() {
		final String templateContent = templateContentModel.getObject();
		final TemplatePreviewDataSet previewDataSet = templatePreviewDataSetModel.getObject();
		final Object parsedPreviewData = (previewDataSet == null ? null : JSONValue.parse(previewDataSet.getData()));
		final RenderTemplateAction renderTemplateAction = new RenderTemplateAction();
		renderTemplateAction.setTemplate(templateContent == null ? "" : templateContent);
		renderTemplateAction.setData(parsedPreviewData);
		renderedPreview = renderTemplateAction.render();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		renderPreview();
		super.onBeforeRender();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.slave_services.common.frontend.components.Iframe#renderReloadScript(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	public void renderReloadScript(AjaxRequestTarget target) {
		renderPreview();
		super.renderReloadScript(target);
	}
	
	/**
	 * Getter method for the renderedPreview.
	 * @return the renderedPreview
	 */
	public String getRenderedPreview() {
		return renderedPreview;
	}
	
}
