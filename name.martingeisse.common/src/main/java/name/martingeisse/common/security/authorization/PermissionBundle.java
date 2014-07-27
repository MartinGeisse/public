/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security.authorization;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A simple class that bundles a set of {@link IPermissions} objects.
 * Having the bundle counts as having *all* the permissions in that set.
 * 
 * The security framework intentionally does not provide special handling
 * for such bundles. In particular, the authorization strategy must
 * deal with bundles explicitly; the framework won't handle this. The reason
 * is that a permission request may be passed that is not satisfied by
 * any permission in the bundle individually, but *is* satisfied when the
 * whole bundle is provided at once. An application that uses bundles should
 * provide an authorization strategy that can handle them.
 */
public final class PermissionBundle implements IPermissions, Iterable<IPermissions>, Serializable {

	/**
	 * the elements
	 */
	private IPermissions[] elements;
	
	/**
	 * Constructor.
	 * @param elements the elements
	 */
	public PermissionBundle(IPermissions... elements) {
		this.elements = elements.clone();
	}

	/**
	 * @return the number of elements
	 */
	public int getElementCount() {
		return elements.length;
	}

	/**
	 * Returns an element.
	 * @param i the index
	 * @return the element
	 */
	public IPermissions getElement(int i) {
		return elements[i];
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Iterator<IPermissions> iterator() {
		return new Iterator<IPermissions>() {

			private int i = 0;
			
			/* (non-Javadoc)
			 * @see java.util.Iterator#hasNext()
			 */
			@Override
			public boolean hasNext() {
				return (i < elements.length);
			}

			/* (non-Javadoc)
			 * @see java.util.Iterator#next()
			 */
			@Override
			public IPermissions next() {
				if (i == elements.length) {
					throw new NoSuchElementException();
				}
				i++;
				return elements[i - 1];
			}

			/* (non-Javadoc)
			 * @see java.util.Iterator#remove()
			 */
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		boolean first = true;
		for (IPermissions element : elements) {
			if (first) {
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append(element);
		}
		builder.append("]");
		return builder.toString();
	}
	
}
