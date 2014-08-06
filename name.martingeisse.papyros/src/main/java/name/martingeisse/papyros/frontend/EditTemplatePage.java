/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.papyros.frontend;

import name.martingeisse.papyros.entity.QTemplate;
import name.martingeisse.papyros.entity.Template;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.wicket.component.codemirror.CodeMirrorBehavior;
import name.martingeisse.wicket.component.codemirror.modes.StandardCodeMirrorModes;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * TODO: document me
 */
public class EditTemplatePage extends AbstractFrontendPage {

	/**
	 * the content
	 */
	private String content;
	
	/**
	 * Constructor.
	 * @param pageParameters the page parameters
	 */
	public EditTemplatePage(PageParameters pageParameters) {
		super(pageParameters);
		final Template template = FrontendDataUtil.loadTemplate(pageParameters);
		this.content = template.getContent();
		
		Form<Void> form = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				final QTemplate q = QTemplate.template;
				SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(q);
				update.set(q.content, content).where(q.id.eq(template.getId()));
				update.execute();
			}
		};
		form.add(new TextArea<>("textarea", new PropertyModel<>(this, "content")).add(new CodeMirrorBehavior(StandardCodeMirrorModes.JAVASCRIPT)));
		add(form);
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
	public void setContent(String content) {
		this.content = content;
	}
	
}
