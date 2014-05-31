/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.codegen.template;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Base class for templates that produce Java source files.
 * To build an actual template, create a subclass of this
 * class and write an associated ".ftl" file.
 */
public abstract class JavaFileTemplate {

	/**
	 * the className
	 */
	private String className;
	
	/**
	 * the packageName
	 */
	private String packageName;
	
	/**
	 * Constructor.
	 */
	public JavaFileTemplate() {
	}

	/**
	 * Getter method for the className.
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * Setter method for the className.
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * Getter method for the packageName.
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	
	/**
	 * Setter method for the packageName.
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * Renders this template to the specified writer, building
	 * a data model from the values stored in this object.
	 * 
	 * @param context the template context
	 * @param w the writer to write to
	 * @throws IOException on I/O errors
	 * @throws TemplateException on errors in the template
	 */
	public void renderToWriter(JavaTemplateContext context, Writer w) throws IOException, TemplateException {
		Map<String, Object> dataModel = new HashMap<String, Object>();
		buildDataModel(dataModel);
		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(getClass(), "");
		configuration.setObjectWrapper(new DefaultObjectWrapper());
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		Template template = configuration.getTemplate(getClass().getSimpleName() + ".ftl");
		template.process(dataModel, w);
	}
	
	/**
	 * Renders this template to a Java source file in the source
	 * path specified in the context, building a data model from
	 * the values stored in this object.
	 * 
	 * @param context the template context
	 * @throws IOException on I/O errors
	 * @throws TemplateException on errors in the template
	 */
	public void renderToSourcePath(JavaTemplateContext context) throws IOException, TemplateException {
		try (PrintWriter w = context.openTextFile(packageName, className + ".java")) {
			renderToWriter(context, w);
		}
	}
	
	/**
	 * Inserts data model values into the specified map. This method
	 * gets called by the template system.
	 * 
	 * The default implementation sets the variables "className" and "packageName"
	 * according to the values set in this class.
	 * 
	 * @param dataModel the data model map
	 */
	protected void buildDataModel(Map<String, Object> dataModel) {
		dataModel.put("className", className);
		dataModel.put("packageName", packageName);
	}
	
}
