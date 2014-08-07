/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.wicket.component.codemirror.compile;

import name.martingeisse.wicket.component.codemirror.CodeMirrorBehavior;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes.Method;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * Additional behavior on top of {@link CodeMirrorBehavior} that
 * auto-compiles the code in the background without "properly"
 * submitting its form (i.e. the HTML form gets submitted, but not
 * processed on the server side).
 * 
 * This behavior should be attached to the same {@link TextField} as
 * the {@link CodeMirrorBehavior}. In principle, this behavior can be
 * used with any {@link FormComponent}, but the CodeMirror-specific
 * feedback information (e.g. error line numbers) are useless in
 * that case.
 */
public class CodeMirrorAutocompileBehavior extends AbstractDefaultAjaxBehavior {

	/* (non-Javadoc)
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#renderHead(org.apache.wicket.Component, org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		StringBuilder builder = new StringBuilder();
		builder.append("initializeCodeMirrorAutocompiler('");
		builder.append(component.getMarkupId());
		builder.append("', {}, ");
		builder.append(getCallbackFunction(CallbackParameter.explicit("value")));
		builder.append("); ");
		response.render(OnDomReadyHeaderItem.forScript(builder.toString()));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#updateAjaxAttributes(org.apache.wicket.ajax.attributes.AjaxRequestAttributes)
	 */
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		attributes.setMethod(Method.POST);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void respond(AjaxRequestTarget target) {
		String value = RequestCycle.get().getRequest().getPostParameters().getParameterValue("value").toString("");
		System.out.println("***YAY*** CodeMirrorAutocompileBehavior! value: " + value);
		target.appendJavaScript("console.log('autocompiling done!');");
	}

}
