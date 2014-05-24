/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Custom log layout that extends {@link PatternLayout}. Special handling:
 * 
 * - replaces space characters by hyphens in the thread name to support log file
 *   analysis tools
 *   
 */
public class MyLogLayout extends PatternLayout {

	/**
	 * Constructor.
	 */
	public MyLogLayout() {
		super();
	}

	/**
	 * Constructor.
	 * @param pattern the pattern
	 */
	public MyLogLayout(String pattern) {
		super(pattern);
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.PatternLayout#createPatternParser(java.lang.String)
	 */
	@Override
	protected PatternParser createPatternParser(String pattern) {
		return new PatternParser(pattern) {
			/* (non-Javadoc)
			 * @see org.apache.log4j.helpers.PatternParser#finalizeConverter(char)
			 */
			@Override
			protected void finalizeConverter(char c) {
				if (c == 't') {
					PatternConverter converter = new PatternConverter(formattingInfo) {
						@Override
						protected String convert(LoggingEvent event) {
							return event.getThreadName().replace(' ', '-');
						}
					};
					currentLiteral.setLength(0);
					addConverter(converter);
				} else {
					super.finalizeConverter(c);
				}
			}
		};
	}
	
}
