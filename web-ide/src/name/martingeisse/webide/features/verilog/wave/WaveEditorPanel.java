/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.verilog.wave;

import name.martingeisse.common.image.BufferedImageBackend;
import name.martingeisse.common.image.BufferedImageBackendType;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Wicket panel for the VCD viewer.
 */
public class WaveEditorPanel extends Panel {

	/**
	 * the totalLength
	 */
	private final int totalLength;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the document model
	 */
	public WaveEditorPanel(final String id, final IModel<ValueChangeDump> model) {
		super(id, model);
		
		long length = 0;
		for (ValueChangeDump.Variable variable : model.getObject().getVariables()) {
			long variableLength = variable.getLastChangeTime();
			if (variableLength > length) {
				length = variableLength;
			}
			System.out.println("*** " + variable.getOriginalIdentifier());
		}
		this.totalLength = (int)length;
		
		add(new ListView<ValueChangeDump.Variable>("variables", model.getObject().getVariables()) {
			@Override
			protected void populateItem(final ListItem<ValueChangeDump.Variable> item) {
				ValueChangeDump.Variable variable = item.getModelObject();
				WaveRenderer renderer = new WaveRenderer(totalLength + 100, 16, variable.getValueChanges());
				BufferedDynamicImageResource resource = new BufferedDynamicImageResource();
				BufferedImageBackend imageBackend = (BufferedImageBackend)renderer.render(BufferedImageBackendType.instance);
				resource.setImage(imageBackend.getBufferedImage());
				item.add(new Label("name", variable.getOriginalIdentifier()));
				item.add(new Image("wave", resource));
			}
		});
	}

}
