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
package org.apache.wicket.markup.html;

import org.apache.wicket.Component;
import org.apache.wicket.IClusterable;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.behavior.Behavior;

/**
 * An interface to be implemented by {@link Behavior}s or {@link IAjaxCallDecorator}s that wish to
 * contribute to the header section of the page.
 * 
 * Example:
 * 
 * <pre>
 * class MyAjaxCallDecorator implements IAjaxCallDecorator, IHeaderContributor
 * {
 * 
 * 	public void renderHead(Component component, IHeaderResponse response)
 * 	{
 * 		response.renderOnLoadJavaScript(&quot;alert('page loaded!');&quot;);
 * 	}
 * }
 * </pre>
 */
public interface IComponentAwareHeaderContributor extends IClusterable
{
	/**
	 * Render to the web response whatever the component-aware wants to contribute to the head
	 * section.
	 * 
	 * @param component
	 *            component which is contributing to the response. This parameter is here to give
	 *            the component as the context for component-awares implementing this interface
	 * 
	 * @param response
	 *            Response object
	 */
	void renderHead(Component component, IHeaderResponse response);
}
