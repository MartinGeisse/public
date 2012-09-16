/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.terms.IGetDisplayNameAware;
import name.martingeisse.wicket.util.DisplayNameEnumChoiceRenderer;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/**
 * A simple panel that wraps a {@link DropDownChoice}.
 * @param <T> the model type
 */
public class DropDownChoicePanel<T> extends AbstractSimpleFormComponentPanel<T, DropDownChoice<T>> {

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public DropDownChoicePanel(String id) {
		super(id);
		add(new DropDownChoice<T>(FORM_COMPONENT_ID));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param dropDownChoice the drop-down choice to use. Must use the value of {#FORM_COMPONENT_ID} as its Wicket id.
	 */
	public DropDownChoicePanel(String id, DropDownChoice<T> dropDownChoice) {
		super(id);
		add(dropDownChoice);
	}
	
	/**
	 * Creates an instance for an enum type.
	 * @param <T> the enum type
	 * @param id the wicket id
	 * @param model the model
	 * @param enumClass the class object for the enum type
	 * @return the created instance
	 */
	public static <T extends Enum<T>> DropDownChoicePanel<T> createForRawEnum(String id, IModel<T> model, Class<T> enumClass) {
		DropDownChoicePanel<T> panel = new DropDownChoicePanel<T>(id);
		panel.getFormComponent().setModel(model);
		panel.getFormComponent().setChoices(getEnumElementsAsList(enumClass));
		return panel;
	}

	/**
	 * Creates an instance for an enum type that implements {@link IGetDisplayNameAware}.
	 * @param <T> the enum type
	 * @param id the wicket id
	 * @param model the model
	 * @param enumClass the class object for the enum type
	 * @return the created instance
	 */
	public static <T extends Enum<T> & IGetDisplayNameAware> DropDownChoicePanel<T> createForDisplayNameEnum(String id, IModel<T> model, Class<T> enumClass) {
		DropDownChoicePanel<T> panel = new DropDownChoicePanel<T>(id);
		panel.getFormComponent().setModel(model);
		panel.getFormComponent().setChoices(getEnumElementsAsList(enumClass));
		panel.getFormComponent().setChoiceRenderer(new DisplayNameEnumChoiceRenderer<T>());
		return panel;
	}

	/**
	 * Returns all elements of an enum type as a list.
	 * @param <T> the enum type
	 * @param enumClass the class object of the enum type
	 * @return the list containing all enum elements
	 */
	private static <T extends Enum<T>> List<T> getEnumElementsAsList(Class<T> enumClass) {
		List<T> result = new ArrayList<T>();
		for (T element : enumClass.getEnumConstants()) {
			result.add(element);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.append("class", "DropDownChoicePanel", " ");
	}
	
}
