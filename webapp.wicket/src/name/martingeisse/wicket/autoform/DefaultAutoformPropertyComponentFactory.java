/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;

import name.martingeisse.common.terms.IGetDisplayNameAware;
import name.martingeisse.common.terms.IReadOnlyAware;
import name.martingeisse.wicket.autoform.annotation.AutoformComponent;
import name.martingeisse.wicket.autoform.annotation.ConstructorArgumentName;
import name.martingeisse.wicket.autoform.annotation.AutoformTextSuggestions;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription;
import name.martingeisse.wicket.model.conversion.LiberalBigDecimalConversionModel;
import name.martingeisse.wicket.model.conversion.LiberalIntegerConversionModel;
import name.martingeisse.wicket.panel.simple.CheckBoxPanel;
import name.martingeisse.wicket.panel.simple.DropDownChoicePanel;
import name.martingeisse.wicket.panel.simple.TextFieldPanel;
import name.martingeisse.wicket.panel.simple.TextFieldWithSuggestionsPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * This factory chooses an appropriate property component based on the property
 * type. Since this factory cannot respect application-specific property types,
 * applications may want to use a subclass of this class. This class also
 * respects any {@link AutoformComponent} annotation (if present) to override
 * the default behavior.
 */
public class DefaultAutoformPropertyComponentFactory implements IAutoformPropertyComponentFactory {

	/**
	 * the default instance of this class
	 */
	public static final DefaultAutoformPropertyComponentFactory instance = new DefaultAutoformPropertyComponentFactory();

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformPropertyComponentFactory#createPropertyComponent(java.lang.String, name.martingeisse.terra.wicket.autoform.AutoformPropertyDescriptor)
	 */
	@Override
	public final Component createPropertyComponent(String id, IAutoformPropertyDescription propertyDescriptor) {

		Class<? extends Component> componentClassOverride = propertyDescriptor.getComponentClassOverride();
		if (componentClassOverride == null) {
			return createPropertyComponentNoOverride(id, propertyDescriptor);
		}
		
		final ConstructorArgumentName additionalArgumentAnnotation = propertyDescriptor.getAnnotationProvider().getAnnotation(ConstructorArgumentName.class);
		String additionalArgument = (additionalArgumentAnnotation != null ? additionalArgumentAnnotation.value() : null);

		try {
			
			// create the component
			final Constructor<? extends Component> constructor = findPropertyComponentConstructor(componentClassOverride, additionalArgument);
			Component panel;
			if (additionalArgument == null) {
				if (constructor.getParameterTypes().length == 2) {
					panel = constructor.newInstance(id, propertyDescriptor.getModel());
				} else {
					panel = constructor.newInstance(id, propertyDescriptor.getModel(), propertyDescriptor.getBeanModel());
				}
			} else {
				if (constructor.getParameterTypes().length == 3) {
					panel = constructor.newInstance(id, propertyDescriptor.getModel(), additionalArgument);
				} else {
					panel = constructor.newInstance(id, propertyDescriptor.getModel(), propertyDescriptor.getBeanModel(), additionalArgument);
				}
			}
			
			// configure the component for read-only properties
			if (panel instanceof IReadOnlyAware) {
				IReadOnlyAware readOnlyAware = (IReadOnlyAware)panel;
				readOnlyAware.setReadOnly(propertyDescriptor.isReadOnly());
			}
			
			return panel;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	private final Constructor<? extends Panel> findPropertyComponentConstructor(Class<? extends Panel> componentClass, String additionalArgument) {
		if (additionalArgument == null) {
			try {
				return componentClass.getConstructor(String.class, IModel.class, IModel.class);
			} catch (NoSuchMethodException e) {
			}
			try {
				return componentClass.getConstructor(String.class, IModel.class);
			} catch (NoSuchMethodException e) {
			}
		} else {
			try {
				return componentClass.getConstructor(String.class, IModel.class, IModel.class, String.class);
			} catch (NoSuchMethodException e) {
			}
			try {
				return componentClass.getConstructor(String.class, IModel.class, String.class);
			} catch (NoSuchMethodException e) {
			}
		}
		throw new RuntimeException("no suitable autoform component constructor found in class " + componentClass);
	}

	/**
	 * This method is used to create the component if no {@link AutoformComponent} annotation is present.
	 * @param id the wicket id
	 * @param propertyDescriptor the property descriptor of the property
	 * @return the component
	 */
	protected Panel createPropertyComponentNoOverride(String id, IAutoformPropertyDescription propertyDescriptor) {

		final Class<?> type = propertyDescriptor.getType();
		final IModel<?> model = propertyDescriptor.getModel();

		if (type == String.class) {

			AutoformTextSuggestions suggestionsAnnotation = propertyDescriptor.getAnnotationProvider().getAnnotation(AutoformTextSuggestions.class);
			String[] suggestions = (suggestionsAnnotation == null) ? null : suggestionsAnnotation.value();
			if (suggestions != null && !propertyDescriptor.isReadOnly()) {
				final TextFieldWithSuggestionsPanel panel = new TextFieldWithSuggestionsPanel(id, this.<String> castModelUnsafe(model));
				panel.setSuggestions(suggestions);
				return panel;
			} else {
				final TextFieldPanel<String> panel = new TextFieldPanel<String>(id, this.<String> castModelUnsafe(model));
				if (propertyDescriptor.isReadOnly()) {
					panel.getTextField().setEnabled(false);
				}
				return panel;
			}
			
		} else if (type == Integer.class || type == Integer.TYPE) {
			final IModel<String> wrapperModel = new LiberalIntegerConversionModel(this.<Integer> castModelUnsafe(model));
			final TextFieldPanel<String> panel = new TextFieldPanel<String>(id, wrapperModel);
			if (propertyDescriptor.isReadOnly()) {
				panel.getTextField().setEnabled(false);
			}
			return panel;
			
		} else if (type == BigDecimal.class) {
			final IModel<String> wrapperModel = new LiberalBigDecimalConversionModel(this.<BigDecimal> castModelUnsafe(model));
			final TextFieldPanel<String> panel = new TextFieldPanel<String>(id, wrapperModel);
			if (propertyDescriptor.isReadOnly()) {
				panel.getTextField().setEnabled(false);
			}
			return panel;
			
		} else if (type == Boolean.class || type == Boolean.TYPE) {
			final CheckBoxPanel panel = new CheckBoxPanel(id, this.<Boolean> castModelUnsafe(model));
			if (propertyDescriptor.isReadOnly()) {
				panel.getCheckBox().setEnabled(false);
			}
			return panel;
			
		} else if (Enum.class.isAssignableFrom(type)) {
			return dropDownChoiceHelper(id, model, type);
			
		} else {
			throw new RuntimeException("cannot create autoform property component for type: " + type.getCanonicalName());
		}

	}
	
	/**
	 * Returns the specified model, cast to type IModel<T> without runtime checking.
	 * @param <T> the target model type
	 * @param model the model to cast
	 * @return returns the argument
	 */
	@SuppressWarnings("unchecked")
	protected final <T> IModel<T> castModelUnsafe(IModel<?> model) {
		return (IModel<T>)model;
	}
	
	/**
	 * This helper exists to sidestep issues with generics. It assumes that the model and enumClass
	 * are compatible and creates a {@link DropDownChoicePanel}.
	 * @param id the wicket id
	 * @param model the model
	 * @param enumClass the enum class
	 * @return the drop-down choice panel
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private final DropDownChoicePanel dropDownChoiceHelper(String id, IModel model, Class enumClass) {
		if (IGetDisplayNameAware.class.isAssignableFrom(enumClass)) {
			return DropDownChoicePanel.createForDisplayNameEnum(id, model, enumClass);
		} else {
			return DropDownChoicePanel.createForRawEnum(id, model, enumClass);
		}
	}

}
