/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages;

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
	 * Constructor. First tries to invoke the one-argument constructor of
	 * the panel class with the Wicket id. If that constructor cannot be
	 * found, then tries the two-argument constructor (Wicket id and
	 * model) by passing null for the model.
	 * @param panelClass the panel class to instantiate
	 */
	public PanelPage(Class<? extends Panel> panelClass) {
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
	public PanelPage(Class<? extends Panel> panelClass, IModel<?> model) {
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
	public PanelPage(Class<? extends Panel> panelClass, IModel<?> model, boolean modelIsOptional) {
		createPanel(panelClass, model, modelIsOptional, false);
	}

	/**
	 * 
	 */
	private void createPanel(Class<? extends Panel> panelClass, IModel<?> model, boolean useOneArgument, boolean useOneArgumentFirst) {
		try {
			
			// try one-arg before two-arg?
			if (useOneArgument && useOneArgumentFirst) {
				try {
					panelClass.getConstructor(String.class).newInstance(WICKET_ID);
				} catch (NoSuchMethodException e) {
				}
			}
			
			// try two-arg
			try {
				panelClass.getConstructor(String.class, IModel.class).newInstance(WICKET_ID, model);
			} catch (NoSuchMethodException e) {
			}
			
			// try one-arg after two-arg?
			if (useOneArgument && !useOneArgumentFirst) {
				try {
					panelClass.getConstructor(String.class).newInstance(WICKET_ID);
				} catch (NoSuchMethodException e) {
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		// error: no such constructor, give detailed information
		StringBuilder builder = new StringBuilder();
		builder.append("no suitable panel constructor found for panel class ").append(panelClass.getCanonicalName());
		builder.append(", one-arg constructor accepted: ").append(useOneArgument);
		builder.append(", one-arg constructor preferred: ").append(useOneArgumentFirst);
		builder.append(". Constructors found in panel class: ");
		try {
			for (Constructor<?> constructor : panelClass.getConstructors()) {
				builder.append("\n* ").append(panelClass.getName()).append('(');
				boolean first = true;
				for (Class<?> parameterType : constructor.getParameterTypes()) {
					if (first) {
						first = false;
					} else {
						builder.append(", ");
					}
					builder.append(parameterType.getCanonicalName());
				}
				builder.append(')');
			}
		} catch (Exception e) {
			builder.append("*** exception while obtaining constructor list ***");
		}
		throw new RuntimeException(builder.toString());
	}
	
	/**
	 * Constructor.
	 * @param panel the panel to wrap in a page
	 */
	public PanelPage(Panel panel) {
		add(panel);
	}
	
}
