/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.stdform;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * Specialized configurator for form elements that use the
 * standard wrapper.
 */
public class WrappedElementConfigurator extends ElementConfigurator {

	/**
	 * Constructor.
	 * @param rawElementComponent the component that represents the raw form element
	 */
	public WrappedElementConfigurator(final Component rawElementComponent) {
		super(rawElementComponent);
	}

	/**
	 * Sets the help text to show for the form control.
	 * @param helpText the help text
	 * @return this
	 */
	public final WrappedElementConfigurator setHelpText(final String helpText) {
		getRawElementComponent().get("helpText").replaceWith(new Label("helpText", helpText));
		return this;
	}

	/**
	 * @return the {@link RepeatingView} for the tool links.
	 */
	public final RepeatingView getToolLinksRepeatingView() {
		return (RepeatingView)getRawElementComponent().get("toolLinks");
	}

	/**
	 * Adds a tool link that just executes a javascript command.
	 * @param text the link text
	 * @param command the Javascript command to execute for the link
	 * @return this
	 */
	public final WrappedElementConfigurator addJavascriptToolLink(final String text, final String command) {
		final RepeatingView repeatingView = getToolLinksRepeatingView();
		repeatingView.add(new JavascriptToolLink(repeatingView.newChildId(), text, command));
		return this;
	}

	/**
	 *
	 */
	static class JavascriptToolLink extends AbstractLink {
		
		private String text;
		private String command;
		
		/**
		 * Constructor.
		 */
		JavascriptToolLink(String id, String text, String command) {
			super(id);
			this.text = text;
			this.command = command;
		}

		@Override
		protected void onComponentTag(final ComponentTag tag) {
			super.onComponentTag(tag);
			tag.put("href", "javascript:;");
			tag.put("onClick", command);
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.html.link.AbstractLink#onComponentTagBody(org.apache.wicket.markup.MarkupStream, org.apache.wicket.markup.ComponentTag)
		 */
		@Override
		public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
			replaceComponentTagBody(markupStream, openTag, text);
		}

	}
	
}
