/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.Files;
import name.martingeisse.webide.entity.QFiles;
import name.martingeisse.webide.java.MemoryFileManager;
import name.martingeisse.webide.java.MemoryJavaFileObject;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * The main workbench page.
 */
public class WorkbenchPage extends WebPage {

	/**
	 * the selectedFilename
	 */
	private String selectedFilename;

	/**
	 * the editorContents
	 */
	private String editorContents;

	/**
	 * the log
	 */
	private String log;

	/**
	 * Constructor.
	 */
	public WorkbenchPage() {

		final ListView<String> filesList = new ListView<String>("files", new PropertyModel<List<String>>(this, "filenames")) {
			@Override
			protected void populateItem(final ListItem<String> item) {
				final String filename = item.getModelObject();

				final WebMarkupContainer container = new WebMarkupContainer("file");
				container.add(new Label("name", filename));
				if (filename.equals(selectedFilename)) {
					item.add(new AttributeAppender("style", Model.of("background-color: #88f"), ", "));
				}
				item.add(container);

				container.add(new AjaxEventBehavior("click") {
					@Override
					protected void onEvent(final AjaxRequestTarget target) {
						selectedFilename = filename;
						loadEditorContents();
						target.add(WorkbenchPage.this);
					}
				});

			}
		};
		add(filesList);
		setOutputMarkupId(true);

		final Form<Void> editorForm = new Form<Void>("editorForm") {
			@Override
			protected void onSubmit() {
				final SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QFiles.files);
				update.where(QFiles.files.name.eq(selectedFilename));
				update.set(QFiles.files.contents, (editorContents == null ? "" : editorContents).getBytes(Charset.forName("utf-8")));
				update.execute();
			}
		};
		editorForm.add(new TextArea<String>("editorArea", new PropertyModel<String>(this, "editorContents")));
		add(editorForm);

		final Form<Void> runForm = new Form<Void>("runForm") {
			@Override
			protected void onSubmit() {

				// obtain the standard file manager so we can include the boot classpath
				final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
				final DiagnosticCollector<JavaFileObject> diagnosticListener = new DiagnosticCollector<JavaFileObject>();
				final Locale locale = null;
				StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnosticListener, locale, Charset.forName("utf-8"));
				
				// fetch and wrap source code files as JavaFileObjects
				final List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
				final MemoryFileManager fileManager = new MemoryFileManager(standardFileManager);
				final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
				for (final Files fileRecord : query.from(QFiles.files).list(QFiles.files)) {
					String name = fileRecord.getName();
					JavaFileObject fileObject = new MemoryJavaFileObject(name, fileRecord.getContents());
					javaFiles.add(fileObject);				
					fileManager.getInputFiles().put(name, fileObject);
				}

				// run the java compiler
				final CompilationTask task = compiler.getTask(null, fileManager, diagnosticListener, null, null, javaFiles);
				final boolean success = task.call();

				// write the compilation log
				final StringBuilder builder = new StringBuilder();
				final List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticListener.getDiagnostics();
				for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
					builder.append("in line ").append(diagnostic.getLineNumber()).append(", column ").append(diagnostic.getColumnNumber())
						.append(": \n");
					builder.append(diagnostic.getMessage(locale)).append('\n');
					builder.append('\n');
				}
				builder.append("success: ").append(success).append('\n');

				// run the generated application
				String[] commandTokens = new String[] {
					"java",
					"-cp",
					"lib/applauncher/code:lib/applauncher/lib/mysql-connector-java-5.1.20-bin.jar",
					"name.martingeisse.webide.tools.AppLauncher",
					selectedFilename,
				};
				try {
					Process process = Runtime.getRuntime().exec(commandTokens);
					process.getOutputStream().close();
					builder.append(IOUtils.toString(process.getInputStream()));
					builder.append(IOUtils.toString(process.getErrorStream()));
					process.waitFor();
				} catch (Exception e) {
					builder.append(e.toString());
				}

				// store the log for rendering
				log = builder.toString();
				
			}
		};
		add(runForm);

		add(new Label("log", new PropertyModel<String>(this, "log")));
	}

	/**
	 * Returns the file names.
	 * @return the file names.
	 */
	public List<String> getFilenames() {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(QFiles.files).list(QFiles.files.name);
	}

	/**
	 * 
	 */
	private void loadEditorContents() {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		final Object resultObject = query.from(QFiles.files).where(QFiles.files.name.eq(selectedFilename))
			.singleResult(QFiles.files.contents);
		final byte[] encodedContents = (byte[])(((Object[])resultObject)[0]);
		editorContents = new String(encodedContents, Charset.forName("utf-8"));
	}

	/**
	 * Getter method for the selectedFilename.
	 * @return the selectedFilename
	 */
	public String getSelectedFilename() {
		return selectedFilename;
	}

	/**
	 * Setter method for the selectedFilename.
	 * @param selectedFilename the selectedFilename to set
	 */
	public void setSelectedFilename(final String selectedFilename) {
		this.selectedFilename = selectedFilename;
	}

	/**
	 * Getter method for the editorContents.
	 * @return the editorContents
	 */
	public String getEditorContents() {
		return editorContents;
	}

	/**
	 * Setter method for the editorContents.
	 * @param editorContents the editorContents to set
	 */
	public void setEditorContents(final String editorContents) {
		this.editorContents = editorContents;
	}

	/**
	 * Getter method for the log.
	 * @return the log
	 */
	public String getLog() {
		return log;
	}

	/**
	 * Setter method for the log.
	 * @param log the log to set
	 */
	public void setLog(final String log) {
		this.log = log;
	}

}
