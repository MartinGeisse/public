/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.tools;

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
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/miner?zeroDateTimeBehavior=convertToNull&useTimezone=false&characterEncoding=utf8&characterSetResults=utf8", "root", "");
		MetaDataExporter exporter = new MetaDataExporter();
		exporter.setTargetFolder(new File("generated"));
		exporter.setPackageName("name.martingeisse.webide.entity");
		exporter.setSerializerClass(MetaDataSerializer.class);
		exporter.setBeanSerializer(new BeanSerializer(false, false));
		exporter.setConfiguration(new CustomMysqlQuerydslConfiguration(new MySQLTemplates(), DateTimeZone.forID("Europe/Berlin")));
		exporter.export(connection.getMetaData());
		connection.close();
	}
	
}
