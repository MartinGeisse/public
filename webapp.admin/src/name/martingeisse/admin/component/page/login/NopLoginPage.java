/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.page.login;

import name.martingeisse.admin.application.security.SecurityUtil;
import name.martingeisse.admin.application.security.credentials.EmptyCredentials;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.request.flow.RedirectToUrlException;

/**
 * Default login page. This page does not ask the user for any
 * credentials. Instead it just has a button that logs the user
 * in.
 */
public class NopLoginPage extends WebPage {

	/**
	 * Constructor.
	 */
	public NopLoginPage() {
		add(new LoginForm("form"));
	}
	
	/**
	 * Login form implementation.
	 */
	private static class LoginForm extends StatelessForm<Void> {

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
			SecurityUtil.login(new EmptyCredentials());
			throw new RedirectToUrlException("/");
		}
		
	}
}
