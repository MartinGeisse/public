/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ucademy.application.specialpage;

import name.martingeisse.ucademy.application.page.AbstractApplicationPage;
import name.martingeisse.ucademy.application.tools.CassandraReset;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This page allows to reset the Cassandra DB
 */
public class CassandraResetPage extends AbstractApplicationPage {

	// TODO remove hardcoded key
	private static final String CORRECT_KEY = "wigohiowejgeoiwjigoegewpoijgwiejgoeiwnbiovnweoeiwnow";
	
	/**
	 * the message
	 */
	private String message = "";
	
	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public CassandraResetPage(PageParameters pageParameters) {
		Form<?> form = new Form<Void>("form");
		add(form);
		form.add(new Button("button") {
			@Override
			public void onSubmit() {
				String key = getPageParameters().get("key").toString();
				if (key == null || !key.equals(CORRECT_KEY)) {
					message = "wrong key";
				} else {
					try {
						new CassandraReset().run();
						message = "database reset ok";
					} catch (Exception e) {
						message = "exception";
					}
				}
				
			};
		});
		add(new Label("message", new PropertyModel<>(this, "message")));
	}

	/**
	 * Getter method for the message.
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
}
