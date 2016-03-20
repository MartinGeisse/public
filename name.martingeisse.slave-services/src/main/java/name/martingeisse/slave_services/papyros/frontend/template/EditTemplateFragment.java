/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.papyros.frontend.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import name.martingeisse.common.terms.IConsumer;
import name.martingeisse.slave_services.common.frontend.components.CompilerMarkerListPanel;
import name.martingeisse.slave_services.entity.QTemplate;
import name.martingeisse.slave_services.entity.Template;
import name.martingeisse.slave_services.entity.TemplateFamily;
import name.martingeisse.slave_services.entity.TemplatePreviewDataSet;
import name.martingeisse.slave_services.papyros.backend.PapyrosDataUtil;
import name.martingeisse.slave_services.papyros.frontend.components.PreviewTemplateIframe;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.wicket.component.codemirror.CodeMirrorBehavior;
import name.martingeisse.wicket.component.codemirror.compile.CodeMirrorAutocompileBehavior;
import name.martingeisse.wicket.component.codemirror.compile.CompilerMarker;
import name.martingeisse.wicket.component.codemirror.compile.CompilerMarkerErrorLevelComparator;
import name.martingeisse.wicket.component.codemirror.compile.CompilerResult;
import name.martingeisse.wicket.component.codemirror.compile.ICompiler;
import name.martingeisse.wicket.component.codemirror.modes.CodeMirrorModes;
import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * Implements the "edit" tab on the template page.
 */
public abstract class EditTemplateFragment extends Fragment {

	/**
	 * the editableContent
	 */
	private String editableContent;

	/**
	 * the previewDataSet
	 */
	private TemplatePreviewDataSet previewDataSet;

	/**
	 * the compilerMarkers
	 */
	private List<CompilerMarker> compilerMarkers;

	/**
	 * Constructor.
	 * @param id the wicket id of the fragment
	 * @param markupId the wicket id that locates the markup
	 * @param markupProvider the parent container that contains the markup
	 * @param family the template family
	 * @param template the template
	 */
	public EditTemplateFragment(String id, String markupId, MarkupContainer markupProvider, final TemplateFamily family, final Template template) {
		super(id, markupId, markupProvider);
		this.editableContent = template.getContent();

		// build the drop-down menu for the preview data set
		final IModel<TemplatePreviewDataSet> previewDataSetModel = new PropertyModel<>(this, "previewDataSet");
		final IModel<List<TemplatePreviewDataSet>> previewDataSetListModel = new LoadableDetachableModel<List<TemplatePreviewDataSet>>() {
			@Override
			protected List<TemplatePreviewDataSet> load() {
				return PapyrosDataUtil.loadPreviewDataSetList(family.getId(), true);
			}
		};
		IChoiceRenderer<TemplatePreviewDataSet> previewDataSetRenderer = new IChoiceRenderer<TemplatePreviewDataSet>() {

			@Override
			public String getIdValue(TemplatePreviewDataSet object, int index) {
				return object.getId().toString();
			}

			@Override
			public Object getDisplayValue(TemplatePreviewDataSet object) {
				return object.getName();
			}

		};
		DropDownChoice<?> previewDataDropDownChoice = new DropDownChoice<>("previewDataSetDropdown", previewDataSetModel, previewDataSetListModel, previewDataSetRenderer);
		previewDataDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("change") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				((PreviewTemplateIframe)EditTemplateFragment.this.get("previewIframe")).renderReloadScript();
			}
		});
		add(previewDataDropDownChoice);
		this.previewDataSet = (previewDataSetListModel.getObject().isEmpty() ? null : previewDataSetListModel.getObject().get(0));

		// prepare the auto-compiler
		final TemplateAutocompiler templateAutocompiler = new TemplateAutocompiler();
		setCompilerMarkersFromResult(ICompiler.Util.compileSafe(templateAutocompiler, editableContent));

		// build the edit form and CodeMirror
		final Form<Void> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				final QTemplate q = QTemplate.template;
				final SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(q);
				update.set(q.content, editableContent).where(q.id.eq(template.getId()));
				update.execute();
				EditTemplateFragment.this.onSubmit();
			}
		};
		final CodeMirrorBehavior codeMirrorBehavior = new CodeMirrorBehavior(CodeMirrorModes.JAVASCRIPT);
		final CodeMirrorAutocompileBehavior autocompileBehavior = new CodeMirrorAutocompileBehavior(templateAutocompiler);
		form.add(new TextArea<>("textarea", new PropertyModel<>(this, "editableContent")).add(codeMirrorBehavior).add(autocompileBehavior));
		add(form);

		// build the preview
		PreviewTemplateIframe previewIframe = new PreviewTemplateIframe("previewIframe", new PropertyModel<String>(this, "editableContent"), previewDataSetModel);
		previewIframe.setOutputMarkupId(true);
		add(previewIframe);

		// build the markers list
		CompilerMarkerListPanel compilerMarkersListPanel = new CompilerMarkerListPanel("compilerMarkers", new PropertyModel<List<CompilerMarker>>(this, "compilerMarkers"));
		add(compilerMarkersListPanel);
		autocompileBehavior.setResultConsumer(new CompilerResultConsumer(previewIframe, compilerMarkersListPanel));

	}
	
	/**
	 * Getter method for the previewDataSet.
	 * @return the previewDataSet
	 */
	public TemplatePreviewDataSet getPreviewDataSet() {
		return previewDataSet;
	}

	/**
	 * Called when the user submits the edited content.
	 */
	protected abstract void onSubmit();

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
		 * the previewIframe
		 */
		private final PreviewTemplateIframe previewIframe;

		/**
		 * the compilerMarkersContainer
		 */
		private final WebMarkupContainer compilerMarkersContainer;

		/**
		 * Constructor.
		 * @param previewIframe the iframe used for preview
		 * @param compilerMarkersContainer the container for the markers list
		 */
		public CompilerResultConsumer(final PreviewTemplateIframe previewIframe, final WebMarkupContainer compilerMarkersContainer) {
			this.previewIframe = previewIframe;
			this.compilerMarkersContainer = compilerMarkersContainer;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.common.terms.IConsumer#consume(java.lang.Object)
		 */
		@Override
		public void consume(final CompilerResult compilerResult) {

			// update marker list
			setCompilerMarkersFromResult(compilerResult);
			AjaxRequestUtil.markForRender(compilerMarkersContainer);

			// update preview
			editableContent = compilerResult.getDocument();
			previewIframe.renderReloadScript();
		}

	}

}
