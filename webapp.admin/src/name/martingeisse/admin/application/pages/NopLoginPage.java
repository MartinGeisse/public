/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.pages;

import name.martingeisse.wicket.security.SecurityUtil;
import name.martingeisse.wicket.security.credentials.EmptyCredentials;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.StatelessForm;

/**
 * Default login page. This page does not ask the user for any
 * credentials. Instead it just has a button that logs the user
 * in. Using the static bypass flag it is also possible to
 * skip this page and login automatically.
 */
public class NopLoginPage extends WebPage {

	/**
	 * Bypass this page and login automatically.
	 */
	public static boolean bypass = false;
	
	/**
	 * Constructor.
	 */
	public NopLoginPage() {
		add(new LoginForm("form"));
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Page#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if (bypass) {
			login();
		}
	}
	
	/**
	 * This method performs the login and the redirect.
	 */
	void login() {
		
		// login and continue
		SecurityUtil.login(new EmptyCredentials());
		continueToOriginalDestination();
		
		// in case no original destination is available, go to the home page
    	setResponsePage(getApplication().getHomePage());
    	
	}
	
	/**
	 * Login form implementation.
	 */
	private class LoginForm extends StatelessForm<Void> {

		/**
		 * Constructor.
		 * @param id the wicket id
		 */
		public LoginForm(String id) {
			super(id);
		}
		
		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		protected void onSubmit() {
			super.onSubmit();
			login();
		}
		
	}
}
