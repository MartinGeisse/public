/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.papyros.frontend.template;

import java.io.Serializable;
import name.martingeisse.wicket.component.codemirror.compile.CompilerErrorLevel;
import name.martingeisse.wicket.component.codemirror.compile.CompilerMarker;
import name.martingeisse.wicket.component.codemirror.compile.CompilerResult;
import name.martingeisse.wicket.component.codemirror.compile.ICompiler;

/**
 * Used by the template editor for auto-compilation and adding error markers.
 */
public final class TemplateAutocompiler implements ICompiler, Serializable {

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.codemirror.compile.ICompiler#compile(java.lang.String)
	 */
	@Override
	public CompilerResult compile(String document) {
		CompilerResult result = new CompilerResult();
		result.getMarkers().add(new CompilerMarker(0, 0, 0, 1, CompilerErrorLevel.ERROR, "foo"));
		return result;
	}
	
}
