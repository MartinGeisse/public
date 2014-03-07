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
	 * TODO documentation
	 */
	void fatalerror(final int s) {
		tex.normalizeselector();
		tex.printnl(262);
		tex.print(287);
		tex.helpptr = 1;
		tex.helpline[0] = s;
		if (tex.logopened) {
			error();
		}
		tex.jumpout();
	}

	/**
	 * TODO documentation
	 */
	void overflow(final int s, final int n) {
		tex.normalizeselector();
		tex.printnl(262);
		tex.print(288);
		tex.print(s);
		tex.printchar(61);
		tex.printInt(n);
		tex.printchar(93);
		tex.helpptr = 2;
		tex.helpline[1] = 289;
		tex.helpline[0] = 290;
		if (tex.logopened) {
			error();
		}
		tex.jumpout();
	}

	/**
	 * TODO documentation
	 */
	void confusion(final int s) {
		tex.normalizeselector();
		if (tex.errorReporter.getWorstLevelSoFar().ordinal() < Level.ERROR.ordinal()) {
			tex.printnl(262);
			tex.print(291);
			tex.print(s);
			tex.printchar(41);
			tex.helpptr = 1;
			tex.helpline[0] = 292;
		} else {
			tex.printnl(262);
			tex.print(293);
			tex.helpptr = 2;
			tex.helpline[1] = 294;
			tex.helpline[0] = 295;
		}
		if (tex.logopened) {
			error();
		}
		tex.jumpout();
	}

}
