/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.codegen.dataclass;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import name.martingeisse.codegen.template.JavaFileTemplate;

import org.apache.commons.lang3.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateScalarModel;

/**
 * Base class for templates that produce "data-holding classes".
 * These classes some on families of (readable, immutable base,
 * immutable, and mutable). A single template class is used to
 * produce all these classes.
 */
public abstract class DataClassTemplate extends JavaFileTemplate {

	/**
	 * the nameKernel
	 */
	private final String nameKernel;
	
	/**
	 * the dataClassType
	 */
	private final DataClassType dataClassType;

	/**
	 * Constructor.
	 * @param nameKernel the name kernel to which the data class type
	 * prefix is added to build the actual class name
	 * @param dataClassType selects mutability and similar properties
	 */
	public DataClassTemplate(String nameKernel, DataClassType dataClassType) {
		this.nameKernel = nameKernel;
		this.dataClassType = dataClassType;
		setClassName(dataClassType.getClassPrefix() + nameKernel);
	}

	@Override
	protected void buildDataModel(Map<String, Object> dataModel) {
		super.buildDataModel(dataModel);
		dataModel.put("classExtensibility", dataClassType.getClassExtensibility());
		dataModel.put("hasFields", dataClassType.hasFields());
		dataModel.put("hasFinalFields", dataClassType.hasFinalFields());
		dataModel.put("fieldFinal", dataClassType.hasFinalFields() ? "final " : "");
		dataModel.put("hasInitializingConstructor", dataClassType.hasInitializingConstructor());
		dataModel.put("hasDelegatingConstructor", dataClassType.hasDelegatingConstructor());
		dataModel.put("hasAbstractGetters", dataClassType.hasAbstractGetters());
		dataModel.put("hasConcreteGetters", dataClassType.hasConcreteGetters());
		dataModel.put("hasSetters", dataClassType.hasSetters());
		DataClassType superclassDataClassType = dataClassType.getSuperclassType();
		if (superclassDataClassType == null) {
			dataModel.put("extendsClause", "");
		} else {
			dataModel.put("extendsClause", " extends " + superclassDataClassType.getClassPrefix() + nameKernel);
		}
		dataModel.put("field", new TemplateDirectiveModel() {
			@Override
			public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVariables, TemplateDirectiveBody body) throws TemplateException, IOException {
				if (!((TemplateBooleanModel)environment.getDataModel().get("hasFields")).getAsBoolean()) {
					return;
				}
				String name = ((TemplateScalarModel)parameters.get("name")).getAsString();
				String type = ((TemplateScalarModel)parameters.get("type")).getAsString();
				String fieldFinal = ((TemplateScalarModel)environment.getDataModel().get("fieldFinal")).getAsString();
				PrintWriter out = new PrintWriter(environment.getOut());
				out.println();
				out.println("	/**");
				out.println("	 * the " + name);
				out.println("	 */");
				out.println("	public " + fieldFinal + type + ' ' + name + ';');
			}
		});
		dataModel.put("accessors", new TemplateDirectiveModel() {
			@Override
			public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVariables, TemplateDirectiveBody body) throws TemplateException, IOException {
				String name = ((TemplateScalarModel)parameters.get("name")).getAsString();
				String capitalizedName = StringUtils.capitalize(name);
				String type = ((TemplateScalarModel)parameters.get("type")).getAsString();
				PrintWriter out = new PrintWriter(environment.getOut());
				boolean hasAbstractGetters = ((TemplateBooleanModel)environment.getDataModel().get("hasAbstractGetters")).getAsBoolean();
				boolean hasConcreteGetters = ((TemplateBooleanModel)environment.getDataModel().get("hasConcreteGetters")).getAsBoolean();
				boolean hasSetters = ((TemplateBooleanModel)environment.getDataModel().get("hasSetters")).getAsBoolean();
				if (hasAbstractGetters || hasConcreteGetters) {
					out.println();
					out.println("	/**");
					out.println("	 * Getter method for the " + name + ".");
					out.println("	 * @return the " + name);
					out.println("	 */");
					if (hasConcreteGetters) {
						out.println("	@Override");
					}
					out.println("	public " + (hasAbstractGetters ? "abstract " : "") + type + " get" + capitalizedName + (hasAbstractGetters ? "();" : "() {"));
					if (hasConcreteGetters) {
						out.println("		return " + name + ";");
						out.println("	}");
					}
				}
				if (hasSetters) {
					out.println();
					out.println("	/**");
					out.println("	 * Setter method for the " + name + ".");
					out.println("	 * @param " + name + " the " + name);
					out.println("	 */");
					out.println("	public void set" + capitalizedName + "(" + type + ' ' + name + ") {");
					out.println("		this." + name + " = " + name + ";");
					out.println("	}");
				}
			}
		});
	}

}
