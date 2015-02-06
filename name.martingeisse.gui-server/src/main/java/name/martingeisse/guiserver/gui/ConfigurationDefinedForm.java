/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui;

import java.util.Map;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.guiserver.backend.BackendHttpClient;
import name.martingeisse.guiserver.configuration.Configuration;
import name.martingeisse.guiserver.configuration.content.FormConfiguration;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

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
		this.formConfigurationHandle = formConfiguration.getConfigurationHandle();
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
		String redirectPagePath = response.analyzeMapElement("redirectPage").tryString();
		if (redirectPagePath != null) {
			System.out.println("* redirect to page: " + redirectPagePath);
			// TODO redirect
		}
		String redirectUrl = response.analyzeMapElement("redirectUrl").tryString();
		if (redirectUrl != null) {
			// TODO redirect
		}
		System.out.println("* done");
	}
	
}
