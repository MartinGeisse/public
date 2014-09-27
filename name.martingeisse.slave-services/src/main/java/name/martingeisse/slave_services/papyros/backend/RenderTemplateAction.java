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

	/**
	 * Renders this template.
	 * @return the render result
	 * @throws ParseException on Jtwig parse errors
	 * @throws CompileException on Jtwig compile errors
	 * @throws RenderException on Jtwig render errors
	 */
	/*
	public String render() throws ParseException, CompileException, RenderException {
		JtwigResource templateResource = new PapyrosTemplateJtwigResource(template);
		JtwigConfiguration configuration = new JtwigConfiguration();
		// TODO uses the platform's default encoding!
        JtwigTemplate template = new JtwigTemplate(templateResource, configuration);
        JtwigModelMap modelMap = new JtwigModelMap();
        modelMap.add("data", data);
        ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();
       	template.output(resultOutputStream, modelMap);
       	return new String(resultOutputStream.toByteArray(), StandardCharsets.UTF_8);
	}
	*/

}
