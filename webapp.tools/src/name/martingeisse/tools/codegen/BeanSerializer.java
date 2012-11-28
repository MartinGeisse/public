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

import name.martingeisse.common.database.IDatabaseDescriptor;
import name.martingeisse.common.util.ReturnValueUtil;
import name.martingeisse.common.util.string.StringUtil;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.sql.support.ForeignKeyData;
import com.mysema.util.BeanUtils;

/**
 * Customized bean serializer that creates the Java beans (not the
 * query classes) from the database schema.
 * 
 * This class has a central 'forAdmin' flag that determines whether
 * the generated classes are intended for use in the admin framework
 * or without it. The latter is very close to how QueryDSL originally
 * generated bean classes, while the former adds an implementation
 * for the IEntityInstance interface of the admin framework.
 */
public class BeanSerializer extends AbstractSerializer {

	/**
	 * the forAdmin
	 */
	private final boolean forAdmin;

	/**
	 * Constructor.
	 * @param forAdmin whether the generated classes are intended for use in the admin framework
	 */
	public BeanSerializer(final boolean forAdmin) {
		this.forAdmin = forAdmin;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.codegen.Serializer#serialize(com.mysema.query.codegen.EntityType, com.mysema.query.codegen.SerializerConfig, com.mysema.codegen.CodeWriter)
	 */
	@Override
	public void serialize(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {
		final String tableName = entityType.getData().get("table").toString();

		// file comment
		printFileComment(w);

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
		if (forAdmin) {
			w.line("@GeneratedFromTable(\"" + tableName + "\")");
		}

		// begin writing the class itself
		if (forAdmin) {
			if (entityType.getSuperType() != null) {
				w.beginClass(entityType, entityType.getSuperType().getType());
			} else {
				w.beginClass(entityType, new SimpleType("AbstractSpecificEntityInstance"));
			}
		} else {
			w.beginClass(entityType, null, new SimpleType("Serializable"));
		}

		// add the meta-data class constant for the admin framework
		if (forAdmin) {
			final String className = w.getGenericName(true, entityType);
			w.javadoc("Meta-data about this class for the admin framework");
			w.line("public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(" + className
				+ ".class);");
			w.nl();
		}

		// add an explicit empty constructor because it looks nicer and to support meta-data in the admin framework
		w.javadoc("Constructor.");
		w.beginConstructor();
		if (forAdmin) {
			w.line("super(GENERATED_CLASS_META_DATA);");
		}
		w.end();

		// generate the data fields
		for (final Property property : entityType.getProperties()) {
			w.javadoc("the " + property.getEscapedName());
			printAnnotations(property.getAnnotations(), w);
			w.privateField(property.getType(), property.getEscapedName());
		}

		// generate accessor methods
		for (final Property property : entityType.getProperties()) {
			final String propertyName = property.getEscapedName();
			final String capitalizedPropertyName = BeanUtils.capitalize(propertyName);
			final Type propertyType = property.getType();

			// getter method
			w.javadoc("Getter method for the " + propertyName + ".", "@return the " + propertyName);
			if (forAdmin) {
				w.line("@GeneratedFromColumn(\"" + property.getName() + "\")");
			}
			w.beginPublicMethod(propertyType, "get" + capitalizedPropertyName);
			w.line("return ", propertyName, ";");
			w.end();

			// setter method
			w.javadoc("Setter method for the " + propertyName + ".", "@param " + propertyName + " the " + propertyName + " to set");
			final Parameter parameter = new Parameter(propertyName, propertyType);
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
			final StringBuilder builder = new StringBuilder();
			for (final Property property : entityType.getProperties()) {
				final String propertyName = property.getEscapedName();
				builder.append(builder.length() == 0 ? ("\"{" + entityType.getSimpleName() + ". ") : " + \", ");
				builder.append(propertyName);
				builder.append(" = \" + ");
				if (property.getType().getCategory() == TypeCategory.ARRAY) {
					builder.append("Arrays.toString(").append(propertyName).append(")");
				} else {
					builder.append(propertyName);
				}
			}
			w.line("return ", builder.toString(), " + \"}\";");
		}
		w.end();

		// generate simplified access methods
		final String databaseExpression = getSimplifiedAccessDatabaseExpression(entityType, config);
		if (databaseExpression != null) {
			final Set<Property> simplifiedAccessProperties = getSimplifiedAccessProperties(entityType, config, w);
			for (final Property simplifiedAccessProperty : simplifiedAccessProperties) {
				generateSimplifiedAccessMethods(entityType, simplifiedAccessProperty, config, w, databaseExpression);
			}
		}

		// finish writing the class itself
		w.end();

	}

	/**
	 * Prints import clauses.
	 */
	private void printImports(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {
		final boolean generateSimplifiedAccessMethods = (getSimplifiedAccessDatabaseExpression(entityType, config) != null);
		final String simplifiedAccessDatabaseImport = getSimplifiedAccessDatabaseImport(entityType, config);

		// to avoid duplicate imports, we first collect all imports in a set
		final Set<String> imports = new HashSet<String>();

		// annotation types
		addAnnotationImports(imports, entityType.getAnnotations());
		for (final Property property : entityType.getProperties()) {
			addAnnotationImports(imports, property.getAnnotations());
		}

		// collection types
		addIf(imports, List.class.getName(), entityType.hasLists());
		addIf(imports, Collection.class.getName(), entityType.hasCollections());
		addIf(imports, Set.class.getName(), entityType.hasSets());
		addIf(imports, Map.class.getName(), entityType.hasMaps());

		// utility classes
		addIf(imports, Arrays.class.getName(), entityType.hasArrays());
		addIf(imports, "name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance", forAdmin);
		addIf(imports, "name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta", forAdmin);
		addIf(imports, "name.martingeisse.admin.entity.schema.orm.GeneratedFromTable", forAdmin);
		addIf(imports, "name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn", forAdmin);
		addIf(imports, "java.io.Serializable", !forAdmin);
		addIf(imports, "com.mysema.query.sql.SQLQuery", generateSimplifiedAccessMethods);
		addIf(imports, "com.mysema.query.types.Predicate", generateSimplifiedAccessMethods);
		addIf(imports, "com.mysema.query.support.Expressions", generateSimplifiedAccessMethods);
		addIf(imports, "com.mysema.commons.lang.CloseableIterator", generateSimplifiedAccessMethods);
		addIf(imports, "name.martingeisse.common.database.EntityConnectionManager", generateSimplifiedAccessMethods);
		addIf(imports, "java.util.HashMap", generateSimplifiedAccessMethods);
		addIf(imports, "java.util.ArrayList", generateSimplifiedAccessMethods);
		addIf(imports, simplifiedAccessDatabaseImport, simplifiedAccessDatabaseImport != null);

		// actually write the imports
		printImports(w, imports);

	}

	/**
	 * Adds imports for all annotations to the import collection.
	 */
	private void addAnnotationImports(final Collection<String> imports, final Collection<Annotation> annotations) {
		for (final Annotation annotation : annotations) {
			imports.add(annotation.annotationType().getName());
		}
	}

	/**
	 * Returns the expression to use for the {@link IDatabaseDescriptor} used to fetch the specified entity,
	 * or null to skip generation of the simplified access methods.
	 */
	protected String getSimplifiedAccessDatabaseExpression(final EntityType entityType, final SerializerConfig config) {
		return null;
	}

	/**
	 * Returns an import class name to add for use in simplified access methods,
	 * or null to skip generation of the import clause.
	 */
	protected String getSimplifiedAccessDatabaseImport(final EntityType entityType, final SerializerConfig config) {
		return null;
	}

	/**
	 * Returns the properties for which simplified accessor methods shall be generated.
	 * The default implementations returns the properties for single-property foreign keys.
	 */
	protected Set<Property> getSimplifiedAccessProperties(final EntityType entityType, final SerializerConfig config, final CodeWriter w)
		throws IOException {
		final Set<Property> result = new HashSet<Property>();
		@SuppressWarnings("unchecked")
		final Collection<ForeignKeyData> foreignKeys = (Collection<ForeignKeyData>)entityType.getData().get(ForeignKeyData.class);
		if (foreignKeys == null) {
			return result;
		}
		for (final ForeignKeyData foreignKeyData : foreignKeys) {
			if (foreignKeyData.getParentColumns().size() == 1) {
				final String propertyName = foreignKeyData.getForeignColumns().get(0);
				final Property property = getProperty(entityType, propertyName);
				ReturnValueUtil.nullMeansMissing(property, "getProperty: " + entityType + ", " + propertyName);
				result.add(property);
			}
		}
		return result;
	}

	/**
	 * Obtains a property by name.
	 * @param entityType the entity type to obtain the property from
	 * @param name the property name
	 * @return the property
	 */
	protected final Property getProperty(final EntityType entityType, final String name) {
		for (final Property property : entityType.getProperties()) {
			if (property.getName().equals(name)) {
				return property;
			}
		}
		return null;
	}

	/**
	 * Generates simplified access methods for the specified property.
	 * @param entityType the entity type that contains the property
	 * @param property the property
	 * @param config the configuration
	 * @param w the writer
	 * @param databaseExpression the expression for the {@link IDatabaseDescriptor}
	 * @throws IOException on I/O errors
	 */
	protected void generateSimplifiedAccessMethods(final EntityType entityType, final Property property, final SerializerConfig config,
		final CodeWriter w, final String databaseExpression) throws IOException {
		generateFindByMethod(entityType, property, config, w, databaseExpression);
		generateFindAllByMethod(entityType, property, config, w, databaseExpression);
		generateMapByMethod(entityType, property, config, w, databaseExpression);
		generateMapAllByMethod(entityType, property, config, w, databaseExpression);
	}

	/**
	 * Generates the findByProperty() method for the specified property.
	 */
	protected void generateFindByMethod(final EntityType entityType, final Property property, final SerializerConfig config,
		final CodeWriter w, final String databaseExpression) throws IOException {
		final String lowercasePropertyName = property.getEscapedName();
		final String uppercasePropertyName = StringUtil.capitalizeFirst(lowercasePropertyName);
		w.line("/**");
		w.line(" * Returns the first instance with the specified ", lowercasePropertyName, ".");
		w.line(" * @param value the ", lowercasePropertyName);
		w.line(" * @return the instance, or null if none was found");
		w.line(" */");
		w.beginStaticMethod(entityType, "findBy" + uppercasePropertyName, new Parameter("value", property.getType()));
		w.line("SQLQuery query = EntityConnectionManager.getConnection(", databaseExpression, ").createQuery();");
		w.line("Q", entityType.getSimpleName(), " table = ", "Q", entityType.getSimpleName(), ".", entityType.getUncapSimpleName(), ";");
		w.line("query.from(table);");
		w.line("query.where(table.", lowercasePropertyName, ".eq(Expressions.constant(value)));");
		w.line("return query.singleResult(table);");
		w.end();
	}

	/**
	 * Generates the findAllByProperty() method for the specified property.
	 */
	protected void generateFindAllByMethod(final EntityType entityType, final Property property, final SerializerConfig config,
		final CodeWriter w, final String databaseExpression) throws IOException {
		final Type listType = new ClassType(List.class, entityType);
		final String lowercasePropertyName = property.getEscapedName();
		final String uppercasePropertyName = StringUtil.capitalizeFirst(lowercasePropertyName);
		w.line("/**");
		w.line(" * Returns all instances with the specified ", lowercasePropertyName, ".");
		w.line(" * @param value the ", lowercasePropertyName);
		w.line(" * @return the instances (an empty list if none was found)");
		w.line(" */");
		w.beginStaticMethod(listType, "findAllBy" + uppercasePropertyName, new Parameter("value", property.getType()));
		w.line("SQLQuery query = EntityConnectionManager.getConnection(", databaseExpression, ").createQuery();");
		w.line("Q", entityType.getSimpleName(), " table = ", "Q", entityType.getSimpleName(), ".", entityType.getUncapSimpleName(), ";");
		w.line("query.from(table);");
		w.line("query.where(table.", lowercasePropertyName, ".eq(Expressions.constant(value)));");
		w.line("return query.list(table);");
		w.end();
	}
	
	/**
	 * Generates the mapByProperty() method for the specified property.
	 */
	protected void generateMapByMethod(final EntityType entityType, final Property property, final SerializerConfig config,
		final CodeWriter w, final String databaseExpression) throws IOException {
		final Type valuesType = new ClassType(Collection.class, property.getType());
		final Type mapType = new ClassType(Map.class, property.getType(), entityType);
		final String lowercasePropertyName = property.getEscapedName();
		final String uppercasePropertyName = StringUtil.capitalizeFirst(lowercasePropertyName);
		w.line("/**");
		w.line(" * Returns the an instance for each of the specified ", lowercasePropertyName, " values that also satisfies the additional");
		w.line(" * conditions, mapped by ", lowercasePropertyName, ".");
		w.line(" * This method is sub-optimal if many instances exist for any of the specified values since it first fetches all those values.");
		w.line(" * Values for which no instance exist will be missing from the returned map.");
		w.line(" * @param values the ", lowercasePropertyName, " values");
		w.line(" * @param additionalConditions the additional conditions for returned instances");
		w.line(" * @return the instances (an empty map if none was found)");
		w.line(" */");
		w.beginStaticMethod(mapType, "mapBy" + uppercasePropertyName, new Parameter("values", valuesType), new Parameter("additionalConditions", new SimpleType("Predicate...")));
		w.line("SQLQuery query = EntityConnectionManager.getConnection(", databaseExpression, ").createQuery();");
		w.line("Q", entityType.getSimpleName(), " table = ", "Q", entityType.getSimpleName(), ".", entityType.getUncapSimpleName(), ";");
		w.line("query.from(table);");
		w.line("query.where(table.", lowercasePropertyName, ".in(values));");
		w.line("query.where(additionalConditions);");
		w.line("CloseableIterator<" + entityType.getSimpleName() + "> it = query.iterate(table);");
		w.line(mapType.toString(), " result = new HashMap<", property.getType().toString(), ", ", entityType.toString(), ">();");
		w.line("while (it.hasNext()) {");
		w.line("	", entityType.getSimpleName(), " row = it.next();");
		w.line("	result.put(row.get", uppercasePropertyName, "(), row);");
		w.line("}");
		w.line("it.close();");
		w.line("return result;");
		w.end();
	}
	
	/**
	 * Generates the mapAllByProperty() method for the specified property.
	 */
	protected void generateMapAllByMethod(final EntityType entityType, final Property property, final SerializerConfig config,
		final CodeWriter w, final String databaseExpression) throws IOException {
		final Type valuesType = new ClassType(Collection.class, property.getType());
		final Type entityListType = new ClassType(List.class, entityType);
		final Type mapType = new ClassType(Map.class, property.getType(), entityListType);
		final String lowercasePropertyName = property.getEscapedName();
		final String uppercasePropertyName = StringUtil.capitalizeFirst(lowercasePropertyName);
		w.line("/**");
		w.line(" * Returns all instances with any of the specified ", lowercasePropertyName, " values that also satisfy the additional");
		w.line(" * conditions, mapped by ", lowercasePropertyName, ".");
		w.line(" * @param values the ", lowercasePropertyName, " values");
		w.line(" * @param additionalConditions the additional conditions for returned instances");
		w.line(" * @return the instances (an empty map if none was found)");
		w.line(" */");
		w.beginStaticMethod(mapType, "mapAllBy" + uppercasePropertyName, new Parameter("values", valuesType), new Parameter("additionalConditions", new SimpleType("Predicate...")));
		w.line("SQLQuery query = EntityConnectionManager.getConnection(", databaseExpression, ").createQuery();");
		w.line("Q", entityType.getSimpleName(), " table = ", "Q", entityType.getSimpleName(), ".", entityType.getUncapSimpleName(), ";");
		w.line("query.from(table);");
		w.line("query.where(table.", lowercasePropertyName, ".in(values));");
		w.line("query.where(additionalConditions);");
		w.line("CloseableIterator<" + entityType.getSimpleName() + "> it = query.iterate(table);");
		w.line(mapType.toString(), " result = new HashMap<", property.getType().toString(), ", ", entityListType.toString(), ">();");
		w.line("while (it.hasNext()) {");
		w.line("	", entityType.getSimpleName(), " row = it.next();");
		w.line("	", property.getType().toString(), " value = row.get", uppercasePropertyName, "();");
		w.line("	", entityListType.toString(), " list = result.get(value);");
		w.line("	if (list == null) {");
		w.line("		list = new ArrayList<", entityType.toString(), ">();");
		w.line("		result.put(value, list);");
		w.line("	}");
		w.line("	list.add(row);");
		w.line("}");
		w.line("it.close();");
		w.line("return result;");
		w.end();
	}
	
}
