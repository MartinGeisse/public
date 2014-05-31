/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.codegen.template;

import java.io.IOException;
import java.util.Map;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * A simple directive that does nothing and discards its body.
 */
public class NopDirective implements TemplateDirectiveModel {

	/**
	 * The shared instance of this class.
	 */
	public static final NopDirective instance = new NopDirective();
	
	/* (non-Javadoc)
	 * @see freemarker.template.TemplateDirectiveModel#execute(freemarker.core.Environment, java.util.Map, freemarker.template.TemplateModel[], freemarker.template.TemplateDirectiveBody)
	 */
	@Override
	public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVariables, TemplateDirectiveBody body) throws TemplateException, IOException {
	}
	
}
