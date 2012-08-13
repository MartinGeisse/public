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
package org.apache.wicket.extensions.markup.html.repeater.data.table.filter;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;


/**
 * Toolbar that creates a form to hold form components used to filter data in the data table. Form
 * components are provided by columns that implement IFilteredColumn.
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public class FilterToolbar extends AbstractToolbar
{
	private static final String FILTER_ID = "filter";
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param table
	 *            data table this toolbar will be added to
	 * @param form
	 *            the filter form
	 * @param stateLocator
	 *            locator responsible for finding object used to store filter's state
	 * @param <T>
	 *            type of filter state object
	 * 
	 */
	public <T> FilterToolbar(final DataTable<T> table, final FilterForm<T> form,
		final IFilterStateLocator<T> stateLocator)
	{
		super(table);

		if (table == null)
		{
			throw new IllegalArgumentException("argument [table] cannot be null");
		}
		if (stateLocator == null)
		{
			throw new IllegalArgumentException("argument [stateLocator] cannot be null");
		}

		// populate the toolbar with components provided by filtered columns

		RepeatingView filters = new RepeatingView("filters");
		filters.setRenderBodyOnly(true);
		add(filters);

		List<IColumn<T>> cols = table.getColumns();
		for (IColumn<T> col : cols)
		{
			WebMarkupContainer item = new WebMarkupContainer(filters.newChildId());
			item.setRenderBodyOnly(true);

			Component filter = null;

			if (col instanceof IFilteredColumn)
			{
				IFilteredColumn<?> filteredCol = (IFilteredColumn<?>)col;
				filter = filteredCol.getFilter(FILTER_ID, form);
			}

			if (filter == null)
			{
				filter = new NoFilter(FILTER_ID);
			}
			else
			{
				if (!filter.getId().equals(FILTER_ID))
				{
					throw new IllegalStateException(
						"filter component returned  with an invalid component id. invalid component id [" +
							filter.getId() +
							"] required component id [" +
							getId() +
							"] generating column [" + col.toString() + "] ");
				}
			}

			item.add(filter);

			filters.add(item);
		}

	}

	@Override
	protected void onBeforeRender()
	{
		if (findParent(FilterForm.class) == null)
		{
			throw new IllegalStateException("FilterToolbar must be contained within a Form");
		}
		super.onBeforeRender();
	}

}
