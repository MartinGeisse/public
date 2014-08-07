/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.wicket.component.codemirror.compile;

/**
 * Compiles the document from a CodeMirror instance.
 */
public interface ICompiler {

	/**
	 * Compiles a document.
	 * 
	 * @param document the document
	 * @return the compiler result
	 */
	public CompilerResult compile(String document);
	
}
