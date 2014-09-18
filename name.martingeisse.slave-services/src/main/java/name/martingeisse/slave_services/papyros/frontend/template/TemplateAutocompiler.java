/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.papyros.frontend.template;

import java.io.LineNumberReader;
import java.io.Serializable;
import java.io.StringReader;

import name.martingeisse.wicket.component.codemirror.compile.CompilerErrorLevel;
import name.martingeisse.wicket.component.codemirror.compile.CompilerMarker;
import name.martingeisse.wicket.component.codemirror.compile.CompilerResult;
import name.martingeisse.wicket.component.codemirror.compile.ICompiler;

/**
 * Used by the template editor for auto-compilation and adding error markers.
 */
public final class TemplateAutocompiler implements ICompiler, Serializable {

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.codemirror.compile.ICompiler#compile(java.lang.String, name.martingeisse.wicket.component.codemirror.compile.CompilerResult)
	 */
	@Override
	public void compile(String document, CompilerResult result) throws Exception {
		int currentLineNumber = 0;
		LineNumberReader r = new LineNumberReader(new StringReader(document));
		while (true) {
			String line = r.readLine();
			if (line == null) {
				break;
			}
			currentLineNumber = r.getLineNumber() - 1;
			
			// ----- TODO fake compiler
			{
				int index = line.indexOf('!');
				if (index != -1) {
					throw new Exception("bla");
				}
			}
			{
				int position = 0;
				while (true) {
					int index1 = line.indexOf('(', position);
					if (index1 == -1) {
						break;
					}
					int index2 = line.indexOf(')', index1);
					if (index2 == -1) {
						result.getMarkers().add(new CompilerMarker(currentLineNumber, index1, currentLineNumber, line.length(), CompilerErrorLevel.WARNING, "foo"));
						break;
					} else {
						result.getMarkers().add(new CompilerMarker(currentLineNumber, index1, currentLineNumber, index2 + 1, CompilerErrorLevel.WARNING, "foo"));
						position = index2 + 1;
					}
				}
			}
			// ---
			{
				int index = line.indexOf('!');
				if (index != -1) {
					throw new Exception("bla");
				}
			}
			{
				int position = 0;
				while (true) {
					int index1 = line.indexOf('{', position);
					if (index1 == -1) {
						break;
					}
					int index2 = line.indexOf('}', index1);
					if (index2 == -1) {
						result.getMarkers().add(new CompilerMarker(currentLineNumber, index1, currentLineNumber, line.length(), CompilerErrorLevel.ERROR, "bar"));
						break;
					} else {
						result.getMarkers().add(new CompilerMarker(currentLineNumber, index1, currentLineNumber, index2 + 1, CompilerErrorLevel.ERROR, "bar"));
						position = index2 + 1;
					}
				}
			}
			// ----- TODO fake compiler
			
		}
	}
	
}
