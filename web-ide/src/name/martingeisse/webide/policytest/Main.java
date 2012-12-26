/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.policytest;

import java.io.File;
import java.io.FileDescriptor;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;

/**
 * TODO: this is just a test. remove.
 */
public class Main {

	public static void main(String[] args) {
		Policy.setPolicy(new MyPolicy());
		System.setSecurityManager(new SecurityManager());
		new File("test").exists();
		
		// Policy.setPolicy(new MyPolicy());
		System.setSecurityManager(new SecurityManager());
		new File("test").exists();
	}
	
	private static class MyPolicy extends Policy {
		
		/* (non-Javadoc)
		 * @see java.security.Policy#getPermissions(java.security.CodeSource)
		 */
		@Override
		public PermissionCollection getPermissions(CodeSource codesource) {
			System.out.println("getPermissions(): " + codesource);
			return super.getPermissions(codesource);
		}

		/* (non-Javadoc)
		 * @see java.security.Policy#getPermissions(java.security.ProtectionDomain)
		 */
		@Override
		public PermissionCollection getPermissions(ProtectionDomain domain) {
			System.out.println("getPermissions(): " + domain);
			return super.getPermissions(domain);
		}
		
		/* (non-Javadoc)
		 * @see java.security.Policy#implies(java.security.ProtectionDomain, java.security.Permission)
		 */
		@Override
		public boolean implies(ProtectionDomain domain, Permission permission) {
			// System.out.println("implies(): " + domain + ", " + permission);
			return super.implies(domain, permission);
		}
		
	}
	
}
