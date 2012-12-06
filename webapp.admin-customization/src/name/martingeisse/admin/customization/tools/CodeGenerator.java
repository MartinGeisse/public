/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.tools;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import name.martingeisse.common.database.config.CustomMysqlQuerydslConfiguration;
import name.martingeisse.tools.codegen.BeanSerializer;
import name.martingeisse.tools.codegen.MetaDataSerializer;

import org.joda.time.DateTimeZone;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.codegen.MetaDataExporter;

/**
 * This tool generates QueryDSL classes.
 */
public class CodeGenerator {

	/**
	 * The main method.
	 * @param args ...
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		BeanSerializer beanSerializer = new BeanSerializer(true);
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/phorum?zeroDateTimeBehavior=convertToNull&useTimezone=false", "root", "");
		MetaDataExporter exporter = new MetaDataExporter();
		exporter.setTargetFolder(new File("generated"));
		exporter.setPackageName("phorum");
		exporter.setSerializerClass(MetaDataSerializer.class);
		exporter.setBeanSerializer(beanSerializer);
		// exporter.setConfiguration(new CustomMysqlQuerydslConfiguration(new MySQLTemplates(), null));
		exporter.setConfiguration(new CustomMysqlQuerydslConfiguration(new MySQLTemplates(), DateTimeZone.forID("Europe/Moscow")));
		exporter.export(connection.getMetaData());
		connection.close();
	}
	
}
