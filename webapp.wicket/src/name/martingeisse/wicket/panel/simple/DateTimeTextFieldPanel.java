/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import name.martingeisse.wicket.application.MyWicketSession;
import name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

/**
 * A {@link Panel} that wraps two {@link TextField}s,
 * one for the date and one for the time-of-day.
 * @param <T> the model type
 */
public class DateTimeTextFieldPanel<T> extends FormComponentPanel<T> implements IFormComponentPanel<T> {

	/**
	 * the originalChronology
	 */
	private Chronology originalChronology;

	/**
	 * the localizedChronology
	 */
	private Chronology localizedChronology;

	/**
	 * the date
	 */
	private LocalDate date;

	/**
	 * the time
	 */
	private LocalTime time;

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the text field
	 */
	public DateTimeTextFieldPanel(final String id, final IModel<T> model) {
		super(id, model);
		add(new TextField<LocalDate>("date", new PropertyModel<LocalDate>(this, "date")).setOutputMarkupId(true));
		add(new TextField<LocalTime>("time", new PropertyModel<LocalTime>(this, "time")));
	}

	/**
	 * Getter method for the originalChronology.
	 * @return the originalChronology
	 */
	public Chronology getOriginalChronology() {
		return originalChronology;
	}

	/**
	 * Setter method for the originalChronology.
	 * @param originalChronology the originalChronology to set
	 */
	public void setOriginalChronology(final Chronology originalChronology) {
		this.originalChronology = originalChronology;
	}

	/**
	 * Getter method for the localizedChronology.
	 * @return the localizedChronology
	 */
	public Chronology getLocalizedChronology() {
		return localizedChronology;
	}

	/**
	 * Setter method for the localizedChronology.
	 * @param localizedChronology the localizedChronology to set
	 */
	public void setLocalizedChronology(final Chronology localizedChronology) {
		this.localizedChronology = localizedChronology;
	}

	/**
	 * Getter method for the date.
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Setter method for the date.
	 * @param date the date to set
	 */
	public void setDate(final LocalDate date) {
		this.date = date;
	}

	/**
	 * Getter method for the time.
	 * @return the time
	 */
	public LocalTime getTime() {
		return time;
	}

	/**
	 * Setter method for the time.
	 * @param time the time to set
	 */
	public void setTime(final LocalTime time) {
		this.time = time;
	}

	/**
	 * Getter method for the dateTextField.
	 * @return the dateTextField
	 */
	@SuppressWarnings("unchecked")
	public TextField<LocalDate> getDateTextField() {
		return (TextField<LocalDate>)get("date");
	}

	/**
	 * Getter method for the timeTextField.
	 * @return the timeTextField
	 */
	@SuppressWarnings("unchecked")
	public TextField<LocalTime> getTimeTextField() {
		return (TextField<LocalTime>)get("time");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		final Class<?> modelType = getType();
		final Object modelObject = getDefaultModelObject();
		if (modelType == DateTime.class) {
			final DateTime modelDateTime = (DateTime)modelObject;
			originalChronology = modelDateTime.getChronology();
			localizedChronology = originalChronology.withZone(MyWicketSession.get().getTimeZone());
			final DateTime localizedModelDateTime = modelDateTime.withChronology(localizedChronology);
			date = localizedModelDateTime.toLocalDate();
			time = localizedModelDateTime.toLocalTime();
		} else if (modelType == LocalDateTime.class) {
			final LocalDateTime modelDateTime = (LocalDateTime)modelObject;
			originalChronology = modelDateTime.getChronology();
			localizedChronology = originalChronology;
			date = modelDateTime.toLocalDate();
			time = modelDateTime.toLocalTime();
		} else {
			throw new IllegalStateException("unsupported model type for DateTimeTextFieldPanel: " + modelType);
		}
		super.onBeforeRender();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.FormComponent#convertInput()
	 */
	@Override
	protected void convertInput() {

		// get all required objects. Note: This method is invoked *before* the sub-text-fields
		// have stored their model values in this object, so we must get them manually.
		final Class<?> modelType = getType();
		final LocalDate date = getDateTextField().getConvertedInput();
		final LocalTime time = getTimeTextField().getConvertedInput();

		// convert the input
		if (modelType == DateTime.class) {
			final DateTime localizedDateTime = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(),
				time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(), time.getMillisOfSecond(), localizedChronology);
			setConvertedInputUnsafe(localizedDateTime.withChronology(originalChronology));
		} else if (modelType == LocalDateTime.class) {
			setConvertedInputUnsafe(new LocalDateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), time.getHourOfDay(),
				time.getMinuteOfHour(), time.getSecondOfMinute(), time.getMillisOfSecond(), localizedChronology));
		} else {
			throw new IllegalStateException("unsupported model type for DateTimeTextFieldPanel: " + modelType);
		}

	}

	/**
	 * 
	 */
	@SuppressWarnings({
		"unused", "unchecked"
	})
	private void setConvertedInputUnsafe(final Object convertedInput) {
		super.setConvertedInput((T)convertedInput);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#getRootComponent()
	 */
	@Override
	public Component getRootComponent() {
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#getFormComponent()
	 */
	@Override
	public FormComponent<T> getFormComponent() {
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#connectValidationErrorAcceptor(name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor)
	 */
	@Override
	public void connectValidationErrorAcceptor(final IValidationErrorAcceptor validationErrorAcceptor) {
		validationErrorAcceptor.acceptValidationErrorsFromMultiple(this, getDateTextField(), getTimeTextField());
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.IHeaderContributor#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		String markupId = getDateTextField().getMarkupId();
		response.renderOnDomReadyJavaScript("$('#" + markupId + "').datepicker({ dateFormat: 'd.m.yy' });");
	}
	
}
