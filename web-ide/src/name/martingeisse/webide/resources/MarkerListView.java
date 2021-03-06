/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import java.util.List;


import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * General-purpose list view to display a list of markers.
 * 
 * Client code must subclass this view and implement the
 * populateItem method. This class provides helper methods
 * to create common marker-related components.
 */
public abstract class MarkerListView extends ListView<FetchMarkerResult> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public MarkerListView(String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the marker list model
	 */
	public MarkerListView(String id, IModel<? extends List<? extends FetchMarkerResult>> model) {
		super(id, model);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param list the marker list
	 */
	public MarkerListView(String id, List<? extends FetchMarkerResult> list) {
		super(id, list);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched and displayed.
	 * @param limit the maximum number of markers to fetch
	 */
	public MarkerListView(String id, MarkerMeaning[] meaningFilter, long limit) {
		super(id, new MarkerDataListModel(meaningFilter, limit));
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param resourceHandle the handle for the resource to show markers for
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched and displayed.
	 * @param limit the maximum number of markers to fetch
	 */
	public MarkerListView(String id, ResourceHandle resourceHandle, MarkerMeaning[] meaningFilter, long limit) {
		super(id, new MarkerDataListModel(resourceHandle, meaningFilter, limit));
	}

	/**
	 * Creates, adds and returns a new {@link Label} for the path of the resource the marker is attached to.
	 * @param parent the parent component
	 * @param id the wicket id
	 * @param markerModel the model for the marker
	 * @return the label
	 */
	protected final Label addResourcePathLabel(WebMarkupContainer parent, String id, IModel<FetchMarkerResult> markerModel) {
		Label label = new Label(id, new PropertyModel<String>(markerModel, "resourcePath"));
		parent.add(label);
		return label;
	}

	/**
	 * Creates, adds and returns a new {@link Label} for the name of the resource the marker is attached to.
	 * @param parent the parent component
	 * @param id the wicket id
	 * @param markerModel the model for the marker
	 * @return the label
	 */
	protected final Label addResourceNameLabel(WebMarkupContainer parent, String id, IModel<FetchMarkerResult> markerModel) {
		Label label = new Label(id, new PropertyModel<String>(markerModel, "resourcePath.lastSegment"));
		parent.add(label);
		return label;
	}
	
	/**
	 * Creates, adds and returns a new {@link Label} for the marker meaning.
	 * @param parent the parent component
	 * @param id the wicket id
	 * @param markerModel the model for the marker
	 * @return the label
	 */
	protected final Label addMeaningLabel(WebMarkupContainer parent, String id, IModel<FetchMarkerResult> markerModel) {
		Label label = new Label(id, new PropertyModel<String>(markerModel, "meaning"));
		parent.add(label);
		return label;
	}

	/**
	 * Creates, adds and returns a new {@link Image} for the marker meaning.
	 * @param parent the parent component
	 * @param id the wicket id
	 * @param markerModel the model for the marker
	 * @return the image component
	 */
	protected final Image addMeaningIcon(WebMarkupContainer parent, String id, final IModel<FetchMarkerResult> markerModel) {
		Image image = new Image(id, new AbstractReadOnlyModel<ResourceReference>() {
			@Override
			public ResourceReference getObject() {
				return MarkerIconSelector.get(markerModel.getObject()).getResourceReference();
			}
		});
		parent.add(image);
		return image;
	}

	/**
	 * Creates, adds and returns a new {@link Label} for the marker message.
	 * @param parent the parent component
	 * @param id the wicket id
	 * @param markerModel the model for the marker
	 * @return the label
	 */
	protected final Label addMessageLabel(WebMarkupContainer parent, String id, IModel<FetchMarkerResult> markerModel) {
		Label label = new Label(id, new PropertyModel<String>(markerModel, "message"));
		parent.add(label);
		return label;
	}

}
