/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import java.util.NoSuchElementException;

import name.martingeisse.admin.entity.instance.IEntityInstance;
import name.martingeisse.common.util.ParameterUtil;

import org.apache.wicket.model.IModel;

/**
 * A model for a single entity instance based on a {@link EntitySelection}.
 * 
 * Note that you cannot create instances of this class directly. Instead,
 * you must create instances of one of its subclasses, {@link Required} and
 * {@link Optional}.
 */
public class EntityInstanceModel implements IModel<IEntityInstance> {

	/**
	 * the selection
	 */
	final EntitySelection selection;

	/**
	 * Constructor.
	 * @param selection the selection
	 */
	public EntityInstanceModel(final EntitySelection selection) {
		ParameterUtil.ensureNotNull(selection, "selection");
		this.selection = selection;
	}

	/**
	 * Getter method for the selection.
	 * @return the selection
	 */
	public EntitySelection getSelection() {
		return selection;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
		selection.getEntityModel().detach();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	@Override
	public IEntityInstance getObject() {
		throw new RuntimeException("this code should not be reachable");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
	 */
	@Override
	public void setObject(final IEntityInstance object) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This class throws a {@link NoSuchElementException} if the entity
	 * instance cannot be found.
	 */
	public static final class Required extends EntityInstanceModel {

		/**
		 * Constructor.
		 * @param selection the selection
		 */
		public Required(final EntitySelection selection) {
			super(selection);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.admin.entity.EntityInstanceModel#getObject()
		 */
		@Override
		public IEntityInstance getObject() {
			return selection.fetchSingleInstance(false);
		}
	}

	/**
	 * This class returns null if the entity instance cannot be found.
	 */
	public static final class Optional extends EntityInstanceModel {

		/**
		 * Constructor.
		 * @param selection the selection
		 */
		public Optional(final EntitySelection selection) {
			super(selection);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.admin.entity.EntityInstanceModel#getObject()
		 */
		@Override
		public IEntityInstance getObject() {
			return selection.fetchSingleInstance(true);
		}

	}

}
