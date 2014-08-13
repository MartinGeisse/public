/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import name.martingeisse.common.terms.IConsumer;
import name.martingeisse.papyros.backend.PapyrosDataUtil;
import name.martingeisse.papyros.backend.RenderTemplateAction;
import name.martingeisse.papyros.entity.PreviewDataSet;
import name.martingeisse.papyros.entity.QTemplate;
import name.martingeisse.papyros.entity.Template;
import name.martingeisse.papyros.entity.TemplateFamily;
import name.martingeisse.papyros.frontend.AbstractFrontendPage;
import name.martingeisse.papyros.frontend.components.Iframe;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.wicket.component.codemirror.CodeMirrorBehavior;
import name.martingeisse.wicket.component.codemirror.compile.CodeMirrorAutocompileBehavior;
import name.martingeisse.wicket.component.codemirror.compile.CompilerMarker;
import name.martingeisse.wicket.component.codemirror.compile.CompilerResult;
import name.martingeisse.wicket.component.codemirror.modes.StandardCodeMirrorModes;
import name.martingeisse.wicket.component.misc.GlyphiconComponent;
import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.json.simple.JSONValue;

import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * TODO: document me
 */
public class EditTemplatePage extends AbstractFrontendPage implements IConsumer<CompilerResult> {

	/**
	 * the content
	 */
	private String content;

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
	 * the preview
	 */
	private String preview;

	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public EditTemplatePage(final PageParameters pageParameters) {
		super(pageParameters);
		final Pair<Template, TemplateFamily> pair = PapyrosDataUtil.loadTemplateAndTemplateFamily(pageParameters);
		final Template template = pair.getLeft();
		final TemplateFamily family = pair.getRight();
		add(new BookmarkablePageLink<>("templateLink", TemplatePage.class, new PageParameters().add("key", family.getKey()).add("language", template.getLanguageKey())));
		
		this.content = template.getContent();
		this.previewDataSet = PapyrosDataUtil.loadPreviewDataSet(template.getTemplateFamilyId(), 0);
		this.parsedPreviewData = (previewDataSet == null ? null : JSONValue.parse(previewDataSet.getData()));

		final Form<Void> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				final QTemplate q = QTemplate.template;
				final SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(q);
				update.set(q.content, content).where(q.id.eq(template.getId()));
				update.execute();
			}
		};
		final CodeMirrorBehavior codeMirrorBehavior = new CodeMirrorBehavior(StandardCodeMirrorModes.JAVASCRIPT);
		final CodeMirrorAutocompileBehavior autocompileBehavior = new CodeMirrorAutocompileBehavior(new TemplateAutocompiler());
		autocompileBehavior.setResultConsumer(this);
		form.add(new TextArea<>("textarea", new PropertyModel<>(this, "content")).add(codeMirrorBehavior).add(autocompileBehavior));
		add(form);

		WebMarkupContainer compilerMarkersContainer = new WebMarkupContainer("compilerMarkersContainer");
		compilerMarkersContainer.setOutputMarkupId(true);
		add(compilerMarkersContainer);
		compilerMarkersContainer.add(new ListView<CompilerMarker>("compilerMarkers", new PropertyModel<List<CompilerMarker>>(this, "compilerMarkers")) {
			@Override
			protected void populateItem(final ListItem<CompilerMarker> item) {

				// add color class
				final CompilerMarker marker = item.getModelObject();
				final String cssClassName = "autocompile-" + marker.getErrorLevel().name().toLowerCase() + "-color";
				item.add(new AttributeAppender("class", Model.of(cssClassName), " "));

				// add icon
				item.add(new GlyphiconComponent("icon") {
					@Override
					protected String getGlyphiconIdentifier() {
						return marker.getErrorLevel().getGlyphicon();
					}
				});

				// add message
				final StringBuilder builder = new StringBuilder();
				builder.append(marker.getStartLine() + 1);
				builder.append(", ");
				builder.append(marker.getStartColumn() + 1);
				builder.append(": ");
				builder.append(marker.getMessage());
				item.add(new Label("message", builder));

			}
		});
		
		add(new Iframe("testRenderIframe", new PropertyModel<>(this, "preview")).setOutputMarkupId(true));

		renderPreview(content);
	}

	/**
	 * Getter method for the content.
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Setter method for the content.
	 * @param content the content to set
	 */
	public void setContent(final String content) {
		this.content = content;
	}

	/**
	 * Getter method for the compilerMarkers.
	 * @return the compilerMarkers
	 */
	public List<CompilerMarker> getCompilerMarkers() {
		return compilerMarkers;
	}

	/**
	 * Setter method for the compilerMarkers.
	 * @param compilerMarkers the compilerMarkers to set
	 */
	public void setCompilerMarkers(final List<CompilerMarker> compilerMarkers) {
		this.compilerMarkers = compilerMarkers;
	}
	
	/**
	 * Getter method for the preview.
	 * @return the preview
	 */
	public String getPreview() {
		return preview;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.terms.IConsumer#consume(java.lang.Object)
	 */
	@Override
	public void consume(final CompilerResult compilerResult) {
		
		// render markers
		this.compilerMarkers = new ArrayList<>(compilerResult.getMarkers());
		Collections.sort(this.compilerMarkers, new Comparator<CompilerMarker>() {
			@Override
			public int compare(CompilerMarker o1, CompilerMarker o2) {
				return o1.getErrorLevel().ordinal() - o2.getErrorLevel().ordinal();
			}
		});
		AjaxRequestUtil.markForRender(get("compilerMarkersContainer"));
		
		// render the template for preview
		renderPreview(compilerResult.getDocument());
		((Iframe)get("testRenderIframe")).renderReloadScript();
		
	}
	
	/**
	 * 
	 */
	private void renderPreview(String document) {
		final RenderTemplateAction renderTemplateAction = new RenderTemplateAction();
		renderTemplateAction.setTemplate(document);
		renderTemplateAction.setData(parsedPreviewData);
		this.preview = renderTemplateAction.render();
	}

}
