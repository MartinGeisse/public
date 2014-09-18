/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.application.tools;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import name.martingeisse.sql.codegen.BeanSerializer;
import name.martingeisse.sql.codegen.MetaDataSerializer;
import name.martingeisse.sql.config.CustomMysqlQuerydslConfiguration;

import org.joda.time.DateTimeZone;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.codegen.MetaDataExporter;


/**
 * Generates QueryDSL classes.
 */
public class QueryDslCodeGeneratorMain {

	/**
	 * The main method.
	 * @param args ...
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/slave_services?zeroDateTimeBehavior=convertToNull&useTimezone=false&characterEncoding=utf8&characterSetResults=utf8", "root", "")) {
			MetaDataExporter exporter = new MetaDataExporter();
			exporter.setTargetFolder(new File("src/generated/java"));
			exporter.setPackageName("name.martingeisse.slave_services.entity");
			exporter.setSerializerClass(MetaDataSerializer.class);
			exporter.setBeanSerializer(new BeanSerializer(true, false));
			exporter.setConfiguration(new CustomMysqlQuerydslConfiguration(new MySQLTemplates(), DateTimeZone.forID("Europe/Berlin")));
			exporter.export(connection.getMetaData());
		}
	}
	
}
