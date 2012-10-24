/**
 * This file contains code snippets from the QueryDSL project.
 * Other parts are Copyright (c) 2011 Martin Geisse.
 */

package name.martingeisse.tools.codegen;

import static com.mysema.codegen.Symbols.ASSIGN;
import static com.mysema.codegen.Symbols.COMMA;
import static com.mysema.codegen.Symbols.DOT_CLASS;
import static com.mysema.codegen.Symbols.EMPTY;
import static com.mysema.codegen.Symbols.NEW;
import static com.mysema.codegen.Symbols.QUOTE;
import static com.mysema.codegen.Symbols.RETURN;
import static com.mysema.codegen.Symbols.SEMICOLON;
import static com.mysema.codegen.Symbols.STAR;
import static com.mysema.codegen.Symbols.SUPER;
import static com.mysema.codegen.Symbols.THIS;
import static com.mysema.codegen.Symbols.UNCHECKED;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Generated;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Constructor;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.TypeExtends;
import com.mysema.codegen.model.Types;
import com.mysema.query.codegen.Delegate;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.Supertype;
import com.mysema.query.codegen.TypeMappings;
import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.codegen.NamingStrategy;
import com.mysema.query.sql.codegen.SQLCodegenModule;
import com.mysema.query.sql.support.ForeignKeyData;
import com.mysema.query.sql.support.InverseForeignKeyData;
import com.mysema.query.sql.support.KeyData;
import com.mysema.query.sql.support.PrimaryKeyData;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.path.ArrayPath;
import com.mysema.query.types.path.CollectionPath;
import com.mysema.query.types.path.ListPath;
import com.mysema.query.types.path.MapPath;
import com.mysema.query.types.path.PathInits;
import com.mysema.query.types.path.SetPath;
import com.mysema.query.types.path.SimplePath;

/**
 * Customized meta-data serializer that creates the query classes (not the
 * Java beans) from the database schema.
 */
public class MetaDataSerializer extends AbstractSerializer {

	/**
	 * The QueryDSL type for the INITS constant.
	 */
	@SuppressWarnings("unused")
	private static final ClassType PATH_INITS_TYPE = new ClassType(PathInits.class);

	/**
	 * String joiner.
	 */
	private static final Joiner JOINER = Joiner.on("\", \"");

	/**
	 * Parameter model for a {@link PathMetadata} parameter.
	 */
	private static final Parameter PATH_METADATA = new Parameter("metadata", new ClassType(PathMetadata.class, (Type)null));

	/**
	 * Parameter model for a {@link PathInits} parameter.
	 */
	private static final Parameter PATH_INITS = new Parameter("inits", new ClassType(PathInits.class));

	/**
	 * the typeMappings
	 */
	private TypeMappings typeMappings;

	/**
	 * the namingStrategy
	 */
	private NamingStrategy namingStrategy;

	/**
	 * Constructor.
	 * @param typeMappings the type mappings
	 * @param namingStrategy the naming strategy
	 * @param innerClassesForKeys (ignored)
	 */
	@Inject
	public MetaDataSerializer(TypeMappings typeMappings, NamingStrategy namingStrategy, @Named(SQLCodegenModule.INNER_CLASSES_FOR_KEYS) boolean innerClassesForKeys) {
        this.typeMappings = typeMappings;
        this.namingStrategy = namingStrategy;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.codegen.Serializer#serialize(com.mysema.query.codegen.EntityType, com.mysema.query.codegen.SerializerConfig, com.mysema.codegen.CodeWriter)
	 */
	@Override
	public void serialize(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {

		// preparation
		Type rawQueryType = typeMappings.getPathType(entityType, entityType, true);
		Type nonrawQueryType = typeMappings.getPathType(entityType, entityType, true);

		// file comment
		printFileComment(w);

		// package declaration
		if (!nonrawQueryType.getPackageName().isEmpty()) {
			w.packageDecl(nonrawQueryType.getPackageName());
		}

		// imports
		printImports(entityType, config, w);

		// class javadoc
		w.nl();
		w.javadoc(typeMappings.getPathType(entityType, entityType, true).getSimpleName() + " is a Querydsl query type for "
			+ entityType.getSimpleName());

		// class annotations
		w.line("@Generated(\"", getClass().getName(), "\")");
		if (entityType.equals(rawQueryType)) {
			printAnnotations(entityType.getAnnotations(), w);
		}

		// begin writing the class itself
		w.beginClass(rawQueryType, new ClassType(entityType.getOriginalCategory(), RelationalPathBase.class, entityType));

		// add a serialVersionUID
		w.privateStaticFinal(Types.LONG_P, "serialVersionUID", String.valueOf(entityType.hashCode()));

		// add factory methods
		printFactoryMethods(entityType, config, w);

		// add an INITS constant
		printInits(entityType, config, w);

		// add a default instance if desired
		printDefaultInstance(entityType, config, w);

		// add a reference to the supertype (if any)
		printSupertypeReference(entityType, config, w);

		// members (properties, primary keys, foreign keys, reverse foreign keys)
		printMembers(entityType, config, w);

		// constructors
		printConstructors(entityType, config, w);

		// delegates
		printDelegates(entityType, config, w);

		// property accessors
		printPropertyAccessors(entityType, config, w);

		// finish writing the class itself
		w.end();

	}

	/**
	 * Prints the required imports.
	 */
	private void printImports(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {

		// to avoid duplicate imports, we first collect all imports in a set
		final Set<String> imports = new HashSet<String>();
		final Set<String> packageImports = new HashSet<String>();
		final Set<Class<?>> staticImports = new HashSet<Class<?>>();

		// the query type
		Type queryType = typeMappings.getPathType(entityType, entityType, true);
		if (!entityType.getPackageName().isEmpty()) {
			if (!queryType.getPackageName().equals(entityType.getPackageName())) {
				if (!queryType.getSimpleName().equals(entityType.getSimpleName())) {
					String fullName = entityType.getFullName();
					String packageName = entityType.getPackageName();
					if (fullName.substring(packageName.length() + 1).contains(".")) {
						fullName = fullName.substring(0, fullName.lastIndexOf('.'));
					}
					imports.add(fullName);
				}
			}
		}

		// delegate packages
		for (Delegate delegate : entityType.getDelegates()) {
			String delegatePackageName = delegate.getDelegateType().getPackageName();
			if (!delegatePackageName.equals(entityType.getPackageName())) {
				packageImports.add(delegatePackageName);
			}
		}

		// import the QueryDSL expression package if needed
		if (needsExpressionPackage(entityType)) {
			packageImports.add(ComparableExpression.class.getPackage().getName());
		}

		// import the java.util package if needed
		if (needsJavaUtilPackage(entityType)) {
			packageImports.add(List.class.getPackage().getName());
		}

		// miscellaneous imports
		imports.add(Generated.class.getName());
		staticImports.add(PathMetadataFactory.class);
		packageImports.add(PathMetadata.class.getPackage().getName());
		packageImports.add(SimplePath.class.getPackage().getName());

		// actually write the imports
		printImports(w, imports);
		printPackageImports(w, packageImports);
		printStaticImports(w, staticImports);

	}

	/**
	 * Checks if the QueryDSL expression package must be imported.
	 */
	private boolean needsExpressionPackage(final EntityType entityType) {
		for (Constructor constructor : entityType.getConstructors()) {
			if (needsExpressionPackage(constructor.getParameters())) {
				return true;
			}
		}
		for (Delegate delegate : entityType.getDelegates()) {
			if (needsExpressionPackage(delegate.getParameters())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the QueryDSL expression package must be imported for any of the specified parameters.
	 */
	private boolean needsExpressionPackage(final Collection<Parameter> parameters) {
		for (Parameter parameter : parameters) {
			if (parameter.getType().getPackageName().equals(ComparableExpression.class.getPackage().getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the java.util package must be imported.
	 */
	@SuppressWarnings("unchecked")
	private boolean needsJavaUtilPackage(final EntityType entityType) {
		Collection<ForeignKeyData> foreignKeys = (Collection<ForeignKeyData>)entityType.getData().get(ForeignKeyData.class);
		Collection<InverseForeignKeyData> inverseForeignKeys = (Collection<InverseForeignKeyData>)entityType.getData().get(
			InverseForeignKeyData.class);
		return (needsJavaUtilPackage(foreignKeys) || needsJavaUtilPackage(inverseForeignKeys));
	}

	/**
	 * Checks if the java.util package must be imported.
	 */
	private boolean needsJavaUtilPackage(final Collection<? extends KeyData> keyDatas) {
		if (keyDatas != null) {
			for (KeyData keyData : keyDatas) {
				if (keyData.getForeignColumns().size() > 1) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Generates static factory methods.
	 */
	private void printFactoryMethods(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {

		// preparation
		String localName = w.getRawName(entityType);
		String genericName = w.getGenericName(true, entityType);
		boolean isGeneric = !localName.equals(genericName);

		// build one factory method per constructor
		for (Constructor constructor : entityType.getConstructors()) {

			// add SuppressWarnings if the entity is generic
			if (isGeneric) {
				w.suppressWarnings(UNCHECKED);
			}

			// start writing the factory method
			Type returnType = new ClassType(ConstructorExpression.class, entityType);
			Function<Parameter, Parameter> parameterTransformer = new Function<Parameter, Parameter>() {
				@Override
				public Parameter apply(Parameter p) {
					Type newType = typeMappings.getExprType(p.getType(), entityType, false, false, true);
					return new Parameter(p.getName(), newType);
				}
			};
			w.beginStaticMethod(returnType, "create", constructor.getParameters(), parameterTransformer);

			// write the body of the factory method
			// TODO: The original code said: "replace with class reference"
			w.beginLine("return new ConstructorExpression<" + genericName + ">(");
			if (isGeneric) {
				w.append("(Class)");
			}
			w.append(localName);
			w.append(".class, new Class[] {");
			boolean first = true;
			for (Parameter parameter : constructor.getParameters()) {
				if (first) {
					first = false;
				} else {
					w.append(", ");
				}
				Type type = parameter.getType();
				Type primitive = Types.PRIMITIVES.get(type);
				if (primitive == null) {
					w.append(w.getRawName(type));
					w.append(".class");
				} else {
					w.append(primitive.getFullName() + ".class");
				}
			}
			w.append("}");
			for (Parameter parameter : constructor.getParameters()) {
				w.append(", ");
				w.append(parameter.getName());
			}
			w.append(");\n");
			w.end();
		}

	}

	/**
	 * Prints the "INITS" constant.
	 */
	private void printInits(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {

		// collect inits
		List<String> inits = new ArrayList<String>();
		for (Property property : entityType.getProperties()) {
			for (String init : property.getInits()) {
				inits.add(property.getEscapedName() + '.' + init);
			}
		}

		// generate the INITS constant
		String value;
		if (inits.isEmpty()) {
			value = "PathInits.DIRECT";
		} else {
			inits.add(0, STAR);
			value = "new PathInits(" + QUOTE + JOINER.join(inits) + QUOTE + ")";
		}
		
		// I don't exactly know why, but the INITS isn't used at all, so don't generate it
		value = value + ""; // suppress warnings
		// w.privateStaticFinal(PATH_INITS_TYPE, "INITS", value);

	}

	/**
	 * Prints a default instance of the Q-class if desired
	 */
	private void printDefaultInstance(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {
		if (config.createDefaultVariable()) {
			String variableName = namingStrategy.getDefaultVariableName(entityType);
			String alias = namingStrategy.getDefaultAlias(entityType);
			Type queryType = typeMappings.getPathType(entityType, entityType, true);
			w.javadoc("The default instance of this class.");
			w.publicStaticFinal(queryType, variableName, NEW + queryType.getSimpleName() + "(\"" + alias + "\")");
		}
	}

	/**
	 * Prints a model constant for the supertype (if any).
	 */
	private void printSupertypeReference(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {
		if (entityType.getSuperType() != null && entityType.getSuperType().getEntityType() != null) {
			EntityType superType = entityType.getSuperType().getEntityType();
			Type superQueryType = typeMappings.getPathType(superType, entityType, false);
			if (!superType.hasEntityFields()) {
				w.publicFinal(superQueryType, "_super", NEW + w.getRawName(superQueryType) + "(this)");
			} else {
				w.publicFinal(superQueryType, "_super");
			}
		}
	}

	/**
	 * Prints model constants for the entity members: properties, primary keys, foreign keys and inverse foreign keys.
	 */
	@SuppressWarnings("unchecked")
	private void printMembers(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {

		// properties
		printProperties(entityType, w);

		// primary keys
		printPrimaryKeys(entityType, (Collection<PrimaryKeyData>)entityType.getData().get(PrimaryKeyData.class), w);

		// forward foreign keys
		printForeignKeys(entityType, (Collection<ForeignKeyData>)entityType.getData().get(ForeignKeyData.class), w, false);

		// reverse foreign keys
		printForeignKeys(entityType, (Collection<InverseForeignKeyData>)entityType.getData().get(InverseForeignKeyData.class), w, true);

	}

	/**
	 * Prints model constants for the entity properties.
	 */
	private void printProperties(final EntityType entityType, final CodeWriter w) throws IOException {
		for (Property property : entityType.getProperties()) {
			Type type = property.getType();
			TypeCategory category = type.getCategory();

			// add a Javadoc comment for this property
			w.javadoc("Metamodel property for property '" + property.getName() + "'");
			
			// TODO: the original code said: "the custom types should have the custom type category"
			if (typeMappings.isRegistered(type) && category != TypeCategory.CUSTOM && category != TypeCategory.ENTITY) {
				printCustomProperty(entityType, property, w);
				continue;
			}

			// preparation
			Type queryType = typeMappings.getPathType(new SimpleType(type, type.getParameters()), entityType, false);
			String classConstant = ("" + w.getRawName(type) + DOT_CLASS);
			String inits = (property.getInits().isEmpty() ? ("INITS.get(\"" + property.getName() + "\")") : ("PathInits.DIRECT"));

			// handle the various types
			switch (category) {
			case STRING:
				printProperty(entityType, property, queryType, w, "createString");
				break;

			case BOOLEAN:
				printProperty(entityType, property, queryType, w, "createBoolean");
				break;

			case SIMPLE:
				printProperty(entityType, property, queryType, w, "createSimple", classConstant);
				break;

			case COMPARABLE:
				printProperty(entityType, property, queryType, w, "createComparable", classConstant);
				break;

			case ENUM:
				printProperty(entityType, property, queryType, w, "createEnum", classConstant);
				break;

			case DATE:
				printProperty(entityType, property, queryType, w, "createDate", classConstant);
				break;

			case DATETIME:
				printProperty(entityType, property, queryType, w, "createDateTime", classConstant);
				break;

			case TIME:
				printProperty(entityType, property, queryType, w, "createTime", classConstant);
				break;

			case NUMERIC:
				printProperty(entityType, property, queryType, w, "createNumber", classConstant);
				break;

			case CUSTOM:
				printCustomProperty(entityType, property, w);
				break;

			case ARRAY: {
				Type actualType = new ClassType(ArrayPath.class, property.getType().getComponentType());
				printProperty(entityType, property, actualType, w, "createArray", classConstant);
				break;
			}

			case COLLECTION: {
				Type genericQueryType = typeMappings.getPathType(getRawType(property.getParameter(0)), entityType, false);
				String genericKey = w.getGenericName(true, property.getParameter(0));
				classConstant = w.getRawName(property.getParameter(0)) + DOT_CLASS;
				queryType = typeMappings.getPathType(property.getParameter(0), entityType, true);
				printProperty(entityType, property, new ClassType(CollectionPath.class, getRawType(property.getParameter(0)),
					genericQueryType), w, "this.<" + genericKey + COMMA + w.getGenericName(true, genericQueryType) + ">createCollection",
					classConstant, w.getRawName(queryType) + DOT_CLASS, inits);
				break;
			}

			case SET: {
				Type genericQueryType = typeMappings.getPathType(getRawType(property.getParameter(0)), entityType, false);
				String genericKey = w.getGenericName(true, property.getParameter(0));
				classConstant = w.getRawName(property.getParameter(0)) + DOT_CLASS;
				queryType = typeMappings.getPathType(property.getParameter(0), entityType, true);
				printProperty(entityType, property, new ClassType(SetPath.class, getRawType(property.getParameter(0)), genericQueryType),
					w, "this.<" + genericKey + COMMA + w.getGenericName(true, genericQueryType) + ">createSet", classConstant,
					w.getRawName(queryType) + DOT_CLASS, inits);
				break;
			}

			case LIST: {
				Type genericQueryType = typeMappings.getPathType(getRawType(property.getParameter(0)), entityType, false);
				String genericKey = w.getGenericName(true, property.getParameter(0));
				classConstant = w.getRawName(property.getParameter(0)) + DOT_CLASS;
				queryType = typeMappings.getPathType(property.getParameter(0), entityType, true);
				printProperty(entityType, property, new ClassType(ListPath.class, getRawType(property.getParameter(0)), genericQueryType),
					w, "this.<" + genericKey + COMMA + w.getGenericName(true, genericQueryType) + ">createList", classConstant,
					w.getRawName(queryType) + DOT_CLASS, inits);
				break;
			}

			case MAP: {
				String genericKey = w.getGenericName(true, property.getParameter(0));
				String genericValue = w.getGenericName(true, property.getParameter(1));
				Type genericQueryType = typeMappings.getPathType(getRawType(property.getParameter(1)), entityType, false);
				String keyType = w.getRawName(property.getParameter(0));
				String valueType = w.getRawName(property.getParameter(1));
				queryType = typeMappings.getPathType(property.getParameter(1), entityType, true);
				printProperty(entityType, property,
					new ClassType(MapPath.class, getRawType(property.getParameter(0)), getRawType(property.getParameter(1)),
						genericQueryType), w,
					"this.<" + genericKey + COMMA + genericValue + COMMA + w.getGenericName(true, genericQueryType) + ">createMap", keyType
						+ DOT_CLASS, valueType + DOT_CLASS, w.getRawName(queryType) + DOT_CLASS);
				break;
			}

			case ENTITY:
				printEntityProperty(entityType, property, w);
				break;

			}
		}
	}

	/**
	 * Return the raw type for the specified type.
	 */
	private Type getRawType(Type type) {
		if (type instanceof EntityType && type.getPackageName().startsWith("ext.java")) {
			return type;
		} else {
			return new SimpleType(type, type.getParameters());
		}
	}

	/**
	 * Helper method to print a single property.
	 */
	private void printProperty(EntityType entityType, Property property, Type type, CodeWriter w, String factoryMethod, String... args)
		throws IOException {

		// build the field's value, either directly or inherited
		StringBuilder builder = new StringBuilder();
		Supertype supertype = entityType.getSuperType();
		if (property.isInherited() && supertype != null) {
			if (!supertype.getEntityType().hasEntityFields()) {
				builder.append("_super." + property.getEscapedName());
			}
		} else {
			builder.append(factoryMethod + "(\"" + property.getName() + QUOTE);
			for (String arg : args) {
				builder.append(COMMA + arg);
			}
			builder.append(")");
		}

		// print a comment for the field
		if (property.isInherited()) {
			w.line("// inherited");
		} else {

		}

		// print the field itself
		if (builder.length() > 0) {
			w.publicFinal(type, property.getEscapedName(), builder.toString());
		} else {
			w.publicFinal(type, property.getEscapedName());
		}

	}

	/**
	 * Prints a property that has a custom type.
	 */
	private void printCustomProperty(EntityType entityType, Property property, CodeWriter w) throws IOException {
		Type queryType = typeMappings.getPathType(property.getType(), entityType, false);
		boolean inherited = property.isInherited();

		// write a comment for the property
		w.line(inherited ? "// custom, inherited" : "// custom");

		// determine the value
		String value;
		if (inherited) {
			Supertype supertype = entityType.getSuperType();
			if (supertype.getEntityType().hasEntityFields()) {
				value = null;
			} else {
				value = "_super." + property.getEscapedName();
			}
		} else {
			value = NEW + w.getRawName(queryType) + "(forProperty(\"" + property.getName() + "\"))";
		}

		// print the property
		if (value == null) {
			w.publicFinal(queryType, property.getEscapedName());
		} else {
			w.publicFinal(queryType, property.getEscapedName(), value);
		}

	}

	/**
	 * Prints a property that has an entity type.
	 */
	private void printEntityProperty(EntityType entityType, Property property, CodeWriter w) throws IOException {
		Type queryType = typeMappings.getPathType(property.getType(), entityType, false);

		// print a comment for the property
		if (property.isInherited()) {
			w.line("// inherited");
		}

		// print the property itself
		w.publicFinal(queryType, property.getEscapedName());

	}

	/**
	 * Prints model constants for the primary keys.
	 */
	private void printPrimaryKeys(EntityType entityType, final Collection<PrimaryKeyData> keys, final CodeWriter w) throws IOException {
		if (keys == null) {
			return;
		}
		for (PrimaryKeyData primaryKey : keys) {
			String fieldName = namingStrategy.getPropertyNameForPrimaryKey(primaryKey.getName(), entityType);
			
			// TODO: use a custom naming strategy
			fieldName = "pk_" + fieldName;
			
			StringBuilder value = new StringBuilder("createPrimaryKey(");
			boolean first = true;
			for (String column : primaryKey.getColumns()) {
				if (first) {
					first = false;
				} else {
					value.append(", ");
				}
				value.append(namingStrategy.getPropertyName(column, entityType));
			}
			value.append(")");
			Type type = new ClassType(PrimaryKey.class, entityType);
			w.javadoc("Metamodel property for primary key '" + primaryKey.getName() + "'");
			w.publicFinal(type, fieldName, value.toString());
		}
	}

	/**
	 * Prints model constants for the foreign keys (only one direction, either forward or reverse).
	 */
	private void printForeignKeys(EntityType entityType, final Collection<? extends KeyData> keys, final CodeWriter w, boolean reverse)
		throws IOException {
		if (keys == null) {
			return;
		}
		for (KeyData foreignKey : keys) {

			// determine the field name
			String fieldName;
			if (reverse) {
				fieldName = namingStrategy.getPropertyNameForInverseForeignKey(foreignKey.getName(), entityType);
			} else {
				fieldName = namingStrategy.getPropertyNameForForeignKey(foreignKey.getName(), entityType);
			}

			// TODO: use a custom naming strategy
			fieldName = "fk_" + fieldName;
			
			// build the field value
			StringBuilder value = new StringBuilder();
			value.append(reverse ? "createInvForeignKey(" : "createForeignKey(");
			if (foreignKey.getForeignColumns().size() == 1) {
				value.append(namingStrategy.getPropertyName(foreignKey.getForeignColumns().get(0), entityType));
				value.append(", \"" + foreignKey.getParentColumns().get(0) + "\"");
			} else {
				StringBuilder local = new StringBuilder();
				StringBuilder foreign = new StringBuilder();
				for (int i = 0; i < foreignKey.getForeignColumns().size(); i++) {
					if (i > 0) {
						local.append(", ");
						foreign.append(", ");
					}
					local.append(namingStrategy.getPropertyName(foreignKey.getForeignColumns().get(0), entityType));
					foreign.append("\"" + foreignKey.getParentColumns().get(0) + "\"");
				}
				value.append("Arrays.asList(" + local + "), Arrays.asList(" + foreign + ")");
			}
			value.append(")");

			// generate the field
			Type type = new ClassType(ForeignKey.class, foreignKey.getType());
			w.javadoc("Metamodel property for " + (reverse ? "reverse " : "") + "foreign key '" + foreignKey.getName() + "'");
			w.publicFinal(type, fieldName, value.toString());

		}

	}

	/**
	 * Prints Q-class constructors.
	 */
	private void printConstructors(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {

		// preparation
		String localName = w.getRawName(entityType);
		String genericName = w.getGenericName(true, entityType);
		boolean isGeneric = !localName.equals(genericName);
		boolean stringOrBoolean = (entityType.getOriginalCategory() == TypeCategory.STRING || entityType.getOriginalCategory() == TypeCategory.BOOLEAN);
		boolean hasEntityFields = entityType.hasEntityFields();
		String chainedCall = hasEntityFields ? THIS : SUPER;
		String cast = (isGeneric ? "(Class)" : EMPTY);

		// handle additional constructor parameters
		StringBuilder builder = new StringBuilder();
		if (entityType.getData().containsKey("schema")) {
			builder.append(", \"").append(entityType.getData().get("schema")).append("\"");
		} else {
			builder.append(", null");
		}
		builder.append(", \"").append(entityType.getData().get("table")).append("\"");
		String additionalParams = builder.toString();

		// constructor (String variable)
		w.javadoc("Path-variable based constructor.", "@param variable the path variable for this entity");
		if (isGeneric) {
			w.suppressWarnings(UNCHECKED);
		}
		w.beginConstructor(new Parameter("variable", Types.STRING));
		if (stringOrBoolean) {
			w.line(chainedCall, "(forVariable(variable)", hasEntityFields ? "" : additionalParams, ");");
		} else {
			String inits = hasEntityFields ? ", INITS" : EMPTY;
			w.line(chainedCall, "(", cast, localName, ".class, forVariable(variable)", inits, hasEntityFields ? "" : additionalParams, ");");
		}
		w.end();

		// constructor (Path-Type path)
		w.javadoc("Path based constructor", "@param path the path for this entity");
		w.beginConstructor(new Parameter("path", new ClassType(Path.class, entityType.isFinal() ? entityType : new TypeExtends(entityType))));
		if (hasEntityFields) {
			w.line("this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);");
		} else {
			if (stringOrBoolean) {
				w.line("super(path.getMetadata());");
			} else {
				w.line("super(path.getType(), path.getMetadata()", additionalParams, ");");
			}
		}
		w.end();

		// constructor (PathMetadata metadata)
		w.javadoc("Path metadata based constructor", "@param metadata the path metadata for this entity");
		if (isGeneric && !hasEntityFields) {
			w.suppressWarnings(UNCHECKED);
		}
		w.beginConstructor(PATH_METADATA);
		if (hasEntityFields) {
			w.line("this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);");
		} else {
			if (stringOrBoolean) {
				w.line("super(metadata);");
			} else {
				w.line("super(", cast, localName, ".class, metadata", additionalParams, ");");
			}
		}
		w.end();

		// constructor (PathMetadata metadata, PathInits inits)
		if (hasEntityFields) {
			w.javadoc("Path metadata and inits based constructor.", "@param metadata the path metadata for this entity", "@param inits the path inits for this entity");
			if (isGeneric) {
				w.suppressWarnings(UNCHECKED);
			}
			w.beginConstructor(PATH_METADATA, PATH_INITS);
			w.line(THIS, "(", cast, localName, ".class, metadata, inits" + additionalParams + ");");
			w.end();
		}

		// constructor (type, PathMetadata metadata, PathInits inits)
		if (hasEntityFields) {

			// handle the supertype
			Supertype supertype = entityType.getSuperType();
			EntityType supertypeEntity;
			if (supertype != null) {
				supertypeEntity = supertype.getEntityType();
				if (supertypeEntity == null) {
					throw new IllegalStateException("No entity type for " + supertype.getType().getFullName());
				}
			} else {
				supertypeEntity = null;
			}
			boolean supertypeHasEntityFields = (supertypeEntity != null && supertypeEntity.hasEntityFields());

			// generate the constructor
			Type typeParameterType = new ClassType(Class.class, new TypeExtends(entityType));
			w.javadoc("Baseline constructor.", "@param type the type to fetch", "@param metadata the path metadata for this entity", "@param inits the path inits for this entity");
			w.beginConstructor(new Parameter("type", typeParameterType), PATH_METADATA, PATH_INITS);
			w.line("super(type, metadata, inits" + additionalParams + ");");
			if (supertypeHasEntityFields) {
				Type superQueryType = typeMappings.getPathType(supertypeEntity, entityType, false);
				w.line("this._super = new " + w.getRawName(superQueryType) + "(type, metadata, inits);");
			}
			for (Property property : entityType.getProperties()) {
				String propertyName = property.getName();
				Type propertyType = property.getType();
				if (propertyType.getCategory() == TypeCategory.ENTITY) {
					Type queryType = typeMappings.getPathType(propertyType, entityType, false);
					if (!property.isInherited()) {
						boolean propertyTypeHasEntityFields = (propertyType instanceof EntityType && ((EntityType)propertyType).hasEntityFields());
						w.line("this." + property.getEscapedName() + ASSIGN, "inits.isInitialized(\"" + propertyName + "\") ? ", NEW
							+ w.getRawName(queryType) + "(forProperty(\"" + propertyName + "\")",
							propertyTypeHasEntityFields ? (", inits.get(\"" + propertyName + "\")") : EMPTY, ") : null;");
					}
				} else if (property.isInherited() && supertypeHasEntityFields) {
					w.line("this.", property.getEscapedName(), " = _super.", property.getEscapedName(), SEMICOLON);
				}
			}
			w.end();
		}

	}
	
	/**
	 * Prints the delegates (if any)
	 */
	private void printDelegates(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {
		for (Delegate delegate : entityType.getDelegates()) {
			
			// method header
	        Parameter[] params = delegate.getParameters().toArray(new Parameter[delegate.getParameters().size()]);
	        w.beginPublicMethod(delegate.getReturnType(), delegate.getName(), params);

	        // method body
	        w.beginLine(RETURN + delegate.getDelegateType().getSimpleName() + "." + delegate.getName() + "(");
	        w.append("this");
	        if (!entityType.equals(delegate.getDeclaringType())) {
	            int counter = 0;
	            EntityType type = entityType;
	            while (type != null && !type.equals(delegate.getDeclaringType())) {
	                type = type.getSuperType() != null ? type.getSuperType().getEntityType() : null;
	                counter++;
	            }
	            for (int i = 0; i < counter; i++) {
	                w.append("._super");
	            }
	        }
	        for (Parameter parameter : delegate.getParameters()) {
	            w.append(COMMA + parameter.getName());
	        }
	        w.append(");\n");

	        // method end
	        w.end();
	        
		}
	}

	/**
	 * Prints the property accessors
	 */
	private void printPropertyAccessors(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {
		for (Property property : entityType.getProperties()) {
			TypeCategory category = property.getType().getCategory();
			if (category == TypeCategory.MAP && config.useMapAccessors()) {
				printMapAccessor(entityType, property, w);
			} else if (category == TypeCategory.LIST && config.useListAccessors()) {
				printListAccessor(entityType, property, w);
			} else if (category == TypeCategory.ENTITY && config.useEntityAccessors()) {
				printEntityAccessor(entityType, property, w);
			}
		}
	}
	
	/**
	 * Prints a single entity accessor.
	 */
	private void printEntityAccessor(EntityType model, Property field, CodeWriter writer) throws IOException {
		String escapedName = field.getEscapedName();
        Type queryType = typeMappings.getPathType(field.getType(), model, false);
        writer.beginPublicMethod(queryType, escapedName);
        writer.line("if (", escapedName, " == null){");
        writer.line("\t", escapedName, " = new ", writer.getRawName(queryType), "(forProperty(\"", field.getName(), "\"));");
        writer.line("}");
        writer.line(RETURN, escapedName, SEMICOLON);
        writer.end();
    }

	/**
	 * Prints a single list accessor.
	 */
	private void printListAccessor(EntityType model, Property field, CodeWriter writer) throws IOException {
		String escapedName = field.getEscapedName();
		Type queryType = typeMappings.getPathType(field.getParameter(0), model, false);

		writer.beginPublicMethod(queryType, escapedName, new Parameter("index", Types.INT));
		writer.line(RETURN + escapedName + ".get(index);").end();

		writer.beginPublicMethod(queryType, escapedName, new Parameter("index", new ClassType(Expression.class, Types.INTEGER)));
		writer.line(RETURN + escapedName + ".get(index);").end();
	}

	/**
	 * Prints a single map accessor.
	 */
	private void printMapAccessor(EntityType model, Property field, CodeWriter writer) throws IOException {
		String escapedName = field.getEscapedName();
		Type queryType = typeMappings.getPathType(field.getParameter(1), model, false);

		writer.beginPublicMethod(queryType, escapedName, new Parameter("key", field.getParameter(0)));
		writer.line(RETURN + escapedName + ".get(key);").end();

		writer.beginPublicMethod(queryType, escapedName, new Parameter("key", new ClassType(Expression.class, field.getParameter(0))));
		writer.line(RETURN + escapedName + ".get(key);").end();
	}
	
}
