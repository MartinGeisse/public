/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.papyros.frontend.components;

import java.util.List;
import name.martingeisse.wicket.component.codemirror.compile.CompilerMarker;
import name.martingeisse.wicket.component.misc.GlyphiconComponent;
import name.martingeisse.wicket.model.relay.ComponentRelayModel;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * A panel that shows a list of CodeMirror autocompiler markers.
 * 
 * This component should be attached to a UL tag.
 */
public final class CompilerMarkerListPanel extends Panel {

	/**
	 * Constructor.
	 * @param id the Wicket id
	 */
	public CompilerMarkerListPanel(String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the Wicket id
	 * @param model the model
	 */
	public CompilerMarkerListPanel(String id, IModel<List<CompilerMarker>> model) {
		super(id, model);
	}
	
	/**
	 * Getter method for the model.
	 * @return the model
	 */
	@SuppressWarnings("unchecked")
	public final IModel<List<CompilerMarker>> getModel() {
		return (IModel<List<CompilerMarker>>)getDefaultModel();
	}
	
	/**
	 * Setter method for the model.
	 * @param model the model
	 */
	public final void setModel(final IModel<List<CompilerMarker>> model) {
		setDefaultModel(model);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		setOutputMarkupId(true);
		add(new ListView<CompilerMarker>("compilerMarkers", new ComponentRelayModel<List<CompilerMarker>>(this)) {
			@Override
			protected void populateItem(final ListItem<CompilerMarker> item) {

				// add color class
				final CompilerMarker marker = item.getModelObject();
				final String cssClassName = "autocompile-" + marker.getErrorLevel().name().toLowerCase() + "-color";
				item.add(new AttributeAppender("class", Model.of(cssClassName), " "));

				// add icon
				item.add(new GlyphiconComponent("icon") {
					@Override
					protected String getGlyphiconIdentifier() {
						return marker.getErrorLevel().getGlyphicon();
					}
				});

				// add message
				final StringBuilder builder = new StringBuilder();
				builder.append(marker.getStartLine() + 1);
				builder.append(", ");
				builder.append(marker.getStartColumn() + 1);
				builder.append(": ");
				builder.append(marker.getMessage());
				item.add(new Label("message", builder));

			}
		});
	}
	
}
