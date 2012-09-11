/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.instance;

import name.martingeisse.admin.entity.EntitySelection;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.reference.EntityReferenceEndpoint;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Base class for panels that visualize or edit single entity instances.
 * This class is not needed to build such panels; it just adds
 * useful base functionality.
 * 
 * The default model is used to obtain the entity.
 */
public class AbstractEntityInstancePanel extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param instanceModel the model
	 */
	public AbstractEntityInstancePanel(final String id, final IModel<EntityInstance> instanceModel) {
		super(id, ParameterUtil.ensureNotNull(instanceModel, "instanceModel"));
	}

	/**
	 * Getter method for the model.
	 * @return the model
	 */
	public final IModel<EntityInstance> getModel() {
		return GenericTypeUtil.unsafeCast(ReturnValueUtil.nullMeansMissing(getDefaultModel(), "model"));
	}

	/**
	 * Setter method for the model.
	 * @param model the model to set
	 */
	public final void setModel(final IModel<EntityInstance> model) {
		setDefaultModel(ParameterUtil.ensureNotNull(model, "model"));
	}

	/**
	 * Getter method for the entityInstance.
	 * @return the entityInstance
	 */
	public final EntityInstance getEntityInstance() {
		return ReturnValueUtil.nullMeansMissing(getModel().getObject(), "model object");
	}
	
	/**
	 * Returns the entity descriptor of the entity being displayed by this panel.
	 * @return the entity descriptor
	 */
	public final EntityDescriptor getEntityDescriptor() {
		return getEntityInstance().getEntity();
	}

	/**
	 * Creates a model that returns a related entity instance.
	 * 
	 * TODO: This method currently unpacks the instance for this panel and extracts
	 * the reference key value for use in the returned model. If that value is later
	 * changed in the entity instance, the returned model will still return the
	 * old reference target and not the instance to which the new value refers.
	 * 
	 * TODO: the reference (endpoints) should store information about whether null is
	 * a value that establishes a reference between partners that both have a
	 * null value, or if null indicates implicit absence of a reference partner.
	 * 
	 * @param nearPropertyName the name of the "near" property
	 * or indicates an empty selection implicitly (false)
	 *  
	 * @return the model for the reference target
	 */
	public final IModel<EntityInstance> createModelForRelatedSingleEntityInstance(String nearPropertyName) {
		ParameterUtil.ensureNotNull(nearPropertyName, "nearPropertyName");
		
		// find the reference endpoint
		final EntityReferenceEndpoint nearEndpoint = getEntityDescriptor().findReference(nearPropertyName);
		if (nearEndpoint == null) {
			throw new IllegalArgumentException("no such reference: " + getEntityDescriptor().getName() + "." + nearPropertyName);
		}
		final EntityReferenceEndpoint farEndpoint = nearEndpoint.getOther();
		if (farEndpoint.getMultiplicity().isMultiple()) {
			throw new IllegalArgumentException("reference has potentially-multiple far endpoints: " + getEntityDescriptor().getName() + "." + nearPropertyName);
		}
		
		// TODO unpack the entity instance for this panel to obtain the value of the key property
		final Object keyValue = getEntityInstance().getFieldValue(nearPropertyName);
		
		// TODO see comment, this value should come from the endpoint (and be the same in both endpoints of any reference)
		boolean nullIsReference = false;
		
		// create a selection and turn it into an entity instance model
		final EntitySelection selection = EntitySelection.forKey(farEndpoint.getEntity(), farEndpoint.getPropertyName(), keyValue, nullIsReference);
		return selection.createSingleInstanceModel(farEndpoint.getMultiplicity().isOptional());
		
	}
	
}
