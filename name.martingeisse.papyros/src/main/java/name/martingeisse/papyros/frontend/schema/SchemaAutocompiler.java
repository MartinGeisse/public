/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.papyros.frontend.schema;

import java.io.Serializable;
import name.martingeisse.common.javascript.ownjson.parser.JsonParser;
import name.martingeisse.common.javascript.ownjson.parserbase.JsonSyntaxException;
import name.martingeisse.wicket.component.codemirror.compile.CompilerErrorLevel;
import name.martingeisse.wicket.component.codemirror.compile.CompilerMarker;
import name.martingeisse.wicket.component.codemirror.compile.CompilerResult;
import name.martingeisse.wicket.component.codemirror.compile.ICompiler;

/**
 * Used by the template editor for auto-compilation and adding error markers.
 */
public final class SchemaAutocompiler implements ICompiler, Serializable {

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.codemirror.compile.ICompiler#compile(java.lang.String, name.martingeisse.wicket.component.codemirror.compile.CompilerResult)
	 */
	@Override
	public void compile(String document, CompilerResult result) throws Exception {
		JsonParser parser = new JsonParser(document);
		try {
			parser.parseValue();
		} catch (JsonSyntaxException e) {
			result.getMarkers().add(new CompilerMarker(e.getStartLine(), e.getStartColumn(), e.getEndLine(), e.getEndColumn(), CompilerErrorLevel.ERROR, e.getRawMessage()));
		}
		
	}
	
}
