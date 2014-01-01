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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This is a stateless variant of {@link AjaxSubmitLink} based on Wicketstuff-stateless.
 */
public class StatelessAjaxSubmitLink extends AbstractSubmitLink {
	private static final long serialVersionUID = 1L;

	private final Form<?> form;

	private final PageParameters pageParameters;

	/**
	 * Construct.
	 * 
	 * @param id the wicket id
	 */
	public StatelessAjaxSubmitLink(final String id) {
		this(id, null, null);
	}

	/**
	 * Construct.
	 * 
	 * @param id the wicket id
	 * @param form the form to submit
	 */
	public StatelessAjaxSubmitLink(final String id, final Form<?> form) {
		this(id, form, null);
	}
	
	/**
	 * Construct.
	 * 
	 * @param id the wicket id
	 * @param pageParameters the page parameters
	 */
	public StatelessAjaxSubmitLink(final String id, final PageParameters pageParameters) {
		this(id, null, pageParameters);
	}

	/**
	 * Construct.
	 * 
	 * @param id the wicket id
	 * @param form the form to submit
	 * @param pageParameters the page parameters
	 */
	public StatelessAjaxSubmitLink(final String id, final Form<?> form, final PageParameters pageParameters) {
		super(id, form);
		this.form = form;
		this.pageParameters = pageParameters;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(newAjaxFormSubmitBehavior("click"));
	}

	protected StatelessAjaxFormSubmitBehavior newAjaxFormSubmitBehavior(final String event) {
		return new StatelessAjaxFormSubmitBehavior(form, event) {
			private static final long serialVersionUID = 1L;

			@Override
			protected PageParameters getPageParameters() {
				return StatelessAjaxSubmitLink.this.pageParameters;
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target) {
				StatelessAjaxSubmitLink.this.onError(target, getForm());
			}

			@Override
			protected Form<?> findForm() {
				return StatelessAjaxSubmitLink.this.getForm();
			}

			@Override
			protected void onComponentTag(final ComponentTag tag) {
				// write the onclick handler only if link is enabled
				if (isLinkEnabled()) {
					super.onComponentTag(tag);
				}
			}

			@Override
			public boolean getDefaultProcessing() {
				return StatelessAjaxSubmitLink.this.getDefaultFormProcessing();
			}

			@Override
			protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				StatelessAjaxSubmitLink.this.updateAjaxAttributes(attributes);
			}

			@Override
			protected void onSubmit(final AjaxRequestTarget target) {
				StatelessAjaxSubmitLink.this.onSubmit(target, getForm());
			}

			@Override
			protected void onAfterSubmit(final AjaxRequestTarget target) {
				StatelessAjaxSubmitLink.this.onAfterSubmit(target, getForm());
			}
		};
	}

	/**
	 * Override this method to provide special submit handling in a multi-button form. This method
	 * will be called <em>before</em> the form's onSubmit method.
	 */
	protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {
	}

	/**
	 * Override this method to provide special submit handling in a multi-button form. This method
	 * will be called <em>after</em> the form's onSubmit method.
	 */
	protected void onAfterSubmit(final AjaxRequestTarget target, final Form<?> form) {
	}

	protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {
	}

	@Override
	protected void onComponentTag(final ComponentTag tag) {
		super.onComponentTag(tag);

		if (isLinkEnabled()) {
			if (tag.getName().toLowerCase().equals("a")) {
				tag.put("href", "javascript:;");
			}
		} else {
			disableLink(tag);
		}
	}

	/**
	 * Final implementation of the Button's onError. StatelessAjaxSubmitLinks have their own onError which is
	 * called.
	 * 
	 * @see org.apache.wicket.markup.html.form.Button#onError()
	 */
	@Override
	public final void onError() {
	}

	/**
	 * Listener method invoked on form submit with errors. This method is called <em>before</em>
	 * {@link Form#onError()}.
	 * 
	 * @param target
	 * @param form
	 */
	protected void onError(final AjaxRequestTarget target, final Form<?> form) {
	}

	/**
	 * Use {@link #onSubmit(AjaxRequestTarget, Form)} instead.
	 */
	@Override
	public final void onSubmit() {
	}

	/**
	 * Use {@link #onAfterSubmit(AjaxRequestTarget, Form)} instead.
	 */
	@Override
	public final void onAfterSubmit() {
	}
}
