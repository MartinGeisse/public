/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.build;

import java.util.Set;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;

/**
 * This interface is implemented by builders from plugins. Its most important methods
 * are the build methods. They take the build descriptor (which is specified in the
 * build.json file that invokes the builder, and passed to this builder as a
 * {@link JsonAnalyzer}) and, if incremental, a set of resource deltas that triggered
 * the build. The task of the builder is to perform workspace modifications.
 * 
 * Note about deep resource deltas: Since these often occur when a folder gets
 * deleted, and the resource are gone then, the builder thread does not try to
 * resolve which resources were affected. Instead, deep deltas are simply passed
 * to the builder, assuming that the builder knows best how to deal with them.
 */
public interface IBuilder {

	// TODO: unused. cleans build artifacts
	// public void clean(ResourcePath buildscriptPath);
	
	// TODO: unused. build after clean
	// public void cleanBuild(ResourcePath buildscriptPath);

	/**
	 * Performs an incremental build in response to a set of resource deltas.
	 * 
	 * @param workspaceId the ID of the workspace in which the build occurs
	 * @param descriptorAnalyzer the analyzer for the build descriptor
	 * @param deltas the deltas that triggered the build
	 */
	public void incrementalBuild(long workspaceId, JsonAnalyzer descriptorAnalyzer, Set<BuilderResourceDelta> deltas);

}
