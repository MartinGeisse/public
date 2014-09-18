/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.slave_services.papyros.backend;

import org.apache.wicket.core.util.string.interpolator.PropertyVariableInterpolator;

/**
 * This class contains the functionality to actually render a template. It is
 * independent from the database.
 */
public class RenderTemplateAction {

	/**
	 * the template
	 */
	private String template;
	
	/**
	 * the data
	 */
	private Object data;

	/**
	 * Getter method for the template.
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Setter method for the template.
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Getter method for the data.
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Setter method for the data.
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * Renders this template.
	 * @return the render result
	 */
	public String render() {
		return new PropertyVariableInterpolator(template, data).toString();
	}
	
}
