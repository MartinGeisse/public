/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.schema;

/**
 * This class captures the overall schema of the whole application data.
 * 
 * This is a singleton class that is initialized once at startup. The instance
 * is initialized and flushed to global memory, then referenced by a volatile
 * variable to avoid synchronization when using the data. The schema must not
 * be modified after initialization!
 */
public class ApplicationSchema extends AbstractApplicationSchema {

	/**
	 * the instance
	 */
	public static volatile ApplicationSchema instance;
	
	/**
	 * Initializes the application schema.
	 */
	public static void initialize() {
		ApplicationSchema schema = new ApplicationSchema();
		
		// synchronize on the schema to ensure that the thread's cache is emptied afterwards
		synchronized(schema) {
			schema.define();
			schema.initializeImplicitSchema();
			schema.prepare();
		}
		
		// store the instance -- the 'instance' variable is volatile to avoid caching
		instance = schema;
		
	}

	// ------------------------------------------------------------------------------------
	
	/**
	 * the mainDatabase
	 */
	private DatabaseDescriptor mainDatabase;
	
	/**
	 * Defines the application schema.
	 */
	private void define() {
		defineDatabases();
		defineExplicitMainDatabaseEntities();
	}
	
	/**
	 * Defines the application's databases.
	 */
	private void defineDatabases() {
	
		// define the main database
		mainDatabase = new DatabaseDescriptor();
		mainDatabase.setDisplayName("leckerMittag");
		getDatabaseDescriptors().add(mainDatabase);
		
	}
	
	/**
	 * Defines the explicit entity descriptors for the main database. 
	 */
	private void defineExplicitMainDatabaseEntities() {
		
	}
	
}
