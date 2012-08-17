/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.page;

import java.lang.reflect.Constructor;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Adapter to use a {@link Panel} as a page. The page includes the
 * usual decoration for admin application pages.
 * 
 * This class can either wrap an existing panel or create a new one
 * from a panel class. When using an existing panel, that panel must
 * use the Wicket id specified by the WICKET_ID constant. When creating
 * a new panel instance, this class attempts to invoke both the
 * one-arg constructor (Wicket id only) and the two-arg constructor
 * (Wicket id and model), as specified in the constructor comments
 * of this class.
 */
public class PanelPage extends AbstractAdminPage {

	/**
	 * This wicket ID must be used for the panel when creating an instance
	 * of this class from an existing panel.
	 */
	public static final String WICKET_ID = "panel";

	/**
	 * the panelClass
	 */
	private Class<? extends Panel> panelClass;

	/**
	 * the model
	 */
	private IModel<?> model;

	/**
	 * the modelIsOptional
	 */
	private boolean modelIsOptional;

	/**
	 * Constructor. First tries to invoke the one-argument constructor of
	 * the panel class with the Wicket id. If that constructor cannot be
	 * found, then tries the two-argument constructor (Wicket id and
	 * model) by passing null for the model.
	 * @param panelClass the panel class to instantiate
	 */
	public PanelPage(final Class<? extends Panel> panelClass) {
		createPanel(panelClass, null, true, true);
	}

	/**
	 * Constructor. Only tries to invoke the two-argument constructor of
	 * the panel class (Wicket id and model). This method is useful to
	 * pass a model and consider it a bug if the panel does not accept
	 * a model. This constructor equivalent to PanelPage(panelClass, model, false).
	 * @param panelClass the panel class to instantiate
	 * @param model the model
	 */
	public PanelPage(final Class<? extends Panel> panelClass, final IModel<?> model) {
		this(panelClass, model, false);
	}

	/**
	 * Constructor. First tries to invoke the two-argument constructor of
	 * the panel class (Wicket id and model). If that constructor cannot be
	 * found and modelIsOptional is true, then tries the one-argument
	 * constructor (Wicket id only), ignoring the model. Ignoring the model
	 * is useful when the caller of this constructor can handle various
	 * panel classes and does not require them to accept a model.
	 * @param panelClass the panel class to instantiate
	 * @param model the model
	 * @param modelIsOptional whether the one-argument constructor is used as a fallback
	 */
	public PanelPage(final Class<? extends Panel> panelClass, final IModel<?> model, final boolean modelIsOptional) {
		this.panelClass = panelClass;
		this.model = model;
		this.modelIsOptional = modelIsOptional;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.pages.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		createPanel(panelClass, model, modelIsOptional, false);
	}

	/**
	 * 
	 */
	private void createPanel(final Class<? extends Panel> panelClass, final IModel<?> model, final boolean useOneArgument, final boolean useOneArgumentFirst) {
		try {

			// try one-arg before two-arg?
			if (useOneArgument && useOneArgumentFirst) {
				try {
					getMainContainer().add(panelClass.getConstructor(String.class).newInstance(WICKET_ID));
					return;
				} catch (final NoSuchMethodException e) {
				}
			}

			// try two-arg
			try {
				getMainContainer().add(panelClass.getConstructor(String.class, IModel.class).newInstance(WICKET_ID, model));
				return;
			} catch (final NoSuchMethodException e) {
			}

			// try one-arg after two-arg?
			if (useOneArgument && !useOneArgumentFirst) {
				try {
					getMainContainer().add(panelClass.getConstructor(String.class).newInstance(WICKET_ID));
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
		builder.append(", one-arg constructor accepted: ").append(useOneArgument);
		builder.append(", one-arg constructor preferred: ").append(useOneArgumentFirst);
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
