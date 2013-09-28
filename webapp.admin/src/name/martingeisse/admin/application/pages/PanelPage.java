/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.pages;

import java.lang.reflect.Constructor;

import name.martingeisse.common.util.ParameterUtil;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Adapter to use a {@link Panel} as a page. The page includes the
 * usual decoration for admin application pages.
 * 
 * This class creates a nested panel from a panel class. It attempts
 * to invoke both the one-arg constructor (Wicket id only) and the
 * two-arg constructor (Wicket id and model), as specified in the
 * constructor comments of this class.
 */
public class PanelPage extends AbstractAdminPage {

	/**
	 * the panelClass
	 */
	private Class<? extends Panel> panelClass;

	/**
	 * the model
	 */
	private IModel<?> model;

	/**
	 * the allowModelLessConstructor
	 */
	private boolean allowModelLessConstructor;
	
	/**
	 * the preferModelLessConstructor
	 */
	private boolean preferModelLessConstructor;

	/**
	 * Constructor. First tries to invoke the one-argument constructor of
	 * the panel class with the Wicket id. If that constructor cannot be
	 * found, then tries the two-argument constructor (Wicket id and
	 * model) by passing null for the model.
	 * 
	 * @param panelClass the panel class to instantiate
	 */
	public PanelPage(final Class<? extends Panel> panelClass) {
		this(panelClass, null, true, true);
	}

	/**
	 * Constructor. Only tries to invoke the two-argument constructor of
	 * the panel class (Wicket id and model). This method is useful to
	 * pass a model and consider it a bug if the panel does not accept
	 * a model. This constructor equivalent to PanelPage(panelClass, model, false, false).
	 * 
	 * @param panelClass the panel class to instantiate
	 * @param model the model
	 */
	public PanelPage(final Class<? extends Panel> panelClass, final IModel<?> model) {
		this(panelClass, model, false, false);
	}

	/**
	 * Constructor. Tries the two-argument constructor (Wicket id and model) and optionally
	 * the one-argument constructor (Wicket id only, ignoring the model) of the panel class
	 * to create the panel.
	 * 
	 * If allowModelLessConstructor is false, then this method tries only the two-argument constructor.
	 * 
	 * Otherwise, if preferModelLessConstructor is false, then it first tries the two-argument
	 * constructor, then the one-argument constructor.
	 * 
	 * Otherwise, it first tries the one-argument constructor, then the two-argument constructor.
	 * 
	 * @param panelClass the panel class to instantiate
	 * @param model the model
	 * @param allowModelLessConstructor whether the one-argument constructor is used as a fallback
	 * @param preferModelLessConstructor whether the one-argument constructor is preferred (if present)
	 * over the two-argument constructor.
	 */
	public PanelPage(final Class<? extends Panel> panelClass, final IModel<?> model, final boolean allowModelLessConstructor, final boolean preferModelLessConstructor) {
		ParameterUtil.ensureNotNull(panelClass, "panelClass");
		this.panelClass = panelClass;
		this.model = model;
		this.allowModelLessConstructor = allowModelLessConstructor;
		this.preferModelLessConstructor = preferModelLessConstructor;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.pages.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		try {

			// try one-arg before two-arg?
			if (allowModelLessConstructor && preferModelLessConstructor) {
				try {
					getMainContainer().add(panelClass.getConstructor(String.class).newInstance("panel"));
					return;
				} catch (final NoSuchMethodException e) {
				}
			}

			// try two-arg
			try {
				getMainContainer().add(panelClass.getConstructor(String.class, IModel.class).newInstance("panel", model));
				return;
			} catch (final NoSuchMethodException e) {
			}

			// try one-arg after two-arg?
			if (allowModelLessConstructor && !preferModelLessConstructor) {
				try {
					getMainContainer().add(panelClass.getConstructor(String.class).newInstance("panel"));
					return;
				} catch (final NoSuchMethodException e) {
				}
			}

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

		// error: no such constructor, give detailed information
		final StringBuilder builder = new StringBuilder();
		builder.append("no suitable panel constructor found for panel class ").append(panelClass.getCanonicalName());
		builder.append(", one-arg constructor accepted: ").append(allowModelLessConstructor);
		builder.append(", one-arg constructor preferred: ").append(preferModelLessConstructor);
		builder.append(". Constructors found in panel class: ");
		try {
			for (final Constructor<?> constructor : panelClass.getConstructors()) {
				builder.append("\n* ").append(panelClass.getName()).append('(');
				boolean first = true;
				for (final Class<?> parameterType : constructor.getParameterTypes()) {
					if (first) {
						first = false;
					} else {
						builder.append(", ");
					}
					builder.append(parameterType.getCanonicalName());
				}
				builder.append(')');
			}
		} catch (final Exception e) {
			builder.append("*** exception while obtaining constructor list ***");
		}
		throw new RuntimeException(builder.toString());
	}

	/**
	 * Constructor.
	 * @param panel the panel to wrap in a page
	 */
	public PanelPage(final Panel panel) {
		add(panel);
	}

}
