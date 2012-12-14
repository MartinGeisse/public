/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.Files;
import name.martingeisse.webide.entity.QFiles;
import name.martingeisse.webide.java.IMemoryFileObject;
import name.martingeisse.webide.java.IMemoryJavaFileObject;
import name.martingeisse.webide.java.MemoryFileManager;
import name.martingeisse.webide.java.MemoryJavaFileObject;
import name.martingeisse.webide.java.codemirror.JavaTextArea;
import name.martingeisse.webide.resources.MarkerData;
import name.martingeisse.webide.resources.MarkerDatabaseUtil;
import name.martingeisse.webide.resources.MarkerListView;
import name.martingeisse.webide.resources.MarkerMeaning;
import name.martingeisse.webide.resources.MarkerOrigin;
import name.martingeisse.webide.resources.ResourceIconSelector;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ResourceReference;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
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
				container.add(new Image("icon", new AbstractReadOnlyModel<ResourceReference>() {
					@Override
					public ResourceReference getObject() {
						return ResourceIconSelector.FILE_OK.getResourceReference();
					}
				}));
				container.add(new Label("name", filename));
				if (filename.equals(selectedFilename)) {
					item.add(new AttributeAppender("class", Model.of("selected"), " "));
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

		add(new MarkerListView("markers", null, 30) {
			@Override
			protected void populateItem(ListItem<MarkerData> item) {
				addMeaningIcon(item, "icon", item.getModel());
				addMeaningLabel(item, "meaning", item.getModel());
				addMessageLabel(item, "message", item.getModel());
			}
		});

		final Form<Void> editorForm = new Form<Void>("editorForm") {
			@Override
			protected void onSubmit() {
				final SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QFiles.files);
				update.where(QFiles.files.name.eq(selectedFilename));
				update.set(QFiles.files.contents, (editorContents == null ? "" : editorContents).getBytes(Charset.forName("utf-8")));
				update.execute();
			}
		};
		editorForm.add(new AjaxButton("submit", editorForm) {
		});
		editorForm.add(new JavaTextArea("editorArea", new PropertyModel<String>(this, "editorContents")));
		// editorForm.add(new TextArea<String>("editorArea", new PropertyModel<String>(this, "editorContents")));
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
				final List<Long> sourceFileIds = new ArrayList<Long>();
				final Map<String, Long> sourceFileNameToId = new HashMap<String, Long>();
				final List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
				final MemoryFileManager fileManager = new MemoryFileManager(standardFileManager);
				final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
				for (final Files fileRecord : query.from(QFiles.files).where(QFiles.files.name.like("%.java")).list(QFiles.files)) {
					sourceFileIds.add(fileRecord.getId());
					sourceFileNameToId.put(fileRecord.getName(), fileRecord.getId());
					String name = fileRecord.getName();
					IMemoryJavaFileObject fileObject = new MemoryJavaFileObject(name, fileRecord.getContents());
					javaFiles.add(fileObject);
					fileManager.getInputFiles().put(name, fileObject);
				}

				// run the java compiler
				final CompilationTask task = compiler.getTask(null, fileManager, diagnosticListener, null, null, javaFiles);
				final boolean success = task.call();

				// save the class files in the database
				for (IMemoryFileObject file : fileManager.getOutputFiles().values()) {
					String filename = file.getName();
					SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QFiles.files);
					delete.where(QFiles.files.name.eq(filename)).execute();
					SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QFiles.files);
					insert.set(QFiles.files.name, filename);
					insert.set(QFiles.files.contents, file.getBinaryContent());
					insert.execute();
				}

				// collect diagnostic messages per source file
				final List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticListener.getDiagnostics();
				final Map<JavaFileObject, List<Diagnostic<? extends JavaFileObject>>> sourceFileToDiagnostics = new HashMap<JavaFileObject, List<Diagnostic<? extends JavaFileObject>>>();
				for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
					JavaFileObject currentFile = diagnostic.getSource();
					List<Diagnostic<? extends JavaFileObject>> currentFileDiagnostics = sourceFileToDiagnostics.get(currentFile);
					if (currentFileDiagnostics == null) {
						currentFileDiagnostics = new ArrayList<Diagnostic<? extends JavaFileObject>>();
						sourceFileToDiagnostics.put(currentFile, currentFileDiagnostics);
					}
					currentFileDiagnostics.add(diagnostic);
				}

				// generate markers for the diagnostic messages
				MarkerDatabaseUtil.removeMarkersForFile(sourceFileIds, MarkerOrigin.JAVAC);
				for (Map.Entry<JavaFileObject, List<Diagnostic<? extends JavaFileObject>>> fileEntry : sourceFileToDiagnostics.entrySet()) {
					String filename = fileEntry.getKey().getName();
					long fileId = sourceFileNameToId.get(filename);
					for (Diagnostic<? extends JavaFileObject> diagnostic : fileEntry.getValue()) {

						// convert the diagnostic kind to a marker meaning (skip this diagnostic if the kind is unknown)
						Kind diagnosticKind = diagnostic.getKind();
						MarkerMeaning meaning;
						if (diagnosticKind == Kind.ERROR) {
							meaning = MarkerMeaning.ERROR;
						} else if (diagnosticKind == Kind.WARNING || diagnosticKind == Kind.MANDATORY_WARNING) {
							meaning = MarkerMeaning.WARNING;
						} else {
							continue;
						}

						// create the marker
						MarkerData markerData = new MarkerData();
						markerData.setOrigin(MarkerOrigin.JAVAC);
						markerData.setMeaning(meaning);
						markerData.setLine(diagnostic.getLineNumber());
						markerData.setColumn(diagnostic.getColumnNumber());
						markerData.setMessage(diagnostic.getMessage(null));
						markerData.insertIntoDatabase(fileId);

					}
				}

				// write the compilation log
				final StringBuilder builder = new StringBuilder();
				builder.append("builder success: ").append(success).append('\n');

				// if the build was successful, run the generated application
				if (success) {
					String className = (selectedFilename.endsWith(".java") ? selectedFilename.substring(0, selectedFilename.length() - 5) : selectedFilename);
					String[] commandTokens = new String[] {
						"java", "-cp", "lib/applauncher/code:lib/applauncher/lib/mysql-connector-java-5.1.20-bin.jar", "name.martingeisse.webide.tools.AppLauncher", className,
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
		final Object resultObject = query.from(QFiles.files).where(QFiles.files.name.eq(selectedFilename)).singleResult(QFiles.files.contents);
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