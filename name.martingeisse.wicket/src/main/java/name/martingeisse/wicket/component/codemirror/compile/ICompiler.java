/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.wicket.component.codemirror.compile;

import org.apache.log4j.Logger;

/**
 * Compiles the document from a CodeMirror instance.
 */
public interface ICompiler {

	/**
	 * Compiles a document and stores information about the compilation
	 * in the specified result object.
	 * 
	 * @param document the document
	 * @param result the result object
	 * @throws Exception on errors
	 */
	public void compile(String document, CompilerResult result) throws Exception;

	/**
	 * Static helper methods to deal with {@link ICompiler} implementation.
	 */
	public static class Util {
		
		/**
		 * the logger
		 */
		private static Logger logger = Logger.getLogger(ICompiler.Util.class);
		
		/**
		 * Compiles the specified document "safely", catching exceptions and
		 * returning the result.
		 * 
		 * @param compiler the compiler
		 * @param document the document
		 * @return the compiler result
		 */
		public static CompilerResult compileSafe(ICompiler compiler, String document) {
			final CompilerResult result = new CompilerResult(document);
			try {
				compiler.compile(document, result);
			} catch (Exception e) {
				logger.error("compiler exception", e);
				result.getMarkers().add(new CompilerMarker(0, 0, 0, 0, CompilerErrorLevel.ERROR, "internal exception: " + e.getMessage()));
			}
			return result;
		}
		
	}
}
