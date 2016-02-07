/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package tex;

import tex.ErrorReporter.Level;

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
				error("",
					"I can't figure out why you would want to use a tab mark",
					"here. If you just want an ampersand, the remedy is",
					"simple: Just type `I\\&' now. But if some right brace",
					"up above has ended a previous alignment prematurely,",
					"you're probably due for more error messages, and you",
					"might try typing `S' now just to see what is salvageable."
				);
			} else {
				error("",
					"I can't figure out why you would want to use a tab mark",
					"or \\cr or \\span just now. If something like a right brace",
					"up above has ended a previous alignment prematurely,",
					"you're probably due for more error messages, and you",
					"might try typing `S' now just to see what is salvageable."
				);
			}
		} else {
			tex.unreadToken();
			if (tex.alignstate < 0) {
				tex.printnl(262);
				tex.print(657);
				tex.alignstate = tex.alignstate + 1;
				tex.insertToken(379);
			} else {
				tex.printnl(262);
				tex.print(1110);
				tex.alignstate = tex.alignstate - 1;
				tex.insertToken(637);
			}
			tex.helpptr = 3;
			tex.helpline[2] = 1111;
			tex.helpline[1] = 1112;
			tex.helpline[0] = 1113;
			error();
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
		youCantUse(
			"Sorry, but I'm not programmed to handle this case;",
			"I'll just pretend that you didn't ask for it.",
			"If you're in the wrong mode, you might be able to",
			"return to the right one by typing `I}' or `I$' or `I\\par'."
		);
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
	void muerror() {
		tex.printnl(262);
		tex.print(662);
		tex.helpptr = 1;
		tex.helpline[0] = 663;
		error();
	}

	/**
	 * TODO documentation
	 */
	void backerror() {
		tex.unreadToken();
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
		tex.showcontext();
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
		tex.showcontext();
		while (tex.helpptr > 0) {
			tex.helpptr = tex.helpptr - 1;
			tex.printnl(tex.helpline[tex.helpptr]);
		}
		tex.errorReporter.error("ERROR"); // TODO dirty: sets the "worst error level so far" and stops on 100+ errors
	}
	
	/**
	 * Simple method to stop with a fatal error.
	 */
	void fatalError(String s) {
		tex.normalizeselector();
		tex.errorReporter.fatal(s);
	}
	
	/**
	 * Stops with a fatal error about capacity quantity s (string pool index) being n, which is too large.
	 */
	void overflow(final int s, final int n) {
		tex.normalizeselector();
		tex.errorReporter.fatal("!TeX capacity exceeded, sorry [" + tex.getStringFromPool(s) + '=' + n + "]. If you really absolutely need more capacity, you can ask a wizard to enlarge me.");
	}

	/**
	 * Stops with a fatal error because of "something that cannot happen".
	 */
	void confusion(final int s) {
		tex.normalizeselector();
		if (tex.errorReporter.getWorstLevelSoFar().ordinal() < Level.ERROR.ordinal()) {
			tex.errorReporter.fatal("!This can't happen (" + tex.getStringFromPool(s) + "). I'm broken. Please show this to someone who can fix can fix. ");
		} else {
			tex.errorReporter.fatal("!I can't go on meeting you like this. One of your faux pas seems to have wounded me deeply... in fact, I'm barely conscious. Please fix it and try again.");
		}
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
			tex.begindiagnostic();
			tex.printnl(825);
			tex.print(c);
			tex.print(826);
			tex.print(tex.fontname[f]);
			tex.printchar(33);
			tex.enddiagnostic(false);
		}
	}

}