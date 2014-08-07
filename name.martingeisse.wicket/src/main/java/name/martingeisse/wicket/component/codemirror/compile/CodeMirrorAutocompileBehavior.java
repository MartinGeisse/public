/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.wicket.component.codemirror.compile;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.wicket.component.codemirror.CodeMirrorBehavior;
import name.martingeisse.wicket.util.WicketHeadUtil;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes.Method;
import org.apache.wicket.ajax.attributes.CallbackParameter;
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

	/**
	 * the compiler
	 */
	private final ICompiler compiler;

	/**
	 * Constructor.
	 * @param compiler the compiler
	 */
	public CodeMirrorAutocompileBehavior(final ICompiler compiler) {
		this.compiler = ParameterUtil.ensureNotNull(compiler, "compiler");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#renderHead(org.apache.wicket.Component, org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(final Component component, final IHeaderResponse response) {
		super.renderHead(component, response);
		WicketHeadUtil.includeClassJavascript(response, CodeMirrorAutocompileBehavior.class);

		final StringBuilder builder = new StringBuilder();
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
	protected void updateAjaxAttributes(final AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		attributes.setMethod(Method.POST);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void respond(final AjaxRequestTarget target) {
		final String value = RequestCycle.get().getRequest().getPostParameters().getParameterValue("value").toString("");
		CompilerResult result = compiler.compile(value);
		for (CompilerMarker marker : result.getMarkers()) {
			final StringBuilder builder = new StringBuilder();
			builder.append("addCodeMirrorAutocompilerMarkerToDocument('");
			builder.append(getComponent().getMarkupId());
			builder.append("', ").append(marker.getStartLine());
			builder.append(", ").append(marker.getStartColumn());
			builder.append(", ").append(marker.getEndLine());
			builder.append(", ").append(marker.getEndColumn());
			builder.append(", '").append(marker.getErrorLevel().name());
			builder.append("', '").append(JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(marker.getMessage()));
			builder.append("');");
		}
		target.appendJavaScript("console.log('autocompiling done!');");
	}

}
