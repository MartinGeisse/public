/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.papyros.frontend.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import name.martingeisse.common.terms.IConsumer;
import name.martingeisse.slave_services.common.frontend.AbstractFrontendPage;
import name.martingeisse.slave_services.common.frontend.components.CompilerMarkerListPanel;
import name.martingeisse.slave_services.entity.QTemplateFamily;
import name.martingeisse.slave_services.entity.TemplateFamily;
import name.martingeisse.slave_services.papyros.backend.PapyrosDataUtil;
import name.martingeisse.slave_services.papyros.frontend.family.TemplateFamilyPage;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.wicket.component.codemirror.CodeMirrorBehavior;
import name.martingeisse.wicket.component.codemirror.compile.CodeMirrorAutocompileBehavior;
import name.martingeisse.wicket.component.codemirror.compile.CompilerMarker;
import name.martingeisse.wicket.component.codemirror.compile.CompilerMarkerErrorLevelComparator;
import name.martingeisse.wicket.component.codemirror.compile.CompilerResult;
import name.martingeisse.wicket.component.codemirror.compile.ICompiler;
import name.martingeisse.wicket.component.codemirror.modes.CodeMirrorModes;
import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * This page allows to edit a schema.
 */
public class SchemaPage extends AbstractFrontendPage {

	/**
	 * the editableContent
	 */
	private String editableContent;

	/**
	 * the compilerMarkers
	 */
	private List<CompilerMarker> compilerMarkers;
	
	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public SchemaPage(final PageParameters pageParameters) {
		super(pageParameters);
		final TemplateFamily templateFamily = PapyrosDataUtil.loadTemplateFamily(pageParameters);
		this.editableContent = templateFamily.getSchema();
		
		add(new BookmarkablePageLink<>("templateFamilyLink", TemplateFamilyPage.class, new PageParameters().add("key", templateFamily.getKey()).add("tab", ".schema")));
		add(new Label("templateFamilyName", templateFamily.getName()));
		add(new Label("templateFamilyKey", templateFamily.getKey()));
		
		//
		final SchemaAutocompiler schemaAutocompiler = new SchemaAutocompiler();
		setCompilerMarkersFromResult(ICompiler.Util.compileSafe(schemaAutocompiler, editableContent));
		
		// build the edit form and CodeMirror
		final Form<Void> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				final QTemplateFamily q = QTemplateFamily.templateFamily;
				final SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(q);
				update.set(q.schema, editableContent).where(q.id.eq(templateFamily.getId()));
				update.execute();
				setResponsePage(TemplateFamilyPage.class, new PageParameters().add("key", templateFamily.getKey()).add("tab", ".schema"));
			}
		};
		final CodeMirrorBehavior codeMirrorBehavior = new CodeMirrorBehavior(CodeMirrorModes.JSON);
		final CodeMirrorAutocompileBehavior autocompileBehavior = new CodeMirrorAutocompileBehavior(schemaAutocompiler);
		form.add(new TextArea<>("textarea", new PropertyModel<>(this, "content")).add(codeMirrorBehavior).add(autocompileBehavior));
		add(form);

		// build the markers list
		CompilerMarkerListPanel compilerMarkersListPanel = new CompilerMarkerListPanel("compilerMarkers", new PropertyModel<List<CompilerMarker>>(this, "compilerMarkers"));
		add(compilerMarkersListPanel);
		autocompileBehavior.setResultConsumer(new CompilerResultConsumer(compilerMarkersListPanel));
		
	}

	/**
	 * Getter method for the content.
	 * @return the content
	 */
	public String getContent() {
		return editableContent;
	}

	/**
	 * Setter method for the content.
	 * @param content the content to set
	 */
	public void setContent(final String content) {
		this.editableContent = content;
	}

	/**
	 * Getter method for the compilerMarkers.
	 * @return the compilerMarkers
	 */
	public List<CompilerMarker> getCompilerMarkers() {
		return compilerMarkers;
	}
	
	/**
	 * 
	 */
	private void setCompilerMarkersFromResult(final CompilerResult compilerResult) {
		compilerMarkers = new ArrayList<>(compilerResult.getMarkers());
		Collections.sort(compilerMarkers, new CompilerMarkerErrorLevelComparator());
	}

	/**
	 * Consumes the compiler result after auto-compilation, in addition to the
	 * CodeMirror component adding markers in the editor automatically.
	 */
	private class CompilerResultConsumer implements IConsumer<CompilerResult>, Serializable {

		/**
		 * the compilerMarkersContainer
		 */
		private final WebMarkupContainer compilerMarkersContainer;

		/**
		 * Constructor.
		 * @param compilerMarkersContainer the container for the markers list
		 */
		public CompilerResultConsumer(final WebMarkupContainer compilerMarkersContainer) {
			this.compilerMarkersContainer = compilerMarkersContainer;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.common.terms.IConsumer#consume(java.lang.Object)
		 */
		@Override
		public void consume(final CompilerResult compilerResult) {
			setCompilerMarkersFromResult(compilerResult);
			AjaxRequestUtil.markForRender(compilerMarkersContainer);
		}

	}
	
}
