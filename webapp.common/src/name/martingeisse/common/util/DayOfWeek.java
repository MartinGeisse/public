/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This enum represents the days of a week.
 */
public enum DayOfWeek {

	/**
	 * the MONDAY
	 */
	MONDAY,
	
	/**
	 * the TUESDAY
	 */
	TUESDAY,
	
	/**
	 * the WEDNESDAY
	 */
	WEDNESDAY,
	
	/**
	 * the THURSDAY
	 */
	THURSDAY,
	
	/**
	 * the FRIDAY
	 */
	FRIDAY,
	
	/**
	 * the SATURDAY
	 */
	SATURDAY,
	
	/**
	 * the SUNDAY
	 */
	SUNDAY;
	
	/**
	 * The mapping table used in fromGregorianCalendarDayOfWeek().
	 */
	private static DayOfWeek[] fromGregorianCalendarDayOfWeekTable;
	
	/**
	 * The long day-of-week names, in the same order as the enum constants.
	 */
	private static String[] longNames = {
		"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"
	};
	
	/**
	 * The short day-of-week names, in the same order as the enum constants.
	 */
	private static String[] shortNames = {
		"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"
	};
	
	/**
	 * Static initializer.
	 */
	static {
		ensureGregorianCalendarClassIsInitialized();
		initializeStaticFields();
	}
	
	/**
	 * This function is used to make sure the fields from {@link GregorianCalendar} and {@link Calendar}
	 * have their correct values before referring to them in our static initializer.
	 */
	private static void ensureGregorianCalendarClassIsInitialized() {
		new GregorianCalendar();
	}
	
	/**
	 * 
	 */
	private static void initializeStaticFields() {
		fromGregorianCalendarDayOfWeekTable = new DayOfWeek[8];
		fromGregorianCalendarDayOfWeekTable[Calendar.MONDAY] = MONDAY;
		fromGregorianCalendarDayOfWeekTable[Calendar.TUESDAY] = TUESDAY;
		fromGregorianCalendarDayOfWeekTable[Calendar.WEDNESDAY] = WEDNESDAY;
		fromGregorianCalendarDayOfWeekTable[Calendar.THURSDAY] = THURSDAY;
		fromGregorianCalendarDayOfWeekTable[Calendar.FRIDAY] = FRIDAY;
		fromGregorianCalendarDayOfWeekTable[Calendar.SATURDAY] = SATURDAY;
		fromGregorianCalendarDayOfWeekTable[Calendar.SUNDAY] = SUNDAY;
	}
	
	/**
	 * Constructor.
	 */
	private DayOfWeek() {
	}
	
	/**
	 * Returns the value of this day-of-week when encoded in a flagset value.
	 * @return the flag value
	 */
	public int getFlagValue() {
		return 1 << ordinal();
	}
	
	/**
	 * Checks whether this day-of-week is contained in the specified day-of-week flagset value.
	 * @param flagSetValue the flagset value
	 * @return true if contained, false if not.
	 */
	public boolean isContainedInFlagSetValue(int flagSetValue) {
		return ((flagSetValue & getFlagValue()) != 0);
	}
	
	/**
	 * Returns the matching {@link DayOfWeek} instance from the day-of-week stored in the specified {@link GregorianCalendar}.
	 * @param gregorianCalendar the {@link GregorianCalendar} that provides the day of week.
	 * @return the matching {@link DayOfWeek} enum constant.
	 */
	public static DayOfWeek from(GregorianCalendar gregorianCalendar) {
		return fromGregorianCalendarDayOfWeek(gregorianCalendar.get(Calendar.DAY_OF_WEEK));
	}
	
	/**
	 * Returns the matching {@link DayOfWeek} instance from the specified day-of-week, represented as one of the
	 * day-of-week constants in class {@link GregorianCalendar}.
	 * @param dayOfWeek the day-of-week constant from {@link GregorianCalendar} that provides the day of week.
	 * @return the matching {@link DayOfWeek} enum constant.
	 */
	public static DayOfWeek fromGregorianCalendarDayOfWeek(int dayOfWeek) {
		if (dayOfWeek < 1 || dayOfWeek > 7) {
			throw new IllegalArgumentException("invalid day-of-week, should be a constant from GregorianCalendar: " + dayOfWeek);
		}
		return fromGregorianCalendarDayOfWeekTable[dayOfWeek];
	}
	
	/**
	 * Returns the long name of the specified day-of-week, given its ordinal number.
	 * @param ordinal the ordinal number of the day-of-week
	 * @return the long name
	 */
	public static String getLongName(int ordinal) {
		if (ordinal < 0 || ordinal > 6) {
			throw new IllegalArgumentException("invalid day-of-week ordinal number: " + ordinal);
		}
		return longNames[ordinal];
	}
	
	/**
	 * Returns the long name of this day-of-week.
	 * @return the long name
	 */
	public String getLongName() {
		return longNames[ordinal()];
	}
	
	/**
	 * Returns the short name of the specified day-of-week, given its ordinal number.
	 * @param ordinal the ordinal number of the day-of-week
	 * @return the short name
	 */
	public static String getShortName(int ordinal) {
		if (ordinal < 0 || ordinal > 6) {
			throw new IllegalArgumentException("invalid day-of-week ordinal number: " + ordinal);
		}
		return shortNames[ordinal];
	}
	
	/**
	 * Returns the short name of this day-of-week.
	 * @return the short name
	 */
	public String getShortName() {
		return shortNames[ordinal()];
	}
	
	/**
	 * Returns the day-of-week with the specified long name, or null if none matches.
	 * @param longName the long name to look for
	 * @return the day-of-week
	 */
	public static DayOfWeek fromLongName(String longName) {
		for (DayOfWeek d : values()) {
			if (d.getLongName().equals(longName)) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * Returns the day-of-week with the specified long name, or null if none matches.
	 * This method performs a case-insensitive match test.
	 * @param longName the long name to look for
	 * @return the day-of-week
	 */
	public static DayOfWeek fromLongNameIgnoreCase(String longName) {
		for (DayOfWeek d : values()) {
			if (d.getLongName().equalsIgnoreCase(longName)) {
				return d;
			}
		}
		return null;
	}

	/**
	 * Returns the day-of-week with the specified short name, or null if none matches.
	 * @param shortName the short name to look for
	 * @return the day-of-week
	 */
	public static DayOfWeek fromShortName(String shortName) {
		for (DayOfWeek d : values()) {
			if (d.getShortName().equals(shortName)) {
				return d;
			}
		}
		return null;
	}

	/**
	 * Returns the day-of-week with the specified short name, or null if none matches.
	 * This method performs a case-insensitive match test.
	 * @param shortName the short name to look for
	 * @return the day-of-week
	 */
	public static DayOfWeek fromShortNameIgnoreCase(String shortName) {
		for (DayOfWeek d : values()) {
			if (d.getShortName().equalsIgnoreCase(shortName)) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * Returns a bit mask for at most seven consecutive days. The range may wrap
	 * around the week boundaries.
	 * 
	 * @param start the first day in the range
	 * @param end the last day in the range
	 * @return the bit mask for the range
	 */
	public static int getWraparoundMask(DayOfWeek start, DayOfWeek end) {
		int s = start.ordinal();
		int e = end.ordinal();
		int base = (s > e ? 127 : 0);
		return base ^ ((1 << s) - 1) ^ ((1 << (e + 1)) - 1);
	}
	
	/**
	 * Returns a bit mask for all days of week covered by the specified date range.
	 * The bit mask is empty (zero) if the start date is after the end date.
	 * 
	 * @param start the first date of the range
	 * @param end the last date of the range
	 * @return the bit mask
	 */
	public static int getMask(GregorianCalendar start, GregorianCalendar end) {

		// copy the date from the input values, discarding time-of-day
		start = DateTimeUtil.copyDate(start);
		end = DateTimeUtil.copyDate(end);
		
		// check for empty result
		if (start.compareTo(end) > 0) {
			return 0;
		}

		// check for "all days-of-week"
		GregorianCalendar startPlusSevenDays = DateTimeUtil.getWithDaysAdded(start, 7);
		if (startPlusSevenDays.compareTo(end) <= 0) {
			return 127;
		}
		
		// the range wraps around once
		return getWraparoundMask(from(start), from(end));

	}
	
}
