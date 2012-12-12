/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.resources;

/**
 * TODO: document me
 *
 */
public class MarkerUtil {

	TODO: Marker-Tabelle, Codegen, SQL-Init-Script
	Marker-Eingenschaften von Eclipse
		origin: JAVAC
		meaning: WARNING, ERROR
		file_id
		line, col
		message
		-> so ok? müsste fürs erste passen
			
	util zum abrufen
	util zum ersetzen je nach origin
	Model zum abrufen (parameter: {file, origins, meanings} (zum filtern), accept() (zum in-memory filtern))
	ListView zum anzeigen (Icon, Texte)
		
	setzen beim compile
	anzeigen per ListView/Model
	compile bei save
	run getrennt per button
	
}
