/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.repltest;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.BinaryLogClient.EventListener;
import com.github.shyiko.mysql.binlog.event.Event;

/**
 * Hello world!
 */
public class Main {

	/**
	 * The main method.
	 * @param args ignored
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {
		final BinaryLogClient client = new BinaryLogClient("localhost", 3306, "ucademy", "username", "password");
		client.registerEventListener(new EventListener() {
			@Override
			public void onEvent(final Event event) {
				System.out.println("event: " + event);
			}
		});
		client.connect();
	}

}
