/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.sql.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;

import com.mysema.codegen.CodeWriter;
import com.mysema.query.codegen.Serializer;

/**
 * Base implementation for {@link Serializer}.
 */
public abstract class AbstractSerializer implements Serializer {

	/**
	 * the fileComment
	 */
	private String fileComment = "This file was generated from the database schema.";

	/**
	 * Constructor.
	 */
	public AbstractSerializer() {
	}

	/**
	 * Getter method for the fileComment.
	 * @return the fileComment
	 */
	public String getFileComment() {
		return fileComment;
	}

	/**
	 * Setter method for the fileComment.
	 * @param fileComment the fileComment to set
	 */
	public void setFileComment(String fileComment) {
		this.fileComment = fileComment;
	}
	
	/**
	 * Prints the file comment.
	 * @param w the writer to print to
	 * @throws IOException on I/O errors
	 */
	protected void printFileComment(CodeWriter w) throws IOException {
		w.javadoc(fileComment);
	}

	/**
	 * Prints multiple imports from a collection
	 * @param w the writer to print to
	 * @param imports the imports to print
	 * @throws IOException on I/O errors
	 */
	protected void printImports(CodeWriter w, Collection<String> imports) throws IOException {
		w.importClasses(imports.toArray(new String[imports.size()]));
	}

	/**
	 * Prints multiple package-imports from a collection
	 * @param w the writer to print to
	 * @param imports the imports to print
	 * @throws IOException on I/O errors
	 */
	protected void printPackageImports(CodeWriter w, Collection<String> imports) throws IOException {
		w.importPackages(imports.toArray(new String[imports.size()]));
	}
	
	/**
	 * Prints multiple static-imports from a collection
	 * @param w the writer to print to
	 * @param imports the imports to print
	 * @throws IOException on I/O errors
	 */
	protected void printStaticImports(CodeWriter w, Collection<Class<?>> imports) throws IOException {
		w.staticimports(imports.toArray(new Class<?>[imports.size()]));
	}
	
	/**
	 * Adds an element to the collection if the condition is true.
	 */
	protected <T> void addIf(Collection<T> collection, T element, boolean condition) {
		if (condition) {
			collection.add(element);
		}
	}

	/**
	 * Prints the specified annotations.
	 */
	protected void printAnnotations(final Collection<Annotation> annotations, final CodeWriter w) throws IOException {
		for (final Annotation annotation : annotations) {
			w.annotation(annotation);
		}
	}
	
}
