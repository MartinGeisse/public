/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package tex;

import java.io.PrintWriter;


/**
 * This class is used by the TeX engine to report errors to its caller.
 */
public final class ErrorReporter {

	/**
	 * the logfile
	 */
	private PrintWriter logfile;
	
	/**
	 * the worstLevelSoFar
	 */
	private Level worstLevelSoFar;
	
	/**
	 * the errorCount
	 */
	private int errorCount;
	
	/**
	 * Constructor.
	 * @param logfile the logfile to write to
	 */
	public ErrorReporter(PrintWriter logfile) {
		this.logfile = logfile;
		this.worstLevelSoFar = Level.OK;
		this.errorCount = 0;
	}
	
	/**
	 * Getter method for the worstLevelSoFar.
	 * @return the worstLevelSoFar
	 */
	public Level getWorstLevelSoFar() {
		return worstLevelSoFar;
	}
	
	/**
	 * Getter method for the errorCount.
	 * @return the errorCount
	 */
	public int getErrorCount() {
		return errorCount;
	}

	/**
	 * Reports an info-level message.
	 * @param level the level to report on
	 * @param message the message
	 */
	public void report(Level level, String message) {
		if (worstLevelSoFar.ordinal() < level.ordinal()) {
			worstLevelSoFar = level;
		}
		System.err.println(level.name() + ": " + message);
		logfile.println(level.name() + ": " + message);
		if (level.ordinal() >= Level.ERROR.ordinal()) {
			errorCount++;
		}
		if (level == Level.FATAL) {
			throw new TexFatalErrorException(message);
		}
		if (errorCount >= 100) {
			throw new TexFatalErrorException("too many errors");
		}
	}

	/**
	 * Reports an info-level message.
	 * @param message the message
	 */
	public void info(String message) {
		report(Level.OK, message);
	}
	
	/**
	 * Reports an warning-level message.
	 * @param message the message
	 */
	public void warning(String message) {
		report(Level.WARNING, message);
	}
	
	/**
	 * Reports an error-level message.
	 * @param message the message
	 */
	public void error(String message) {
		report(Level.ERROR, message);
	}
	
	/**
	 * Reports an fatal-level message.
	 * @param message the message
	 */
	public void fatal(String message) {
		report(Level.FATAL, message);
	}
	

	/**
	 * Reportable error levels.
	 */
	public enum Level {

		/**
		 * No error occurred yet.
		 */
		OK,
		
		/**
		 * Only warnings occurred yet.
		 */
		WARNING,
		
		/**
		 * At least one error has occurred.
		 */
		ERROR,
		
		/**
		 * A fatal error has occurred and the TeX engine has stopped
		 * because of it.
		 */
		FATAL;
		
	}
	
}
