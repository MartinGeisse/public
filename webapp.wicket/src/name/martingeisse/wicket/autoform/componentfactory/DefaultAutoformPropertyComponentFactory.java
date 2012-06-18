/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.componentfactory;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;

import name.martingeisse.common.terms.IGetDisplayNameAware;
import name.martingeisse.common.terms.IReadOnlyAware;
import name.martingeisse.wicket.autoform.annotation.components.AutoformComponent;
import name.martingeisse.wicket.autoform.annotation.components.AutoformTextSuggestions;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription;
import name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor;
import name.martingeisse.wicket.autoform.validation.IValidatorAcceptor;
import name.martingeisse.wicket.model.conversion.LiberalBigDecimalConversionModel;
import name.martingeisse.wicket.model.conversion.LiberalIntegerConversionModel;
import name.martingeisse.wicket.panel.simple.CheckBoxPanel;
import name.martingeisse.wicket.panel.simple.DropDownChoicePanel;
import name.martingeisse.wicket.panel.simple.TextFieldPanel;
import name.martingeisse.wicket.panel.simple.TextFieldWithSuggestionsPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidator;

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
	 * @see name.martingeisse.wicket.autoform.componentfactory.IAutoformPropertyComponentFactory#createPropertyComponent(java.lang.String, name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescription, org.apache.wicket.validation.IValidator<?>[], name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor)
	 */
	@Override
	public final Component createPropertyComponent(String id, IAutoformPropertyDescription propertyDescription, IValidator<?>[] validators, IValidationErrorAcceptor validationErrorAcceptor) {
		Class<? extends Component> componentClassOverride = propertyDescription.getComponentClassOverride();
		if (componentClassOverride == null) {
			return createPropertyComponentNoOverride(id, propertyDescription, validators, validationErrorAcceptor);
		} else {
			return createPropertyComponent(id, propertyDescription, componentClassOverride, validators, validationErrorAcceptor);
		}
	}
	
	/**
	 * Creates a property component for a specific component class. This is used to implement
	 * {@link AutoformComponent}.
	 * @param id the wicket id of the component to create
	 * @param propertyDescription the property description
	 * @param componentClassOverride the component class to use
	 * @param validators the validators to add to the component
	 * @param validationErrorAcceptor the acceptor for validation errors
	 * @return the component
	 */
	protected final Component createPropertyComponent(String id, IAutoformPropertyDescription propertyDescription, Class<? extends Component> componentClassOverride, IValidator<?>[] validators, IValidationErrorAcceptor validationErrorAcceptor) {
		try {
			Component component = findAndInvokePropertyComponentConstructor(id, propertyDescription, componentClassOverride);
			if (component instanceof IReadOnlyAware) {
				IReadOnlyAware readOnlyAware = (IReadOnlyAware)component;
				readOnlyAware.setReadOnly(propertyDescription.isReadOnly());
			} else if (propertyDescription.isReadOnly()) {
				throw new RuntimeException("property " + propertyDescription.getName() + " is marked as read-only but component class " + component.getClass() + " does not implement IReadOnlyAware");
			}
			if (validators.length > 0) {
				if (!(component instanceof IValidatorAcceptor)) {
					throw new RuntimeException("property " + propertyDescription.getName() + " has validation annotations but component class " + component.getClass() + " does not implement IValidatorAcceptor");
				}
				IValidatorAcceptor acceptor = (IValidatorAcceptor)component;
				acceptor.acceptValidators(validators, validationErrorAcceptor);
			}
			return component;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Invokes an appropriate constructor for the component.
	 * @param id the wicket id
	 * @param propertyDescription the property description
	 * @param componentClass the component class to use
	 * @return the component instance
	 * @throws Exception on errors
	 */
	private final Component findAndInvokePropertyComponentConstructor(String id, IAutoformPropertyDescription propertyDescription, Class<? extends Component> componentClass) throws Exception {
		final Annotation additionalArgument = propertyDescription.getComponentConstructorArgument();
		final IModel<?> propertyModel = propertyDescription.getModel();
		if (additionalArgument == null) {
			try {
				return componentClass.getConstructor(String.class, IModel.class).newInstance(id, propertyModel);
			} catch (NoSuchMethodException e) {
			}
		} else {
			try {
				return componentClass.getConstructor(String.class, IModel.class, additionalArgument.annotationType()).newInstance(id, propertyModel, additionalArgument);
			} catch (NoSuchMethodException e) {
			}
			try {
				return componentClass.getConstructor(String.class, IModel.class, Annotation.class).newInstance(id, propertyModel, additionalArgument);
			} catch (NoSuchMethodException e) {
			}
		}
		throw new RuntimeException("no suitable autoform component constructor found in class " + componentClass);
	}
	
	/**
	 * This method is used to create the component if no {@link AutoformComponent} annotation is present.
	 * @param id the wicket id
	 * @param propertyDescriptor the property descriptor of the property
	 * @param validators the validators to add to the component
	 * @param validationErrorAcceptor the acceptor for validation errors
	 * @return the component
	 */
	protected Panel createPropertyComponentNoOverride(String id, IAutoformPropertyDescription propertyDescriptor, IValidator<?>[] validators, IValidationErrorAcceptor validationErrorAcceptor) {

		// TODO: Do not use liberal conversion models. Instead, properly use Wicket's conversion/validation mechanism.
		
		final Class<?> type = propertyDescriptor.getType();
		final IModel<?> model = propertyDescriptor.getModel();

		if (type == String.class) {
			AutoformTextSuggestions suggestionsAnnotation = propertyDescriptor.getAnnotation(AutoformTextSuggestions.class);
			String[] suggestions = (suggestionsAnnotation == null) ? null : suggestionsAnnotation.value();
			if (suggestions != null && !propertyDescriptor.isReadOnly()) {
				final TextFieldWithSuggestionsPanel panel = new TextFieldWithSuggestionsPanel(id, this.<String> castModelUnsafe(model));
				panel.setSuggestions(suggestions);
				addValidatorsUnsafe(panel.getTextField(), validators);
				validationErrorAcceptor.acceptValidationErrorsFrom(panel.getTextField());
				return panel;
			} else {
				final TextFieldPanel<String> panel = new TextFieldPanel<String>(id, this.<String> castModelUnsafe(model));
				if (propertyDescriptor.isReadOnly()) {
					panel.getTextField().setEnabled(false);
				}
				addValidatorsUnsafe(panel.getTextField(), validators);
				validationErrorAcceptor.acceptValidationErrorsFrom(panel.getTextField());
				return panel;
			}
			
		} else if (type == Integer.class || type == Integer.TYPE) {
			final IModel<String> wrapperModel = new LiberalIntegerConversionModel(this.<Integer> castModelUnsafe(model));
			final TextFieldPanel<String> panel = new TextFieldPanel<String>(id, wrapperModel);
			if (propertyDescriptor.isReadOnly()) {
				panel.getTextField().setEnabled(false);
			}
			addValidatorsUnsafe(panel.getTextField(), validators);
			validationErrorAcceptor.acceptValidationErrorsFrom(panel.getTextField());
			return panel;
			
		} else if (type == BigDecimal.class) {
			final IModel<String> wrapperModel = new LiberalBigDecimalConversionModel(this.<BigDecimal> castModelUnsafe(model));
			final TextFieldPanel<String> panel = new TextFieldPanel<String>(id, wrapperModel);
			if (propertyDescriptor.isReadOnly()) {
				panel.getTextField().setEnabled(false);
			}
			addValidatorsUnsafe(panel.getTextField(), validators);
			validationErrorAcceptor.acceptValidationErrorsFrom(panel.getTextField());
			return panel;
			
		} else if (type == Boolean.class || type == Boolean.TYPE) {
			final CheckBoxPanel panel = new CheckBoxPanel(id, this.<Boolean> castModelUnsafe(model));
			if (propertyDescriptor.isReadOnly()) {
				panel.getCheckBox().setEnabled(false);
			}
			addValidatorsUnsafe(panel.getCheckBox(), validators);
			validationErrorAcceptor.acceptValidationErrorsFrom(panel.getCheckBox());
			return panel;
			
		} else if (Enum.class.isAssignableFrom(type)) {
			DropDownChoicePanel<?> panel = dropDownChoiceHelper(id, model, type);
			addValidatorsUnsafe(panel.getDropDownChoice(), validators);
			validationErrorAcceptor.acceptValidationErrorsFrom(panel.getDropDownChoice());
			return panel;
			
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
	 * are compatible and creates a {@link DropDownChoicePanel}. Will use the enum constant name
	 * for the id, and either the display name from {@link IGetDisplayNameAware} (if the enum type
	 * supports it) or the enum constant name for displaying.
	 * 
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

	/**
	 * Adds the validators to the component, bypassing generic type safety.
	 * @param component the component to add the validators to
	 * @param validators the validators to add to the component
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	protected final void addValidatorsUnsafe(FormComponent component, IValidator[] validators) {
		component.add(validators);
	}
	
}
