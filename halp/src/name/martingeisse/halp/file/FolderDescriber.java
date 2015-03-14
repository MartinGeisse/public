/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.halp.file;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import name.martingeisse.halp.Assessment;
import name.martingeisse.halp.DescribeProblem;
import name.martingeisse.halp.ProblemAssessor;
import name.martingeisse.halp.StringAssessment;

import org.w3c.dom.Document;

/**
 * This class is able to describe folders, but not files.
 */
public class FolderDescriber implements ProblemAssessor<DescribeProblem<File>> {

	/* (non-Javadoc)
	 * @see name.martingeisse.halp.ProblemAssessor#assess(name.martingeisse.halp.Problem)
	 */
	@Override
	public Assessment assess(DescribeProblem<File> problem) {
		File file = problem.getThing();
		if (!file.isDirectory()) {
			return null;
		}
		StringBuilder builder = new StringBuilder();

		builder.append("This is a folder.\n");
		builder.append("Relative path: " + file.getPath() + "\n");
		file = file.getAbsoluteFile();
		builder.append("Absolute path: " + file.getPath() + "\n");
		try {
			file = file.getCanonicalFile();
			builder.append("Canonical path: " + file.getCanonicalPath() + "\n");
		} catch (IOException e) {
			builder.append("Could not determine canonical path: " + e.getMessage() + "\n");
		}
		builder.append("\n");

		if (new File(file, ".project").isFile()) {
			builder.append("It looks like an Eclipse project.\n");

			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.parse(new File(file, ".project"));
				XPath xPath = XPathFactory.newInstance().newXPath();
				String name = (String)xPath.evaluate("string(/projectDescription/name)", document, XPathConstants.STRING);
				builder.append("Project name: " + name + "\n");

				boolean isJavaProject = (xPath.evaluate("/projectDescription/natures/nature[string(self::*)='org.eclipse.jdt.core.javanature']", document, XPathConstants.NODE) != null);
				if (isJavaProject) {
					builder.append("This looks like a Java project.\n");
				}
				
			} catch (Exception e) {
				builder.append("But parsing its .project file failed: " + e.getMessage());
			}

			// TODO extension point: further sub-describers for Eclipse projects
			builder.append("\n");
		}

		// TODO extension point: further sub-describers for folders
		return new StringAssessment(builder.toString());
	}

}
