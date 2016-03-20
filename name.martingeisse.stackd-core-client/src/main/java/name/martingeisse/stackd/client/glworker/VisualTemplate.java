/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.glworker;

/**
 * A template that can be rendered to represent objects of type T.
 * Rendering will be done by passing work units to a GL worker and may
 * be affected by the current OpenGL state. Especially, there is no
 * position / orientation information being passed to the template --
 * prepare appropriate OpenGL transformations for that.
 */
public interface VisualTemplate<T> {

	/**
	 * Renders the specified subject using this template.
	 * 
	 * This method must be called from the application thread.
	 * 
	 * @param subject the subject to render
	 * @param worker the worker that consumes GL work units
	 */
	public void render(T subject, GlWorkerLoop worker);
	
}
