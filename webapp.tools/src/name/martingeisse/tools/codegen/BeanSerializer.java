/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.tools.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.util.BeanUtils;

/**
 * Customized bean serializer that creates the Java beans (not the
 * query classes) from the database schema.
 */
public class BeanSerializer implements Serializer {

	/**
	 * the fileComment
	 */
	private String fileComment = "This file was generated from the database schema.";

	/**
	 * Constructor.
	 */
	public BeanSerializer() {
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
	public void setFileComment(final String fileComment) {
		this.fileComment = fileComment;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.codegen.Serializer#serialize(com.mysema.query.codegen.EntityType, com.mysema.query.codegen.SerializerConfig, com.mysema.codegen.CodeWriter)
	 */
	@Override
	public void serialize(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {

		// file comment
		w.javadoc(fileComment);

		// package declaration
		if (!entityType.getPackageName().isEmpty()) {
			w.packageDecl(entityType.getPackageName());
		}
		
		// imports
		printImports(entityType, config, w);
		
		// class Javadoc comment
		w.javadoc("This class represents rows from table '" + entityType.getData().get("table") + "'.");
		
		// class annotations
		printAnnotations(entityType.getAnnotations(), w);
		
		// begin writing the class itself
		w.beginClass(entityType, entityType.getSuperType() == null ? null : entityType.getSuperType().getType());
		
		// add an explicit empty constructor because it looks nicer
		w.javadoc("Constructor.");
		w.beginConstructor();
		w.end();
		
		// generate the data fields
		for (Property property : entityType.getProperties()) {
			w.javadoc("the " + property.getName());
			printAnnotations(property.getAnnotations(), w);
			w.privateField(property.getType(), property.getEscapedName());
		}
		
		// generate accessor methods
		for (Property property : entityType.getProperties()) {
			String propertyName = property.getEscapedName();
			String capitalizedPropertyName = BeanUtils.capitalize(propertyName);
			Type propertyType = property.getType();
			
			// getter method
			w.javadoc("Getter method for the " + propertyName + ".", "@return the " + propertyName);
			w.beginPublicMethod(propertyType, "get" + capitalizedPropertyName);
			w.line("return ", propertyName, ";");
			w.end();
			
			// setter method
			w.javadoc("Setter method for the " + propertyName + ".", "@param " + propertyName + " the " + propertyName + " to set");
			Parameter parameter = new Parameter(propertyName, propertyType);
			w.beginPublicMethod(Types.VOID, "set" + capitalizedPropertyName, parameter);
			w.line("this.", propertyName, " = ", propertyName, ";");
			w.end();
			
		}

		// generate toString() method
		w.line("/* (non-Javadoc)");
		w.line(" * @see java.lang.Object#toString()");
		w.line(" */");
		w.annotation(Override.class);
		w.beginPublicMethod(Types.STRING, "toString");
		{
			StringBuilder builder = new StringBuilder();
			for (Property property : entityType.getProperties()) {
				String propertyName = property.getEscapedName();
				builder.append(builder.length() == 0 ? "\"" : " + \", ");
				builder.append(propertyName);
				builder.append(" = \" + ");
				if (property.getType().getCategory() == TypeCategory.ARRAY) {
					builder.append("Arrays.toString(").append(propertyName).append(")");
				} else {
					builder.append(propertyName);
				}
			}
			w.line("return ", builder.toString(), ";");
		}
		w.end();
		
		// finish writing the class itself
		w.end();
		
	}
	
	/**
	 * Prints import clauses.
	 */
	private void printImports(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {
		
		// to avoid duplicate imports, we first collect all imports in a set
		Set<String> imports = new HashSet<String>();
		
		// annotation types
		addAnnotationImports(imports, entityType.getAnnotations());
		for (Property property : entityType.getProperties()) {
			addAnnotationImports(imports, property.getAnnotations());
		}
		
		// collection types
		addIf(imports, List.class.getName(), entityType.hasLists());
		addIf(imports, Collection.class.getName(), entityType.hasCollections());
		addIf(imports, Set.class.getName(), entityType.hasSets());
		addIf(imports, Map.class.getName(), entityType.hasMaps());
		
		// utility classes
		addIf(imports, Arrays.class.getName(), entityType.hasArrays());

		// actually write the imports
		w.importClasses(imports.toArray(new String[imports.size()]));
		
	}
	
	/**
	 * Adds imports for all annotations to the import collection.
	 */
	private void addAnnotationImports(Collection<String> imports, Collection<Annotation> annotations) {
		for (Annotation annotation : annotations) {
			imports.add(annotation.annotationType().getName());
		}
	}
	
	/**
	 * Adds an element to the collection if the condition is true.
	 */
	private <T> void addIf(Collection<T> collection, T element, boolean condition) {
		if (condition) {
			collection.add(element);
		}
	}

	/**
	 * Prints the specified annotations.
	 */
	private void printAnnotations(Collection<Annotation> annotations, final CodeWriter w) throws IOException {
		for (Annotation annotation : annotations) {
			w.annotation(annotation);
		}
	}
	
}
