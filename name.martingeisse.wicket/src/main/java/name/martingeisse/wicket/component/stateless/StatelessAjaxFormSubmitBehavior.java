/*
 * Original License:
 * --------------------------------------------------------------------------
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
 * --------------------------------------------------------------------------
 * 
 * Modifications (c) 2014 Martin Geisse.
 */
package name.martingeisse.wicket.component.stateless;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes.Method;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmitter;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.wicketstuff.stateless.StatelessAjaxEventBehavior;

/**
 * This is a stateless variant of {@link AjaxFormSubmitBehavior} based on Wicketstuff-stateless.
 */
public abstract class StatelessAjaxFormSubmitBehavior extends StatelessAjaxEventBehavior {
	private static final long serialVersionUID = 1L;

	/**
	 * should never be accessed directly (thus the __ cause its overkill to create a super class),
	 * instead always use #getForm()
	 */
	private Form<?> __form;

	private boolean defaultProcessing = true;

	/**
	 * Constructor. This constructor can only be used when the component this behavior is attached
	 * to is inside a form.
	 * 
	 * @param event
	 *            javascript event this behavior is attached to, like onclick
	 */
	public StatelessAjaxFormSubmitBehavior(final String event) {
		this(null, event);
	}

	/**
	 * Construct.
	 * 
	 * @param form
	 *            form that will be submitted
	 * @param event
	 *            javascript event this behavior is attached to, like onclick
	 */
	public StatelessAjaxFormSubmitBehavior(final Form<?> form, final String event) {
		super(event);
		__form = form;

		if (form != null) {
			form.setOutputMarkupId(true);
		}
	}

	/**
	 * 
	 * @return Form that will be submitted by this behavior
	 */
	public final Form<?> getForm() {
		if (__form == null) {
			__form = findForm();

			if (__form == null) {
				throw new IllegalStateException("form was not specified in the constructor and cannot " + "be found in the hierarchy of the component this behavior " + "is attached to: Component=" + getComponent().toString(false));
			}
		}
		return __form;
	}

	/**
	 * Finds form that will be submitted
	 * 
	 * @return form to submit or {@code null} if none found
	 */
	protected Form<?> findForm() {
		// try to find form in the hierarchy of owning component
		final Component component = getComponent();
		if (component instanceof Form<?>) {
			return (Form<?>)component;
		} else {
			return component.findParent(Form.class);
		}
	}

	@Override
	protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);

		final Form<?> form = getForm();
		attributes.setFormId(form.getMarkupId());

		final String formMethod = form.getMarkupAttributes().getString("method");
		if (formMethod == null || "POST".equalsIgnoreCase(formMethod)) {
			attributes.setMethod(Method.POST);
		}

		if (form.getRootForm().isMultiPart()) {
			attributes.setMultipart(true);
			attributes.setMethod(Method.POST);
		}

		if (getComponent() instanceof IFormSubmittingComponent) {
			final String submittingComponentName = ((IFormSubmittingComponent)getComponent()).getInputName();
			attributes.setSubmittingComponentName(submittingComponentName);
		}
	}

	/**
	 * @see org.apache.wicket.ajax.AjaxEventBehavior#onEvent(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onEvent(final AjaxRequestTarget target) {
		getForm().getRootForm().onFormSubmitted(new IFormSubmitter() {
			@Override
			public Form<?> getForm() {
				return StatelessAjaxFormSubmitBehavior.this.getForm();
			}

			@Override
			public boolean getDefaultFormProcessing() {
				return StatelessAjaxFormSubmitBehavior.this.getDefaultProcessing();
			}

			@Override
			public void onError() {
				StatelessAjaxFormSubmitBehavior.this.onError(target);
			}

			@Override
			public void onSubmit() {
				StatelessAjaxFormSubmitBehavior.this.onSubmit(target);
			}

			@Override
			public void onAfterSubmit() {
				StatelessAjaxFormSubmitBehavior.this.onAfterSubmit(target);
			}
		});
	}

	/**
	 * Override this method to provide special submit handling in a multi-button form. This method
	 * will be called <em>after</em> the form's onSubmit method.
	 */
	protected void onAfterSubmit(final AjaxRequestTarget target) {
	}

	/**
	 * Override this method to provide special submit handling in a multi-button form. This method
	 * will be called <em>before</em> the form's onSubmit method.
	 */
	protected void onSubmit(final AjaxRequestTarget target) {
	}

	/**
	 * Listener method invoked when the form has been processed and errors occurred
	 * 
	 * @param target
	 */
	protected void onError(final AjaxRequestTarget target) {
	}

	/**
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#getPreconditionScript()
	 */
	@Override
	@Deprecated
	protected CharSequence getPreconditionScript() {
		return null;
	}

	/**
	 * @see Button#getDefaultFormProcessing()
	 * 
	 * @return {@code true} for default processing
	 */
	public boolean getDefaultProcessing() {
		return defaultProcessing;
	}

	/**
	 * @see Button#setDefaultFormProcessing(boolean)
	 * @param defaultProcessing the defaultProcessing
	 */
	public void setDefaultProcessing(final boolean defaultProcessing) {
		this.defaultProcessing = defaultProcessing;
	}
	
}
