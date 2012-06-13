/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * A simple {@link Panel} that wraps a {@link TextField}.
 */
public class TextFieldWithSuggestionsPanel extends Panel implements IHeaderContributor {

	/**
	 * the CUSTOM_TEXT
	 */
	private static final String CUSTOM_TEXT = "(sonstige)";

	/**
	 * the textField
	 */
	private final TextField<String> textField;

	/**
	 * the suggestionsIncludingCustom
	 */
	private List<String> suggestionsIncludingCustom;

	/**
	 * the selectedSuggestion
	 */
	private String selectedSuggestion;

	/**
	 * the textFieldStyle
	 */
	private String textFieldStyle;

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public TextFieldWithSuggestionsPanel(final String id) {
		this(id, new TextField<String>(getTextFieldId()));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the text field
	 */
	public TextFieldWithSuggestionsPanel(final String id, final IModel<String> model) {
		this(id, new TextField<String>(getTextFieldId(), model));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param textField the text field to use. Must use the wicket:id returned by getTextFieldId().
	 */
	public TextFieldWithSuggestionsPanel(final String id, final TextField<String> textField) {
		super(id);
		setOutputMarkupId(true);

		this.textField = textField;
		textField.add(new AttributeAppender("style", new PropertyModel<String>(this, "textFieldStyle"), ""));
		add(textField);

		this.suggestionsIncludingCustom = null;
		final IModel<String> selectedSuggestionModel = new PropertyModel<String>(this, "selectedSuggestion");
		final IModel<List<String>> suggestionsModel = new PropertyModel<List<String>>(this, "suggestionsIncludingCustom");
		final DropDownChoice<String> suggestionsDropDownChoice = new DropDownChoice<String>("suggestions", selectedSuggestionModel, suggestionsModel);
		suggestionsDropDownChoice.setNullValid(false);
		add(suggestionsDropDownChoice);

	}

	/**
	 * @return the wicket:id that the wrapped text field must use.
	 */
	public static String getTextFieldId() {
		return "wrapped";
	}

	/**
	 * Getter method for the textField.
	 * @return the textField
	 */
	public final TextField<String> getTextField() {
		return textField;
	}

	/**
	 * Getter method for the suggestionsIncludingCustom.
	 * @return the suggestionsIncludingCustom
	 */
	public List<String> getSuggestionsIncludingCustom() {
		return suggestionsIncludingCustom;
	}

	/**
	 * @param suggestions the suggestions to use
	 */
	public void setSuggestions(final String[] suggestions) {
		suggestionsIncludingCustom = new ArrayList<String>();
		for (final String suggestion : suggestions) {
			suggestionsIncludingCustom.add(suggestion);
		}
		suggestionsIncludingCustom.add(CUSTOM_TEXT);
	}

	/**
	 * Getter method for the selectedSuggestion.
	 * @return the selectedSuggestion
	 */
	public String getSelectedSuggestion() {
		return selectedSuggestion;
	}

	/**
	 * Setter method for the selectedSuggestion.
	 * @param selectedSuggestion the selectedSuggestion to set
	 */
	public void setSelectedSuggestion(final String selectedSuggestion) {
		this.selectedSuggestion = selectedSuggestion;
	}

	/**
	 * Getter method for the textFieldStyle.
	 * @return the textFieldStyle
	 */
	public String getTextFieldStyle() {
		return textFieldStyle;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.IHeaderContributor#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	@Override
	public void renderHead(final IHeaderResponse response) {
		response.renderJavaScript(generateInitializationJavascript(), null);
	}

	/**
	 * @return the initialization Javascript fragment
	 */
	private String generateInitializationJavascript() {
		final StringBuilder builder = new StringBuilder();
		builder.append("$().ready(function() {\n");
		builder.append("	prepareTextFieldWithSuggestionsPanel('#" + getMarkupId() + "');\n");
		builder.append("});\n");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		checkIfValueMatchesSuggestion();
		super.onBeforeRender();
	}

	/**
	 * 
	 */
	private void checkIfValueMatchesSuggestion() {
		final String value = textField.getModelObject();
		if (value == null) {
			selectedSuggestion = null;
			textFieldStyle = "visibility: hidden; ";
			return;
		}

		for (final String suggestion : suggestionsIncludingCustom) {
			if (value.equals(suggestion)) {
				selectedSuggestion = value;
				textFieldStyle = "visibility: hidden; ";
				return;
			}
		}

		selectedSuggestion = CUSTOM_TEXT;
		textFieldStyle = "visibility: visible; ";
	}

}
