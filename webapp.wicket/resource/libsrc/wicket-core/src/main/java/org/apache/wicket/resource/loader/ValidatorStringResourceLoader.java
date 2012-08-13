/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.resource.loader;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidatorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is one of Wicket's default string resource loaders.
 * <p>
 * The validator string resource loader checks resource bundles attached to validators (eg
 * MinimumValidator.properties). The validator list is pulled from the form component in error.
 * <p>
 * This implementation is fully aware of both locale and style values when trying to obtain the
 * appropriate resources.
 * <p>
 * 
 * @author igor.vaynberg
 */
public class ValidatorStringResourceLoader extends ComponentStringResourceLoader
{
	private static final Logger log = LoggerFactory.getLogger(ValidatorStringResourceLoader.class);

	/**
	 * Create and initialize the resource loader.
	 */
	public ValidatorStringResourceLoader()
	{
	}

	/**
	 * @see org.apache.wicket.resource.loader.ComponentStringResourceLoader#loadStringResource(java.lang.Class,
	 *      java.lang.String, java.util.Locale, java.lang.String, java.lang.String)
	 */
	@Override
	public String loadStringResource(Class<?> clazz, final String key, final Locale locale,
		final String style, final String variation)
	{
		// only care about IValidator subclasses
		if (clazz == null || !IValidator.class.isAssignableFrom(clazz))
		{
			return null;
		}

		return super.loadStringResource(clazz, key, locale, style, variation);
	}

	/**
	 * @see org.apache.wicket.resource.loader.ComponentStringResourceLoader#loadStringResource(org.apache.wicket.Component,
	 *      java.lang.String, java.util.Locale, java.lang.String, java.lang.String)
	 */
	@Override
	public String loadStringResource(final Component component, final String key,
		final Locale locale, final String style, final String variation)
	{
		if (component == null || !(component instanceof FormComponent))
		{
			return null;
		}

		FormComponent<?> fc = (FormComponent<?>)component;
		for (IValidator<?> validator : fc.getValidators())
		{
			Class<?> scope = getScope(validator);
			String resource = loadStringResource(scope, key, locale, style,
				variation);
			if (resource != null)
			{
				return resource;
			}
		}

		// not found
		return null;
	}

	private Class<? extends IValidator> getScope(IValidator<?> validator)
	{
		Class<? extends IValidator> scope;
		if (validator instanceof ValidatorAdapter)
		{
			scope = ((ValidatorAdapter) validator).getValidator().getClass();
		}
		else
		{
			scope = validator.getClass();
		}
		return scope;
	}
}
