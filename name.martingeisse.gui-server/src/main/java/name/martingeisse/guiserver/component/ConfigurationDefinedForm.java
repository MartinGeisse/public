/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.component;

import java.util.Map;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.guiserver.backend.BackendHttpClient;
import name.martingeisse.guiserver.configuration.Configuration;
import name.martingeisse.guiserver.configuration.element.xml.PageConfiguration;
import name.martingeisse.guiserver.template.basic.form.FormConfiguration;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * A form that is defined by a {@link FormConfiguration}.
 */
public final class ConfigurationDefinedForm extends Form<Map<String, Object>> {

	/**
	 * the formConfigurationHandle
	 */
	private final int formConfigurationHandle;
	
	/**
	 * the cachedFormConfiguration
	 */
	private transient FormConfiguration cachedFormConfiguration;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param formConfiguration the configuration for this form
	 * @param model the form data model
	 */
	public ConfigurationDefinedForm(String id, FormConfiguration formConfiguration, IModel<Map<String, Object>> model) {
		super(id, model);
		this.formConfigurationHandle = formConfiguration.getSnippetHandle();
		this.cachedFormConfiguration = formConfiguration;
	}

	/**
	 * Getter method for the form configuration.
	 * @return the form configuration.
	 */
	public final FormConfiguration getFormConfiguration() {
		if (cachedFormConfiguration == null) {
			cachedFormConfiguration = (FormConfiguration)Configuration.getInstance().getSnippet(formConfigurationHandle);
		}
		return cachedFormConfiguration;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
	 */
	@Override
	protected void onSubmit() {
		JsonAnalyzer response = BackendHttpClient.postParametersForJson(getFormConfiguration().getBackendUrl(), getModelObject());
		JsonAnalyzer validation = response.analyzeMapElement("validation");
		if (!validation.isNull()) {
			for (Map.Entry<String, JsonAnalyzer> validationEntry : validation.analyzeMap().entrySet()) {
				final String field = validationEntry.getKey();
				final String message = validationEntry.getValue().expectString();
				visitFormComponents(new IVisitor<FormComponent<?>, Void>() {
					@Override
					public void component(FormComponent<?> formComponent, IVisit<Void> visit) {
						FieldPathBehavior fieldPathBehavior = null;
						for (FieldPathBehavior fieldPathBehavior2 : formComponent.getBehaviors(FieldPathBehavior.class)) {
							fieldPathBehavior = fieldPathBehavior2;
						}
						if (fieldPathBehavior != null && fieldPathBehavior.getPath().equals(field)) {
							formComponent.error(message);
						}
					}
				});
			}
		}
		String redirectPagePath = response.analyzeMapElement("redirectPage").tryString();
		if (redirectPagePath != null) {
			PageParameters targetPageParameters = new PageParameters();
			targetPageParameters.add(PageConfiguration.CONFIGURATION_ELEMENT_PATH_PAGE_PARAMETER_NAME, redirectPagePath);
			setResponsePage(ConfigurationDefinedPage.class, targetPageParameters);
		}
		String redirectUrl = response.analyzeMapElement("redirectUrl").tryString();
		if (redirectUrl != null) {
			getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(redirectUrl));
		}
	}
	
}
