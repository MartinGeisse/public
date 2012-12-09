/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide;

import name.martingeisse.webide.ssh.ShellFactory;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;

/**
 * The main class.
 */
public class Main {

	/**
	 * The main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {
		final SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(9001);
		final PasswordAuthenticator auth = new PasswordAuthenticator() {
			@Override
			public boolean authenticate(final String username, final String password, final ServerSession session) {
				return true;
			}
		};
		sshd.setPasswordAuthenticator(auth);
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("hostkey.ser"));
		sshd.setShellFactory(new ShellFactory());
		sshd.start();
	}

}
