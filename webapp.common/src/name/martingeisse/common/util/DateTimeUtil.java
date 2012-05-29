/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utility methods to handle date, time, and datetime objects, represented
 * as {@link GregorianCalendar} instances.
 */
public final class DateTimeUtil {

	/**
	 * Prevent instantiation.
	 */
	private DateTimeUtil() {
	}

	/**
	 * @return a new {@link GregorianCalendar} representing the time this method was invoked.
	 */
	public static GregorianCalendar createNow() {
		return new GregorianCalendar();
	}
	
	/**
	 * Sets the time fields (24-hour, minute, second, millisecond) of the specified
	 * {@link GregorianCalendar} instance to 0 so it subsequently represents midnight
	 * of the same day as before.
	 * @param c the instance to modify
	 */
	public static void setTimeToMidnight(GregorianCalendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * Creates a {@link GregorianCalendar} instance with the specified values. This method
	 * handles the problem that the day-of-month and the month must be set in a specific
	 * sequence to avoid the GregorianCalendar class making "corrections" to the date based
	 * on the previous value of the instance.
	 * 
	 * This method takes the month as a {@link Month} constant to avoid uncertainty in whether
	 * the month is 1-based or one of the 0-based constants from GregorianCalendar.
	 * 
	 * @param day the day of month, 1-based
	 * @param month the month
	 * @param year the year
	 * @return a new GregorianCalendar with the specified values
	 */
	public static GregorianCalendar createDate(int day, Month month, int year) {
		GregorianCalendar result = new GregorianCalendar();
		setTimeToMidnight(result);
		result.set(Calendar.DAY_OF_MONTH, 1);
		result.set(Calendar.YEAR, year);
		result.set(Calendar.MONTH, month.getGregorianCalendarValue());
		result.set(Calendar.DAY_OF_MONTH, day);
		return result;
	}

	/**
	 * Creates a {@link GregorianCalendar} instance with the same date as the specified
	 * original instance, and the time-of-day set to midnight.
	 * 
	 * @param original the original to copy
	 * @return the copy
	 */
	public static GregorianCalendar copyDate(GregorianCalendar original) {
		GregorianCalendar result = (GregorianCalendar)original.clone();
		setTimeToMidnight(result);
		return result;
	}
	
	/**
	 * Creates a {@link GregorianCalendar} instance with the specified values, with the
	 * year implicitly being the current year. This method handles the problem that the
	 * day-of-month and the month must be set in a specific sequence to avoid the
	 * GregorianCalendar class making "corrections" to the date based on the previous
	 * value of the instance.
	 * 
	 * This method takes the month as a {@link Month} constant to avoid uncertainty in whether
	 * the month is 1-based or one of the 0-based constants from GregorianCalendar.
	 * 
	 * @param day the day of month, 1-based
	 * @param month the month
	 * @return a new GregorianCalendar with the specified values
	 */
	public static GregorianCalendar createDate(int day, Month month) {
		GregorianCalendar result = new GregorianCalendar();
		setTimeToMidnight(result);
		result.set(Calendar.DAY_OF_MONTH, 1);
		result.set(Calendar.MONTH, month.getGregorianCalendarValue());
		result.set(Calendar.DAY_OF_MONTH, day);
		return result;
	}
	
	/**
	 * Creates a {@link GregorianCalendar} from the specified {@link Date}.
	 * @param date the date
	 * @return a new GregorianCalendar with the specified value.
	 */
	public static GregorianCalendar create(Date date) {
		GregorianCalendar result = new GregorianCalendar();
		result.setTime(date);
		return result;
	}
	
	/**
	 * Compares two datetime objects as to whether their date parts are equal.
	 * @param x the first datetime object to compare
	 * @param y the second datetime object to compare
	 * @return true if the date parts are equal, false if not
	 */
	public static boolean dateEquals(GregorianCalendar x, GregorianCalendar y) {
		return (x.get(Calendar.YEAR) == y.get(Calendar.YEAR))
			&& (x.get(Calendar.MONTH) == y.get(Calendar.MONTH))
			&& (x.get(Calendar.DAY_OF_MONTH) == y.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Given the date from the specified {@link GregorianCalendar} instance, returns
	 * the date of monday in the same week. Monday is considered the first day of week
	 * by this method. Note that the returned day may be in the previous month and even
	 * year.
	 * 
	 * The time part of the returned object is unspecified.
	 * @param anchorDate the date to start from
	 * @return the date of monday in the same week
	 */
	public static GregorianCalendar getMondayFromDate(GregorianCalendar anchorDate) {
		int dayOfMonth = anchorDate.get(Calendar.DAY_OF_MONTH);
		Month month = Month.from(anchorDate);
		int year = anchorDate.get(Calendar.YEAR);
		return getMondayFromDate(year, month, dayOfMonth);
	}
	
	/**
	 * Given the specified year, month and day of month, returns the date of monday
	 * in the same week. Monday is considered the first day of week by this method.
	 * Note that the returned day may be in the previous month and even year.
	 * 
	 * The time part of the returned object is unspecified.
	 * 
	 * @param year the year to start from
	 * @param month the month to start from
	 * @param dayOfMonth the day of month to start from
	 * @return the date of monday in the same week
	 */
	public static GregorianCalendar getMondayFromDate(int year, Month month, int dayOfMonth) {
		GregorianCalendar c = new GregorianCalendar();
		setTimeToMidnight(c);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month.getGregorianCalendarValue());
		c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		DayOfWeek argumentDayOfWeek = DayOfWeek.from(c);
		c.add(Calendar.DAY_OF_MONTH, -(argumentDayOfWeek.ordinal() - DayOfWeek.MONDAY.ordinal()));
		return c;
	}
	
	/**
	 * Given the specified month and day of month as well as the current year, returns
	 * the date of monday in the same week. Monday is considered the first day of week
	 * by this method. Note that the returned day may be in the previous month and
	 * even year.
	 * 
	 * The time part of the returned object is unspecified.
	 * 
	 * @param month the month to start from
	 * @param dayOfMonth the day of month to start from
	 * @return the date of monday in the same week
	 */
	public static GregorianCalendar getMondayFromDateOfCurrentYear(Month month, int dayOfMonth) {
		GregorianCalendar c = new GregorianCalendar();
		setTimeToMidnight(c);
		c.set(Calendar.MONTH, month.getGregorianCalendarValue());
		c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		DayOfWeek argumentDayOfWeek = DayOfWeek.from(c);
		c.add(Calendar.DAY_OF_MONTH, -(argumentDayOfWeek.ordinal() - DayOfWeek.MONDAY.ordinal()));
		return c;
	}
	
	/**
	 * Returns a new {@link GregorianCalendar} instance that is the specified value plus the
	 * specified number of days added. The original date is left unchanged. The result has
	 * the same time-of-day as the original.
	 * @param originalDate the original date to start from
	 * @param days the number of days to add
	 * @return the new date
	 */
	public static GregorianCalendar getWithDaysAdded(GregorianCalendar originalDate, int days) {
		GregorianCalendar newDate = (GregorianCalendar)originalDate.clone();
		newDate.add(Calendar.DAY_OF_MONTH, days);
		return newDate;
	}

	/**
	 * Returns the smaller of the specified dates. The returned object is the same as one
	 * of the arguments, not a clone.
	 * @param x the first date
	 * @param y the second date
	 * @return the smaller one of the arguments
	 */
	public static GregorianCalendar min(GregorianCalendar x, GregorianCalendar y) {
		return (x.compareTo(y) < 0) ? x : y;
	}

	/**
	 * Returns the greater of the specified dates. The returned object is the same as one
	 * of the arguments, not a clone.
	 * @param x the first date
	 * @param y the second date
	 * @return the greater one of the arguments
	 */
	public static GregorianCalendar max(GregorianCalendar x, GregorianCalendar y) {
		return (x.compareTo(y) > 0) ? x : y;
	}
	
}
