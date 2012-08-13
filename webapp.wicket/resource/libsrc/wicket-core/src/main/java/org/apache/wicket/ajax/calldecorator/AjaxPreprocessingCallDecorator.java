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
package org.apache.wicket.ajax.calldecorator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;

/**
 * Ajax call decorator that decorates script before passing it to the delegate decorator
 * 
 * @see IAjaxCallDecorator for notes on escaping quotes in scripts
 * 
 * @since 1.2
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public class AjaxPreprocessingCallDecorator implements IAjaxCallDecorator, IAjaxCallDecoratorDelegate
{
	private static final long serialVersionUID = 1L;

	private final IAjaxCallDecorator delegate;

	/**
	 * Construct.
	 * 
	 * @param delegate
	 */
	public AjaxPreprocessingCallDecorator(IAjaxCallDecorator delegate)
	{
		this.delegate = delegate;
	}


	public CharSequence decorateScript(Component c, CharSequence script)
	{
		CharSequence s = preDecorateScript(script);
		return (delegate == null) ? s : delegate.decorateScript(c, s);

	}

	public CharSequence decorateOnSuccessScript(Component c, CharSequence script)
	{
		CharSequence s = preDecorateOnSuccessScript(script);
		return (delegate == null) ? s : delegate.decorateOnSuccessScript(c, s);
	}

	public CharSequence decorateOnFailureScript(Component c, CharSequence script)
	{
		CharSequence s = preDecorateOnFailureScript(script);

		return (delegate == null) ? s : delegate.decorateOnFailureScript(c, s);
	}


	/**
	 * Decorates ajax call script
	 * 
	 * @param script
	 * @return decorated script
	 */
	public CharSequence preDecorateScript(CharSequence script)
	{
		return script;
	}

	/**
	 * Decorates success handling script
	 * 
	 * @param script
	 * @return decorated script
	 */
	public CharSequence preDecorateOnSuccessScript(CharSequence script)
	{
		return script;
	}

	/**
	 * Decorates the failure handling script
	 * 
	 * @param script
	 * @return decorated script
	 */
	public CharSequence preDecorateOnFailureScript(CharSequence script)
	{
		return script;
	}


	public IAjaxCallDecorator getDelegate()
	{
		return delegate;
	}
}
