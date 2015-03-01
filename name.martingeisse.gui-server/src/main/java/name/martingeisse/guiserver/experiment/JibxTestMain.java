/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.experiment;

import java.io.InputStream;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;

/**
 * TODO: document me
 *
 */
public class JibxTestMain {

	public static void main(String[] args) throws Exception {
		IBindingFactory bindingFactory = BindingDirectory.getFactory(Customer.class);
		IUnmarshallingContext context = bindingFactory.createUnmarshallingContext();
		Customer customer;
		try (InputStream in = JibxTestMain.class.getResourceAsStream("example.xml")) {
			customer = (Customer)context.unmarshalDocument(in, null);
		}
		System.out.println(customer.person.firstName + " " + customer.person.lastName);
	}
}
