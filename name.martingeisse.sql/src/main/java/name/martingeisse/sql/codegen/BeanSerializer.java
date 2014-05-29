/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.sql.codegen;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ClassUtils;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.sql.support.PrimaryKeyData;
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
	 * the forWicket
	 */
	private final boolean forWicket;

	/**
	 * the forAdmin
	 */
	private final boolean forAdmin;

	/**
	 * Constructor.
	 * @param forWicket whether the generated classes are intended for use with Wicket
	 * @param forAdmin whether the generated classes are intended for use in the admin framework
	 */
	public BeanSerializer(final boolean forWicket, final boolean forAdmin) {
		this.forWicket = forWicket;
		this.forAdmin = forAdmin;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.codegen.Serializer#serialize(com.mysema.query.codegen.EntityType, com.mysema.query.codegen.SerializerConfig, com.mysema.codegen.CodeWriter)
	 */
	@Override
	public void serialize(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {
		final String tableName = entityType.getData().get("table").toString();
		@SuppressWarnings("unchecked")
		final Collection<PrimaryKeyData> primaryKeys = (Collection<PrimaryKeyData>)entityType.getData().get(PrimaryKeyData.class);
		// @SuppressWarnings("unchecked")
		// Collection<ForeignKeyData> foreignKeys = (Collection<ForeignKeyData>) entityType.getData().get(ForeignKeyData.class);

		// determine primary key
		PrimaryKeyData primaryKey;
		if (primaryKeys.size() == 0) {
			primaryKey = null;
		} else if (primaryKeys.size() == 1) {
			primaryKey = primaryKeys.iterator().next();
		} else {
			throw new RuntimeException("Found entity with more than one primary key: " + entityType.getSimpleName());
		}

		// determine primary-key and non-primary-key fields
		List<Property> primaryKeyProperties;
		List<Property> nonPrimaryKeyProperties;
		if (primaryKey == null) {
			primaryKeyProperties = new ArrayList<Property>();
			nonPrimaryKeyProperties = new ArrayList<Property>(entityType.getProperties());
		} else {
			primaryKeyProperties = new ArrayList<Property>();
			nonPrimaryKeyProperties = new ArrayList<Property>();
			propertyLoop: for (final Property property : entityType.getProperties()) {
				for (final String primaryKeyPropertyName : primaryKey.getColumns()) {
					if (primaryKeyPropertyName.equals(property.getName())) {
						primaryKeyProperties.add(property);
						continue propertyLoop;
					}
				}
				nonPrimaryKeyProperties.add(property);
			}
		}

		// determine if we have an "ID"
		final Property idProperty;
		if (primaryKeyProperties.size() == 1 && primaryKeyProperties.get(0).getName().equals("id")) {
			idProperty = primaryKeyProperties.get(0);
		} else {
			idProperty = null;
		}

		// map properties by name
		final Map<String, Property> propertiesByName = new HashMap<String, Property>();
		for (final Property property : entityType.getProperties()) {
			propertiesByName.put(property.getEscapedName(), property);
		}

		// check if the entity has a boolean "deleted" property
		Property deletedFlagProperty = propertiesByName.get("deleted");
		if (deletedFlagProperty != null && deletedFlagProperty.getType().getJavaClass() != Boolean.class && deletedFlagProperty.getType().getJavaClass() != Boolean.TYPE) {
			deletedFlagProperty = null;
		}

		// check if the entity has an "orderIndex" property
		Property orderIndexProperty = propertiesByName.get("orderIndex");
		if (orderIndexProperty != null && orderIndexProperty.getType().getJavaClass() != Integer.class && orderIndexProperty.getType().getJavaClass() != Integer.TYPE) {
			orderIndexProperty = null;
		}

		// file comment
		printFileComment(w);

		// package declaration
		if (!entityType.getPackageName().isEmpty()) {
			w.packageDecl(entityType.getPackageName());
		}

		// imports
		printImports(entityType, config, w, idProperty != null, deletedFlagProperty != null, orderIndexProperty != null);

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
			final List<Type> interfaces = new ArrayList<>();
			interfaces.add(new SimpleType("Serializable"));
			interfaces.add(new SimpleType(new SimpleType("IEntityWithId"), idProperty.getType()));
			if (deletedFlagProperty != null) {
				interfaces.add(new SimpleType("IEntityWithDeletedFlag"));
			}
			if (orderIndexProperty != null) {
				interfaces.add(new SimpleType("IEntityWithOrderIndex"));
			}
			w.beginClass(entityType, null, interfaces.toArray(new Type[0]));
		}

		// add the meta-data class constant for the admin framework
		if (forAdmin) {
			final String className = w.getGenericName(true, entityType);
			w.javadoc("Meta-data about this class for the admin framework");
			w.line("public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(" + className + ".class);");
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
			if (property == idProperty || property == deletedFlagProperty || property == orderIndexProperty) {
				w.line("@Override");
			}
			w.beginPublicMethod(propertyType, "get" + capitalizedPropertyName);
			w.line("return ", propertyName, ";");
			w.end();

			// setter method
			w.javadoc("Setter method for the " + propertyName + ".", "@param " + propertyName + " the " + propertyName + " to set");
			if (property == idProperty || property == deletedFlagProperty || property == orderIndexProperty) {
				w.line("@Override");
			}
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

		// generate findById() method and corresponding Wicket model (only works if there is a single-column primary key)
		if (idProperty != null) {
			final String entityName = entityType.getSimpleName();
			final String uncapEntityName = entityType.getUncapSimpleName();

			Type idType = idProperty.getType();
			if (ClassUtils.wrapperToPrimitive(idType.getJavaClass()) != null) {
				idType = new SimpleType(ClassUtils.wrapperToPrimitive(idType.getJavaClass()).getSimpleName());
			}

			w.javadoc("Loads a record by id.", "@param id the id of the record to load", "@return the loaded record");
			w.beginStaticMethod(entityType, "findById", new Parameter("id", idType));
			w.line("final Q" + entityName + " q = Q" + entityName + "." + entityType.getUncapSimpleName() + ";");
			w.line("final SQLQuery query = EntityConnectionManager.getConnection().createQuery();");
			w.line("return query.from(q).where(q." + idProperty.getName() + ".eq(id)).singleResult(q);");
			w.end();

			if (forWicket) {
				w.javadoc("Creates a model that loads a record by id.", "@param id the id of the record to load", "@return the model loading the record");
				w.beginStaticMethod(new SimpleType(new SimpleType("IModel"), entityType), "getModelForId", new Parameter("id", idType));
				w.line("return new EntityModel<" + entityName + ">(Q" + entityName + "." + uncapEntityName + ", Q" + entityName + "." + uncapEntityName + ".id.eq(id));");
				w.end();
			}

		}

		// generate insert() method (only works if there is no multi-column primary key)
		if (primaryKey == null || primaryKey.getColumns().size() < 2) {
			w.javadoc("Inserts a record into the database using all fields from this object except the ID, then updates the ID.");
			w.beginPublicMethod(Types.VOID, "insert");
			w.line("final Q" + entityType.getSimpleName() + " q = Q" + entityType.getSimpleName() + "." + entityType.getUncapSimpleName() + ";");
			w.line("final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);");
			for (final Property property : nonPrimaryKeyProperties) {
				final String propertyName = property.getEscapedName();
				w.line("insert.set(q." + propertyName + ", " + propertyName + ");");
			}
			if (primaryKey == null || primaryKey.getColumns().isEmpty()) {
				w.line("insert.execute();");
			} else {
				final String idName = primaryKey.getColumns().iterator().next();
				final Type idType = propertiesByName.get(idName).getType();
				w.line(idName + " = insert.executeWithKey(" + idType.getSimpleName() + ".class);");
			}
			w.end();
		}

		// finish writing the class itself
		w.end();

	}

	/**
	 * Prints import clauses.
	 */
	private void printImports(final EntityType entityType, final SerializerConfig config, final CodeWriter w, final boolean hasId, final boolean hasDeletedFlag, final boolean hasOrderIndex) throws IOException {

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
		imports.add("com.mysema.query.sql.SQLQuery");
		imports.add("com.mysema.query.sql.dml.SQLInsertClause");
		imports.add("name.martingeisse.sql.EntityConnectionManager");
		addIf(imports, "name.martingeisse.sql.terms.IEntityWithId", hasId);
		addIf(imports, "name.martingeisse.sql.terms.IEntityWithDeletedFlag", hasDeletedFlag);
		addIf(imports, "name.martingeisse.sql.terms.IEntityWithOrderIndex", hasOrderIndex);
		if (forWicket) {
			addIf(imports, "org.apache.wicket.model.IModel", hasId);
			addIf(imports, "name.martingeisse.wicket.model.database.EntityModel", hasId);
		}

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

}
