/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.console;

import name.martingeisse.ecosim.devices.chardisplay.CharacterDisplayController;
import name.martingeisse.webide.features.ecosim.EcosimEvents;
import name.martingeisse.webide.features.simvm.editor.SameSimulationFilter;
import name.martingeisse.webide.features.simvm.model.ISimulationModelElement;
import name.martingeisse.webide.features.simvm.simulation.SimulationEventMessage;
import name.martingeisse.webide.features.simvm.simulation.SimulationEvents;
import name.martingeisse.webide.ipc.IpcEvent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.atmosphere.Subscribe;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * This panel shows a console in the web UI.
 */
public class ConsolePanel extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public ConsolePanel(final String id, IModel<ISimulationModelElement> model) {
		super(id, model);
		setOutputMarkupId(true);
		add(new CharacterDisplayComponent("characterDisplay"));
	}
	
	/**
	 * @return the model element for this panel
	 */
	public ConsoleModelElement getConsoleModelElement() {
		return (ConsoleModelElement)getDefaultModelObject();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("class", "auto-focus");
	}
	
	/**
	 * Subscribes to {@link IpcEvent}s.
	 * @param target the request handler
	 * @param message the event message
	 */
	@Subscribe(filter = SameSimulationFilter.class)
	public void onSimulatorGeneratedEvent(final AjaxRequestTarget target, final SimulationEventMessage message) {
		final IpcEvent event = message.getEvent();
		final String eventType = event.getType();
		if (eventType.equals(EcosimEvents.CHARACTER_DISPLAY_OUTPUT)) {
			target.add(this);
		} else if (eventType.equals(SimulationEvents.EVENT_TYPE_START)) {
			target.add(this);
		}
	}
	
	/**
	 * 
	 */
	CharSequence buildDisplayContents() {
		CharacterDisplayController controller = getConsoleModelElement().getDisplayController();
		StringBuilder builder = new StringBuilder();
		for (int row = 0; row < 30; row++) {
			for (int column = 0; column < 80; column++) {
				int character = controller.getCharacter(column, row) & 0xff;
				int attribute = controller.getAttribute(column, row) & 0xff;
				char mappedCharacter = mapOutputCharacter(character);
				builder.append("<span class=\"_").append(Integer.toHexString(attribute & 0x0f)).append(" x");
				builder.append((attribute >> 4) & 7).append('_');
				if ((attribute & 0x80) != 0) {
					builder.append(" bl");
				}
				builder.append("\">");
				if (mappedCharacter == '<') {
					builder.append("&lt;"); 
				} else if (mappedCharacter == '>') {
					builder.append("&gt;"); 
				} else if (mappedCharacter == '&') {
					builder.append("&amp;"); 
				} else {
					builder.append(mappedCharacter);
				}
				builder.append("</span>");
			}
			if (row != 29) {
				builder.append('\n');
			}
		}
		return builder;
	}
	
	private char mapOutputCharacter(int code) {
		// we need to sidestep control characters since the browser won't display their glyphs
		if (code < 0x20) {
			return (char)(code + 0x100);
		} else if (code >= 0x80 && code < 0xa0) {
			return (char)(code - 0x80 + 0x120);
		} else if (code == 0x7f) {
			return (char)0x140;
		} else if (code == 0xa0) {
			return (char)0x141;
		} else if (code == 0xad) {
			return (char)0x142;
		} else {
			return (char)code;
		}
	}
	
	/**
	 * This component generates markup for the character display in a
	 * compact way.
	 */
	class CharacterDisplayComponent extends WebComponent {
		
		CharacterDisplayComponent(String id) {
			super(id);
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.Component#onComponentTagBody(org.apache.wicket.markup.MarkupStream, org.apache.wicket.markup.ComponentTag)
		 */
		@Override
		public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
			replaceComponentTagBody(markupStream, openTag, buildDisplayContents());
		}
		
	}
	
}
