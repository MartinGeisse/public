/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.stdform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.wicket.component.misc.BootstrapComponentFeedbackPanel;
import name.martingeisse.wicket.component.stateless.StatelessAjaxSubmitLink;
import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.html.form.LabeledWebMarkupContainer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This panel implements a "standard form", using a very regular
 * Bootstrap-based layout. It is intended to quickly build forms,
 * usually to edit data records, without any special layout or
 * content.
 * 
 * Form elements are stacked vertically using a {@link RepeatingView}
 * with manually added components. Each component uses a basic
 * piece of markup that contains a label, help text, error message and
 * the form control itself. Special form elements can be added by
 * adding components with intrinsic markup, such as panels or
 * fragments. Standard special components such as a submit button
 * are provided as fragments by this class.
 * 
 * The FORM element is contained in this panel.
 */
public class StandardFormPanel extends Panel {

	/**
	 * The wicket:id to use for the form control within the
	 * form control wrapper fragment.
	 */
	public static final String FORM_CONTROL_ID = "formControl";

	/**
	 * the modelUpdateListeners
	 */
	private List<ModelUpdateListener> modelUpdateListeners;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param stateless whether to use a StatelessForm
	 */
	public StandardFormPanel(final String id, final boolean stateless) {
		super(id);
		construct(stateless);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 * @param stateless whether to use a StatelessForm
	 */
	public StandardFormPanel(final String id, final IModel<?> model, final boolean stateless) {
		super(id, model);
		construct(stateless);
	}

	/**
	 * 
	 */
	private final void construct(final boolean stateless) {

		// Create the form. Stupid StatelessForm class requires us to duplicate code here :(
		final Form<Void> form;
		if (stateless) {
			form = new StatelessForm<Void>("form") {

				@Override
				protected void onValidate() {
					AjaxRequestUtil.markForRender(StandardFormPanel.this);
				}

				@Override
				protected void onSubmit() {
					internalOnSubmit();
				}

			};
		} else {
			form = new Form<Void>("form") {

				@Override
				protected void onValidate() {
					AjaxRequestUtil.markForRender(StandardFormPanel.this);
				}

				@Override
				protected void onSubmit() {
					internalOnSubmit();
				}

			};
		}

		// build the rest of the panel
		setOutputMarkupId(true);
		add(form);
		final RepeatingView formElements = new RepeatingView("formElements");
		form.add(formElements);

	}

	/**
	 * @return the form
	 */
	public final Form<?> getForm() {
		return (Form<?>)get("form");
	}

	/**
	 * @return the {@link RepeatingView} for the form elements
	 */
	public final RepeatingView getFormElementsRepeatingView() {
		return (RepeatingView)get("form:formElements");
	}

	/**
	 * Generates a component ID for a new raw form element.
	 * 
	 * @return the component ID
	 */
	public final String newRawFormElementId() {
		return getFormElementsRepeatingView().newChildId();
	}

	/**
	 * Adds the specified component as a "raw" form element, i.e. without
	 * an error text or label. This is typically used to add custom
	 * components that are not really form controls, or contain the error
	 * text and label in a special way.
	 * 
	 * @param component the component to add
	 */
	public final void addRawFormElement(final Component component) {
		getFormElementsRepeatingView().add(component);
	}

	/**
	 * Adds a form control wrapper fragment for the specified form control.
	 * 
	 * @param label the label for the form control
	 * @param formControl the form control to wrap
	 * @param labelTarget the component to refer to in the "for" attribute of the label.
	 * This parameter may be null; in this case, the "for" attribute will be omitted.
	 * @param errorSource the source for error messages. This parameter may be null;
	 * in this case, no error messages will be shown.
	 * @return the wrapper fragment
	 */
	public final Fragment addFormControl(final String label, final Component formControl, final LabeledWebMarkupContainer labelTarget, final Component errorSource) {
		final Fragment wrapperFragment = new Fragment(newRawFormElementId(), "formControlWrapperFragment", this);
		wrapperFragment.add(new BootstrapComponentFeedbackPanel("error", errorSource));
		wrapperFragment.add(new FormComponentLabel("label", labelTarget).add(new Label("text", label)));
		wrapperFragment.add(new Label("helpText"));
		wrapperFragment.add(new RepeatingView("toolLinks"));
		wrapperFragment.add(formControl);
		addRawFormElement(wrapperFragment);
		return wrapperFragment;
	}

	/**
	 * Adds a string-typed text field using the specified model.
	 * @param label the label for the text field
	 * @param model the model
	 * @return a configurator that can be used to further configure the added components
	 */
	public final SingleFormComponentElementConfigurator addTextField(final String label, final IModel<String> model) {
		final TextField<String> textField = new TextField<String>("textField", model);
		final Fragment wrapperFragment = addFormControl(label, new Fragment(FORM_CONTROL_ID, "textFieldFragment", this).add(textField), textField, textField);
		return new SingleFormComponentElementConfigurator(wrapperFragment, textField);
	}

	/**
	 * Adds a text field using the specified type and model.
	 * @param label the label for the text field
	 * @param type the model type
	 * @param model the model
	 * @return a configurator that can be used to further configure the added components
	 */
	public final <T> SingleFormComponentElementConfigurator addTextField(final String label, final Class<T> type, final IModel<T> model) {
		final TextField<T> textField = new TextField<T>("textField", model);
		textField.setType(type);
		final Fragment wrapperFragment = addFormControl(label, new Fragment(FORM_CONTROL_ID, "textFieldFragment", this).add(textField), textField, textField);
		return new SingleFormComponentElementConfigurator(wrapperFragment, textField);
	}

	/**
	 * Adds an email text field using the specified model.
	 * @param label the label for the text field
	 * @param model the model
	 * @return a configurator that can be used to further configure the added components
	 */
	public final SingleFormComponentElementConfigurator addEmailTextField(final String label, final IModel<String> model) {
		final EmailTextField textField = new EmailTextField("textField", model);
		final Fragment wrapperFragment = addFormControl(label, new Fragment(FORM_CONTROL_ID, "emailTextFieldFragment", this).add(textField), textField, textField);
		return new SingleFormComponentElementConfigurator(wrapperFragment, textField);
	}

	/**
	 * Adds a password field using the specified model.
	 * @param label the label for the password field
	 * @param model the model
	 * @return a configurator that can be used to further configure the added components
	 */
	public final SingleFormComponentElementConfigurator addPasswordTextField(final String label, final IModel<String> model) {
		final PasswordTextField textField = new PasswordTextField("textField", model);
		textField.setRequired(false); // probably *is* required, but no magic defaults please
		final Fragment wrapperFragment = addFormControl(label, new Fragment(FORM_CONTROL_ID, "passwordTextFieldFragment", this).add(textField), textField, textField);
		return new SingleFormComponentElementConfigurator(wrapperFragment, textField);
	}

	/**
	 * Adds a text area using the specified model.
	 * @param label the label for the text area
	 * @param model the model
	 * @param rows number of visible rows
	 * @return a configurator that can be used to further configure the added components
	 */
	public final SingleFormComponentElementConfigurator addTextArea(final String label, final IModel<String> model, final int rows) {
		final TextArea<String> textArea = new TextArea<String>("textArea", model) {
			@Override
			protected void onComponentTag(final ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("rows", rows);
			}
		};
		final Fragment wrapperFragment = addFormControl(label, new Fragment(FORM_CONTROL_ID, "textAreaFragment", this).add(textArea), textArea, textArea);
		return new SingleFormComponentElementConfigurator(wrapperFragment, textArea);
	}

	/**
	 * Adds an HTML fragment for an info text to the form.
	 * 
	 * @param model the model for the html fragment
	 * @return a configurator that can be used to further configure the added components
	 */
	public final ElementConfigurator addInfoHtml(final IModel<String> model) {
		final Fragment wrapperFragment = new Fragment(newRawFormElementId(), "infoHtmlFragment", this);
		wrapperFragment.add(new Label("html", model).setEscapeModelStrings(false));
		addRawFormElement(wrapperFragment);
		return new ElementConfigurator(wrapperFragment);
	}

	/**
	 * Adds a submit button for the form. 
	 */
	public final void addSubmitButton() {
		addSubmitButton("Submit");
	}

	/**
	 * Adds a submit button for the form. 
	 * @param text the button text
	 */
	public final void addSubmitButton(final String text) {
		final Label label = new Label("button", text);
		addRawFormElement(new Fragment(newRawFormElementId(), "submitButtonFragment", this).add(label));
	}

	/**
	 * Adds an AJAX submit button for the form. 
	 */
	public final void addAjaxSubmitButton() {
		addAjaxSubmitButton("Submit");
	}

	/**
	 * Adds an AJAX submit button for the form. 
	 * @param text the button text
	 */
	public final void addAjaxSubmitButton(final String text) {
		final AjaxSubmitLink submitLink = new AjaxSubmitLink("button") {};
		submitLink.setBody(Model.of(text));
		addRawFormElement(new Fragment(newRawFormElementId(), "submitButtonFragment", this).add(submitLink));
	}
	
	/**
	 * Adds a stateless AJAX submit button for the form. 
	 * @param pageParameters the page parameters used for submitting the form
	 */
	public final void addStatelessAjaxSubmitButton(PageParameters pageParameters) {
		addStatelessAjaxSubmitButton("Submit", pageParameters);
	}
	
	/**
	 * Adds a stateless AJAX submit button for the form. 
	 * @param text the button text
	 * @param pageParameters the page parameters used for submitting the form
	 */
	public final void addStatelessAjaxSubmitButton(final String text, PageParameters pageParameters) {
		final StatelessAjaxSubmitLink submitLink = new StatelessAjaxSubmitLink("button", pageParameters);
		submitLink.setBody(Model.of(text));
		addRawFormElement(new Fragment(newRawFormElementId(), "submitButtonFragment", this).add(submitLink));
	}

	/**
	 * Adds a listener that updates complex models. See {@link ModelUpdateListener}.
	 * @param listener the listener to add
	 */
	public void addModelUpdateListener(final ModelUpdateListener listener) {
		if (modelUpdateListeners == null) {
			modelUpdateListeners = new ArrayList<StandardFormPanel.ModelUpdateListener>(3);
		}
		modelUpdateListeners.add(listener);
	}

	/**
	 * This callback is called by the form itself when submitted. It invokes
	 * the model update listeners, then delegates to {@link #onSubmit()}.
	 */
	private void internalOnSubmit() {
		if (modelUpdateListeners != null) {
			for (final ModelUpdateListener listener : modelUpdateListeners) {
				listener.updateModel();
			}
		}
		onSubmit();
	}

	/**
	 * This callback is called when the form was submitted, after model update
	 * listeners have been invoked.
	 */
	protected void onSubmit() {
	}

	/**
	 * Clients can provide objects of this type to update complex models using the values
	 * from one or more form components. This is basically the equivalent to a custom
	 * {@link IFormModelUpdateListener}, except that the latter cannot be used in
	 * {@link StandardFormPanel}s because there is no enclosing component that could be
	 * a listener.
	 */
	public static interface ModelUpdateListener extends Serializable {

		/**
		 * This method is invoked after the individual form components have updated
		 * their models, but before {@link StandardFormPanel#onSubmit()} is called.
		 */
		public void updateModel();

	}

}
