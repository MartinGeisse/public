/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package tex;

import name.martingeisse.jtex.error.ErrorReporter;
import name.martingeisse.jtex.error.ErrorReporter.Level;

/**
 * Contains high-level error reporting logic and convenience methods.
 * All error handling in this class eventually leads to the low-level
 * reporting methods in {@link ErrorReporter}.
 */
public final class ErrorLogic {

	/**
	 * the tex
	 */
	private Tex tex;
	
	/**
	 * Constructor.
	 * @param tex the tex engine
	 */
	ErrorLogic(Tex tex) {
		this.tex = tex;
	}
	
	/**
	 * TODO documentation
	 */
	public void alignError() {
		if (Math.abs(tex.alignstate) > 2) {
			tex.printnl("!Misplaced ");
			tex.printcmdchr(tex.curcmd, tex.curchr);
			if (tex.curtok == 1062) {
				throw new RuntimeException(
					"I can't figure out why you would want to use a tab mark" +
					"here. If you just want an ampersand, the remedy is" +
					"simple: Just type `I\\&' now. But if some right brace" +
					"up above has ended a previous alignment prematurely," +
					"you're probably due for more error messages, and you" +
					"might try typing `S' now just to see what is salvageable."
				);
			} else {
				throw new RuntimeException(
					"I can't figure out why you would want to use a tab mark" +
					"or \\cr or \\span just now. If something like a right brace" +
					"up above has ended a previous alignment prematurely," +
					"you're probably due for more error messages, and you" +
					"might try typing `S' now just to see what is salvageable."
				);
			}
		} else {
			tex.unreadToken();
			if (tex.alignstate < 0) {
				throw new RuntimeException("missing left brace");
			} else {
				throw new RuntimeException("missing right brace");
			}
		}
	}


	/**
	 * TODO documentation
	 */
	void youCantUse(String... helpLines) {
		tex.printnl("!You can't use `");
		tex.printcmdchr(tex.curcmd, tex.curchr);
		tex.print("' in ");
		tex.printmode(tex.curlist.modefield);
		error("", helpLines);
	}

	/**
	 * TODO documentation
	 */
	void reportillegalcase() {
		youCantUse("Sorry, but I'm not programmed to handle this case");
	}

	/**
	 * TODO documentation
	 */
	void interror(final int n) {
		tex.print(286);
		tex.printInt(n);
		tex.printchar(41);
		error();
	}

	/**
	 * TODO documentation
	 */
	void error(String errorMessage, String... helpLines) {
		tex.printnl(errorMessage);
		errorWithStringHelpLines();
	}

	/**
	 * TODO documentation
	 */
	void errorWithStringHelpLines(String... helpLines) {
		tex.printchar('.');
		for (String helpLine : helpLines) {
			tex.printnl(helpLine);
		}
		tex.helpptr = 0;
		tex.errorReporter.error("ERROR"); // TODO dirty: sets the "worst error level so far" and stops on 100+ errors
	}

	/**
	 * TODO documentation
	 */
	void error() {
		tex.printchar('.');
		while (tex.helpptr > 0) {
			tex.helpptr = tex.helpptr - 1;
			tex.printnl(tex.helpline[tex.helpptr]);
		}
		tex.errorReporter.error("ERROR"); // TODO dirty: sets the "worst error level so far" and stops on 100+ errors
	}
	
	/**
	 * Emits a warning about a "lost" character, i.e. a character for which there is no glyph
	 * in the font, *if* such warnings are currently enabled as specified by \tracinglostchars.
	 * 
	 * @param f the font
	 * @param c the lost character
	 */
	void charwarning(final int f, final int c) {
		if (tex.eqtb[9598].getInt() > 0) { // (tracinglostchars) --> show characters that aren't in the font
			throw new RuntimeException("missing glyph in font for character " + (char)c + " (" + c + ")");
		}
	}

}
