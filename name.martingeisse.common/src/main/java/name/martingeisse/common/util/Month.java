/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import name.martingeisse.common.terms.AmbiguousFuzzyMatchException;
import org.apache.commons.lang3.StringUtils;

/**
 * This enum represents the months of a year.
 */
public enum Month {
	
	/**
	 * the JANUARY
	 */
	JANUARY,
	
	/**
	 * the FEBRUARY
	 */
	FEBRUARY,
	
	/**
	 * the MARCH
	 */
	MARCH,
	
	/**
	 * the APRIL
	 */
	APRIL,
	
	/**
	 * the MAY
	 */
	MAY,
	
	/**
	 * the JUNE
	 */
	JUNE,
	
	/**
	 * the JULY
	 */
	JULY,
	
	/**
	 * the AUGUST
	 */
	AUGUST,
	
	/**
	 * the SEPTEMBER
	 */
	SEPTEMBER,
	
	/**
	 * the OCTOBER
	 */
	OCTOBER,
	
	/**
	 * the NOVEMBER
	 */
	NOVEMBER,
	
	/**
	 * the DECEMBER
	 */
	DECEMBER;

	/**
	 * the longNames
	 */
	private static final String[] longNames = {
		"Januar",
		"Februar",
		"März",
		"April",
		"Mai",
		"Juni",
		"Juli",
		"August",
		"September",
		"Oktober",
		"November",
		"Dezember"
	};

	/**
	 * the shortNames
	 */
	private static final String[] shortNames = {
		"Jan",
		"Feb",
		"Mär",
		"Apr",
		"Mai",
		"Jun",
		"Jul",
		"Aug",
		"Sep",
		"Okt",
		"Nov",
		"Dez"
	};
	
	/**
	 * @return the constant used in {@link GregorianCalendar} for this month.
	 */
	public int getGregorianCalendarValue() {
		return ordinal() + Calendar.JANUARY;
	}

	/**
	 * @param c a {@link GregorianCalendar} that contains the month to return
	 * @return the corresponding month
	 */
	public static Month from(GregorianCalendar c) {
		return fromGregorianCalendarValue(c.get(Calendar.MONTH));
	}
	
	/**
	 * @param value the constant used in {@link GregorianCalendar}
	 * @return the corresponding month
	 */
	public static Month fromGregorianCalendarValue(int value) {
		if (value < Calendar.JANUARY || value > Calendar.DECEMBER) {
			throw new IllegalArgumentException("invalid argument value: " + value);
		}
		return values()[value - Calendar.JANUARY];
	}
	
	/**
	 * @return the numeric value that shall be displayed to the user for this month
	 * This method returns 1 for january.
	 */
	public int getNumericDisplayValue() {
		return ordinal() + 1;
	}

	/**
	 * @param value the displayed numeric value
	 * @return the corresponding month
	 */
	public static Month fromNumericDisplayValue(int value) {
		if (value < 1 || value > 12) {
			throw new IllegalArgumentException("invalid argument value: " + value);
		}
		return values()[value - 1];
	}
	
	/**
	 * @return the short name of this month
	 */
	public String getShortName() {
		return shortNames[ordinal()];
	}
	
	/**
	 * @return the long name of this month
	 */
	public String getLongName() {
		return longNames[ordinal()];
	}
	
	/**
	 * Returns the number of days this month has in the specified year
	 * @param year the year
	 * @return the number of days
	 */
	public int getNumberOfDays(int year) {
		return GregorianCalendarUtil.createDate(1, this, year).getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Return the previous month to this month.
	 * @return the previous month
	 */
	public Month getPrevious() {
		return values()[(ordinal() + 11) % 12];
	}
	
	/**
	 * Return the next month to this month.
	 * @return the next month
	 */
	public Month getNext() {
		return values()[(ordinal() + 1) % 12];
	}
	
	/**
	 * Returns the day of week (as a {@link DayOfWeek} value) for the specified day of
	 * this month. The days of month are counted the same way as they are displayed, and
	 * also as counted in {@link GregorianCalendar} (i.e. 1-based). 
	 * @param year the year
	 * @param dayOfMonth the 1-based day of month
	 * @return the day of week for that day
	 */
	public DayOfWeek getDayOfWeekFromDayOfMonth(int year, int dayOfMonth) {
		return DayOfWeek.from(GregorianCalendarUtil.createDate(dayOfMonth, this, year));
	}

	/**
	 * Finds a month by its long name. This method requires an exact match.
	 * @param name the month name to recognize
	 * @return the month, or null if no month name matches
	 */
	public static Month findByLongName(String name) {
		for (Month month : values()) {
			if (month.getLongName().equals(name)) {
				return month;
			}
		}
		return null;
	}

	/**
	 * Finds a month by its long name. This method requires a
	 * case-insensitive but otherwise exact match.
	 * @param name the month name to recognize
	 * @return the month, or null if no month name matches
	 */
	public static Month findByLongNameIgnoreCase(String name) {
		for (Month month : values()) {
			if (month.getLongName().equalsIgnoreCase(name)) {
				return month;
			}
		}
		return null;
	}
	
	/**
	 * Finds a month by its name, using fuzzy matching (Levenshtein distance).
	 * 
	 * This method determines the Levelshtein distance to all long month names. If all
	 * distances are greater than the specified maximum distance, null is returned.
	 * Otherwise, if a single month has smallest distance to the argument name, that
	 * month is returned. Otherwise, the minimum distance is shared by multiple
	 * months, i.e. the name is ambiguous, and an {@link AmbiguousFuzzyMatchException}
	 * is thrown.
	 * 
	 * @param name the name to find. Must not be null.
	 * @param maxDistance the maximum distance to accept a match.
	 * @return returns the matching month with shortest distance, or null if none matches
	 * @throws AmbiguousFuzzyMatchException if the shortest distance is shared by more than one month
	 */
	public static Month fuzzyFind(String name, int maxDistance) {
		if (name == null) {
			throw new IllegalArgumentException("name argument is null");
		}
		
		Month shortestMatchMonth = null;
		int shortestMatchDistance = Integer.MAX_VALUE;
		boolean ambiguous = false;
		for (Month month : values()) {
			int distance = StringUtils.getLevenshteinDistance(month.getLongName(), name);
			if (distance < shortestMatchDistance) {
				shortestMatchDistance = distance;
				ambiguous = false;
				shortestMatchMonth = month;
			} else if (distance == shortestMatchDistance) {
				ambiguous = true;
			}
		}
		
		if (shortestMatchDistance > maxDistance) {
			return null;
		} else if (ambiguous) {
			throw new AmbiguousFuzzyMatchException();
		} else {
			return shortestMatchMonth;
		}
	}
}
