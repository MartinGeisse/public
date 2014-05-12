/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model;

import name.martingeisse.common.terms.DisplayName;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * This is a read-only model that wraps an inner model which returns either a string
 * (a class name) or a class object. This model then returns a display name for
 * that class, which is:
 * - null if the inner model returns null
 * - the inner model value if the inner model returns an unknown class name
 * - the DisplayName value if the inner model returns a class object or known class name and that class has such an annotation
 * - the canonical class name if the inner model returns a class object or known class name and that class has no DisplayName annotation
 */
public class ClassDisplayNameModel extends AbstractReadOnlyModel<String> {

	/**
	 * the inner
	 */
	private IModel<?> inner;
	
	/**
	 * Constructor.
	 */
	private ClassDisplayNameModel(IModel<?> inner) {
		this.inner = inner;
	}
	
	/**
	 * Creates a new instance that returns the display name of a class from the specified model.
	 * @param classModel the model that provides the class
	 * @return the display name model
	 */
	public static ClassDisplayNameModel fromClassModel(IModel<Class<?>> classModel) {
		return new ClassDisplayNameModel(classModel);
	}
	
	/**
	 * Creates a new instance that returns the display name of a class with the name taken
	 * from the specified model.
	 * @param classNameModel the model that provides the class name
	 * @return the display name model
	 */
	public static ClassDisplayNameModel fromClassNameModel(IModel<String> classNameModel) {
		return new ClassDisplayNameModel(classNameModel);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
	public String getObject() {
		Object o = inner.getObject();
		if (o == null) {
			return null;
		} else if (o instanceof Class<?>) {
			return getObject((Class<?>)o);
		} else if (o instanceof String) {
			String s = (String)o;
			try {
				return getObject(Class.forName(s));
			} catch (ClassNotFoundException e) {
				return s;
			}
		} else {
			throw new RuntimeException("inner model of ClassDisplayNameModel returned invalid object: " + o);
		}
	}

	/**
	 * @param c
	 * @return
	 */
	private static String getObject(Class<?> c) {
		DisplayName annotation = c.getAnnotation(DisplayName.class);
		return (annotation != null ? annotation.value() : c.getCanonicalName());
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#detach()
	 */
	@Override
	public void detach() {
		inner.detach();
		super.detach();
	}
	
}
