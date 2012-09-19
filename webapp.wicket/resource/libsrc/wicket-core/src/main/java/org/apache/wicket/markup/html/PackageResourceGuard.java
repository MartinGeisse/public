/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup.html;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Application;
import org.apache.wicket.util.lang.Packages;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default implementation of {@link IPackageResourceGuard}. By default, the extensions 'properties',
 * 'class' and 'java' are blocked and files like 'log4j.xml' and 'applicationContext.xml'
 * 
 * A more secure implementation which by default denies access to any resource is
 * {@link SecurePackageResourceGuard}
 * 
 * @author eelcohillenius
 */
public class PackageResourceGuard implements IPackageResourceGuard
{
	/** Log. */
	private static final Logger log = LoggerFactory.getLogger(PackageResourceGuard.class);

	/** Set of extensions that are denied access. */
	private Set<String> blockedExtensions = new HashSet<String>(4);

	/** Set of filenames that are denied access. */
	private Set<String> blockedFiles = new HashSet<String>(4);

	private boolean allowAccessToRootResources = false;

	/**
	 * Construct.
	 */
	public PackageResourceGuard()
	{
		blockedExtensions.add("properties");
		blockedExtensions.add("class");
		blockedExtensions.add("java");

		blockedFiles.add("applicationContext.xml");
		blockedFiles.add("log4j.xml");
	}

	/**
	 * @see org.apache.wicket.markup.html.IPackageResourceGuard#accept(java.lang.Class,
	 *      java.lang.String)
	 */
	@Override
	public boolean accept(Class<?> scope, String path)
	{
		String absolutePath = Packages.absolutePath(scope, path);
		return acceptAbsolutePath(absolutePath);
	}

	/**
	 * Whether the provided absolute path is accepted.
	 * 
	 * @param path
	 *            The absolute path, starting from the class root (packages are separated with
	 *            forward slashes instead of dots).
	 * @return True if accepted, false otherwise.
	 */
	protected boolean acceptAbsolutePath(String path)
	{
		int ixExtension = path.lastIndexOf('.');
		int len = path.length();
		final String ext;
		if (ixExtension <= 0 || ixExtension == len || (path.lastIndexOf('/') + 1) == ixExtension)
		{
			ext = null;
		}
		else
		{
			ext = path.substring(ixExtension + 1).toLowerCase().trim();
		}

		if ("html".equals(ext) &&
			getClass().getClassLoader().getResource(path.replaceAll("\\.html", ".class")) != null)
		{
			log.warn("Access denied to shared (static) resource because it is a Wicket markup file: " +
				path);
			return false;
		}

		if (acceptExtension(ext) == false)
		{
			log.warn("Access denied to shared (static) resource because of the file extension: " +
				path);
			return false;
		}

		String filename = Strings.lastPathComponent(path, '/');
		if (acceptFile(filename) == false)
		{
			log.warn("Access denied to shared (static) resource because of the file name: " + path);
			return false;
		}

		// Only if a placeholder, e.g. $up$ is defined, access to parent directories is allowed
		if (Strings.isEmpty(Application.get().getResourceSettings().getParentFolderPlaceholder()))
		{
			if (path.contains(".."))
			{
				log.warn("Access to parent directories via '..' is by default disabled for shared resources: " +
					path);
				return false;
			}
		}

		if (!allowAccessToRootResources)
		{
			String absolute = path;
			if (absolute.startsWith("/"))
			{
				absolute = absolute.substring(1);
			}
			if (!absolute.contains("/"))
			{
				log.warn("Access to root directory is by default disabled for shared resources: " +
					path);
				return false;
			}
		}

		return true;
	}

	/**
	 * Whether the provided extension is accepted.
	 * 
	 * @param extension
	 *            The extension, starting from the class root (packages are separated with forward
	 *            slashes instead of dots).
	 * @return True if accepted, false otherwise.
	 */
	protected boolean acceptExtension(String extension)
	{
		return (!blockedExtensions.contains(extension));
	}

	/**
	 * Whether the provided filename is accepted.
	 * 
	 * @param file
	 *            filename
	 * @return True if accepted, false otherwise.
	 */
	protected boolean acceptFile(String file)
	{
		if (file != null)
		{
			file = file.trim();
		}
		return (!blockedFiles.contains(file));
	}

	/**
	 * Gets the set of extensions that are denied access.
	 * 
	 * @return The set of extensions that are denied access
	 */
	protected final Set<String> getBlockedExtensions()
	{
		return blockedExtensions;
	}

	/**
	 * Gets the set of extensions that are denied access.
	 * 
	 * @return The set of extensions that are denied access
	 */
	protected final Set<String> getBlockedFiles()
	{
		return blockedFiles;
	}

	/**
	 * Sets the set of extensions that are denied access.
	 * 
	 * @param blockedExtensions
	 *            Set of extensions that are denied access
	 */
	protected final void setBlockedExtensions(Set<String> blockedExtensions)
	{
		this.blockedExtensions = blockedExtensions;
	}

	/**
	 * Sets the set of filenames that are denied access.
	 * 
	 * @param blockedFiles
	 *            Set of extensions that are denied access
	 */
	protected final void setBlockedFiles(Set<String> blockedFiles)
	{
		this.blockedFiles = blockedFiles;
	}

	/**
	 * Checks whether or not resources in the web root folder can be access.
	 * 
	 * @return {@code true} iff root resources can be accessed
	 */
	public final boolean isAllowAccessToRootResources()
	{
		return allowAccessToRootResources;
	}

	/**
	 * Sets whether or not resources in the web root folder can be accessed.
	 * 
	 * @param allowAccessToRootResources
	 */
	public final void setAllowAccessToRootResources(boolean allowAccessToRootResources)
	{
		this.allowAccessToRootResources = allowAccessToRootResources;
	}
}
