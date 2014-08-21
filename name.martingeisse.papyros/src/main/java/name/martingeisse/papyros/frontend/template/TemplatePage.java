/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.martingeisse.common.terms.IConsumer;
import name.martingeisse.papyros.backend.PapyrosDataUtil;
import name.martingeisse.papyros.backend.RenderTemplateAction;
import name.martingeisse.papyros.entity.PreviewDataSet;
import name.martingeisse.papyros.entity.QTemplate;
import name.martingeisse.papyros.entity.Template;
import name.martingeisse.papyros.entity.TemplateFamily;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
import name.martingeisse.papyros.frontend.components.CompilerMarkerListPanel;
import name.martingeisse.papyros.frontend.components.Iframe;
import name.martingeisse.papyros.frontend.components.PageParameterDrivenTabPanel;
import name.martingeisse.papyros.frontend.family.TemplateFamilyPage;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.wicket.component.codemirror.CodeMirrorBehavior;
import name.martingeisse.wicket.component.codemirror.compile.CodeMirrorAutocompileBehavior;
import name.martingeisse.wicket.component.codemirror.compile.CompilerMarker;
import name.martingeisse.wicket.component.codemirror.compile.CompilerMarkerErrorLevelComparator;
import name.martingeisse.wicket.component.codemirror.compile.CompilerResult;
import name.martingeisse.wicket.component.codemirror.compile.ICompiler;
import name.martingeisse.wicket.component.codemirror.modes.CodeMirrorModes;
import name.martingeisse.wicket.util.AjaxRequestUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.json.simple.JSONValue;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * The main page to manage a template.
 * 
 * TODO edit ohne save, dann reload -- editor-inhalt enthält noch das original, aber preview die editierte version.
 * sollte einheitlich sein.
 */
public class TemplatePage extends AbstractFrontendPage {

	/**
	 * the editableContent
	 */
	private String editableContent;

	/**
	 * the previewDataSet
	 */
	private PreviewDataSet previewDataSet;

	/**
	 * the parsedPreviewData
	 */
	private Object parsedPreviewData;

	/**
	 * the compilerMarkers
	 */
	private List<CompilerMarker> compilerMarkers;

	/**
	 * the renderedPreview
	 */
	private String renderedPreview;

	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public TemplatePage(final PageParameters pageParameters) {
		super(pageParameters);
		final Pair<Template, TemplateFamily> pair = PapyrosDataUtil.loadTemplateAndTemplateFamily(pageParameters);
		final Template template = pair.getLeft();
		final TemplateFamily family = pair.getRight();
		this.editableContent = template.getContent();

		add(new BookmarkablePageLink<>("templateFamilyLink", TemplateFamilyPage.class, new PageParameters().add("key", family.getKey())));
		add(new Label("templateFamilyName", family.getName()));
		add(new Label("templateFamilyKey", family.getKey()));
		add(new Label("languageKey", template.getLanguageKey()));

		final PageParameterDrivenTabPanel tabPanel = new PageParameterDrivenTabPanel("tabs", "tab") {
			@Override
			protected Component createBody(String id, String selector) {
				if (selector.equals(".preview")) {

					loadPreviewData(template.getTemplateFamilyId());
					renderPreview(editableContent);
					final Fragment fragment = new Fragment(id, "tabPreview", TemplatePage.this);
					fragment.add(new Iframe("iframe", Model.of(renderedPreview)));
					return fragment;

				} else if (selector.equals(".edit")) {

					//
					final TemplateAutocompiler templateAutocompiler = new TemplateAutocompiler();
					loadPreviewData(template.getTemplateFamilyId());
					renderPreview(editableContent);
					setCompilerMarkersFromResult(ICompiler.Util.compileSafe(templateAutocompiler, editableContent));
					
					final Fragment fragment = new Fragment(id, "tabEdit", TemplatePage.this);
					
					// build the edit form and CodeMirror
					final Form<Void> form = new Form<Void>("form") {
						@Override
						protected void onSubmit() {
							final QTemplate q = QTemplate.template;
							final SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(q);
							update.set(q.content, editableContent).where(q.id.eq(template.getId()));
							update.execute();
							setResponsePage(TemplatePage.class, createTabLinkPageParameters(".preview"));
						}
					};
					final CodeMirrorBehavior codeMirrorBehavior = new CodeMirrorBehavior(CodeMirrorModes.JAVASCRIPT);
					final CodeMirrorAutocompileBehavior autocompileBehavior = new CodeMirrorAutocompileBehavior(templateAutocompiler);
					form.add(new TextArea<>("textarea", new PropertyModel<>(TemplatePage.this, "content")).add(codeMirrorBehavior).add(autocompileBehavior));
					fragment.add(form);

					// build the preview
					Iframe previewIframe = new Iframe("previewIframe", new PropertyModel<>(TemplatePage.this, "renderedPreview"));
					previewIframe.setOutputMarkupId(true);
					fragment.add(previewIframe);
					
					// build the markers list
					CompilerMarkerListPanel compilerMarkersListPanel = new CompilerMarkerListPanel("compilerMarkers", new PropertyModel<List<CompilerMarker>>(TemplatePage.this, "compilerMarkers"));
					fragment.add(compilerMarkersListPanel);
					autocompileBehavior.setResultConsumer(new CompilerResultConsumer(previewIframe, compilerMarkersListPanel));
					
					return fragment;

				} else {
					return new EmptyPanel(id);
				}
			}
		};
		tabPanel.addTab("Preview", ".preview");
		tabPanel.addTab("Edit", ".edit");
		add(tabPanel);

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
	 * Getter method for the renderedPreview.
	 * @return the renderedPreview
	 */
	public String getRenderedPreview() {
		return renderedPreview;
	}

	/**
	 * 
	 */
	private void loadPreviewData(final long templateFamilyId) {
		previewDataSet = PapyrosDataUtil.loadFirstPreviewDataSet(templateFamilyId);
		parsedPreviewData = (previewDataSet == null ? null : JSONValue.parse(previewDataSet.getData()));
	}

	/**
	 * 
	 */
	private void renderPreview(String document) {
		final RenderTemplateAction renderTemplateAction = new RenderTemplateAction();
		renderTemplateAction.setTemplate(document);
		renderTemplateAction.setData(parsedPreviewData);
		renderedPreview = renderTemplateAction.render();
	}
	
	/**
	 * Consumes the compiler result after auto-compilation, in addition to the
	 * CodeMirror component adding markers in the editor automatically.
	 */
	private class CompilerResultConsumer implements IConsumer<CompilerResult>, Serializable {

		/**
		 * the previewIframe
		 */
		private final Iframe previewIframe;
		
		/**
		 * the compilerMarkersContainer
		 */
		private final WebMarkupContainer compilerMarkersContainer;

		/**
		 * Constructor.
		 * @param previewIframe the iframe used for preview
		 * @param compilerMarkersContainer the container for the markers list
		 */
		public CompilerResultConsumer(final Iframe previewIframe, final WebMarkupContainer compilerMarkersContainer) {
			this.previewIframe = previewIframe;
			this.compilerMarkersContainer = compilerMarkersContainer;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.common.terms.IConsumer#consume(java.lang.Object)
		 */
		@Override
		public void consume(final CompilerResult compilerResult) {
			setCompilerMarkersFromResult(compilerResult);
			AjaxRequestUtil.markForRender(compilerMarkersContainer);
			renderPreview(compilerResult.getDocument());
			previewIframe.renderReloadScript();
		}

	}
}
