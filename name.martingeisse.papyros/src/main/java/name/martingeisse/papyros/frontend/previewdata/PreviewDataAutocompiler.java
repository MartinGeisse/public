/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.papyros.frontend.previewdata;

import java.io.Serializable;
import name.martingeisse.wicket.component.codemirror.compile.CompilerResult;
import name.martingeisse.wicket.component.codemirror.compile.ICompiler;

/**
 * Used by the template editor for auto-compilation and adding error markers.
 */
public final class PreviewDataAutocompiler implements ICompiler, Serializable {

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.codemirror.compile.ICompiler#compile(java.lang.String, name.martingeisse.wicket.component.codemirror.compile.CompilerResult)
	 */
	@Override
	public void compile(String document, CompilerResult result) throws Exception {
	}
	
}
