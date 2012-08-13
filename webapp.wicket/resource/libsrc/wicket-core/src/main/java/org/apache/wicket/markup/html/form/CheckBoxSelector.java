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
package org.apache.wicket.markup.html.form;

import java.util.Arrays;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;


/**
 * A Javascript-based "Select All" checkbox component that works with a loose collection of
 * {@link CheckBox} components. By default, clicking on any of the controlled checkboxes
 * automatically updates the state of the "select all" checkbox. Override
 * {@link AbstractCheckSelector#wantAutomaticUpdate()} to change this.
 * 
 * @author Carl-Eric Menzel <cmenzel@wicketbuch.de>
 */
public class CheckBoxSelector extends AbstractCheckSelector
{
	private static final long serialVersionUID = 1L;

	private final static ResourceReference JS = new PackageResourceReference(
		CheckBoxSelector.class, "CheckBoxSelector.js");

	/**
	 * Javascript array literal containing the markup IDs of the checkboxes we want to
	 * check/uncheck. Example: "['foo', 'bar', 'baz']". Generated by
	 * JavascriptUtils.buildMarkupIdJSArrayLiteral().
	 */
	private final String checkBoxIdArrayLiteral;

	/**
	 * @param id
	 *            The component ID
	 * @param boxes
	 *            The CheckBoxes this selector will control.
	 */
	public CheckBoxSelector(String id, CheckBox... boxes)
	{
		super(id);
		setOutputMarkupId(true);
		checkBoxIdArrayLiteral = buildMarkupIdJSArrayLiteral(Arrays.asList(boxes));
	}

	@Override
	protected CharSequence getFindCheckboxesFunction()
	{
		return "Wicket.CheckboxSelector.Checkboxes.findCheckboxesFunction(" +
			checkBoxIdArrayLiteral + ")";
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
		response.renderJavaScriptReference(JS);
	}


	/**
	 * Builds a JavaScript array literal containing the markup IDs of the given components. Example:
	 * "['foo', 'bar', 'baz']".
	 * 
	 * @param components
	 *            The components whose IDs we need
	 * @return a properly formatted JS array literal
	 */
	private String buildMarkupIdJSArrayLiteral(final Iterable<? extends Component> components)
	{
		StringBuilder buf = new StringBuilder();
		buf.append("[");
		if (components.iterator().hasNext())
		{
			for (Component component : components)
			{
				component.setOutputMarkupId(true);
				buf.append("'").append(component.getMarkupId()).append("', ");
			}
			buf.delete(buf.length() - 2, buf.length());
		}
		buf.append("]");
		return buf.toString();
	}

}
