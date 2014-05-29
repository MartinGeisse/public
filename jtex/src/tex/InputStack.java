/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package tex;

/**
 * TODO: This comment describes the goal, not the current situation
 * 
 * Represents the stack of inputs used by the TeX engine and behaves as
 * an abstract stream of tokens. All input operations should use this
 * class, such as:
 * - reading tokens
 * - opening and closing input files
 * - "unreading" tokens (psuhing back)
 * - inserting synthetic tokens, e.g. for error recovery (TODO: also for macro expansion?)
 * 
 */
public final class InputStack {

	/**
	 * Maximum number of simultaneous entries 
	 */
	public static final int STACK_SIZE = 200;
	
	/**
	 * the tex
	 */
	private final Tex tex;

	/**
	 * Constructor.
	 * @param tex the TeX engine
	 */
	InputStack(Tex tex) {
		this.tex = tex;
	}

	/**
	 * Removes as many finished token lists from the input stack as possible.
	 */
	public void popFinishedTokenLists() {
		while ((tex.curinput.getState() == Tex.TOKENIZER_STATE_TOKEN_LIST) && (tex.curinput.getLoc() == 0)) {
			tex.endtokenlist();
		}
	}

	/**
	 * Pushes a copy of the current input on the input stack.
	 */
	public void duplicate() {
		if (tex.inputptr == STACK_SIZE) {
			tex.errorLogic.overflow(593, STACK_SIZE);
		}
		tex.inputStackBackingArray[tex.inputptr].copyFrom(tex.curinput);
		tex.inputptr++;
	}

	/**
	 * Pops the top input off the input stack and makes it the current input.
	 */
	public void pop() {
		tex.inputptr--;
		tex.curinput.copyFrom(tex.inputStackBackingArray[tex.inputptr]);
	}

	/**
	 * Closes and removes all inputs on the input stack.
	 * 
	 * TODO: Since the TOS is kept in curinput, not in inputStackBackingArray, this seems
	 * to leave the bottom input open...!?
	 */
	public void close() {
		while (tex.inputptr > 0) {
			if (tex.curinput.getState() == Tex.TOKENIZER_STATE_TOKEN_LIST) {
				tex.endtokenlist();
			} else {
				tex.endfilereading();
			}
		}
	}

	/**
	 * Inserts a single token, to be read next. The source specifies where that token comes from.
	 * @param token the token to insert
	 * @param source where the token comes from (called "token_type" in the original TeX, for
	 * example 3 = read but pushed back, 4 = inserted synthetic token, ...; p. 125)
	 */
	public void insertToken(int token, int source) {
		popFinishedTokenLists();
		int p = tex.allocateMemoryWord();
		tex.mem[p].setlh(token);
		if (token < 768) {
			if (token < 512) {
				tex.alignstate = tex.alignstate - 1;
			} else {
				tex.alignstate = tex.alignstate + 1;
			}
		}
		duplicate();
		tex.curinput.setState(Tex.TOKENIZER_STATE_TOKEN_LIST);
		tex.curinput.setStart(p);
		tex.curinput.setIndex(source);
		tex.curinput.setLoc(p);
	}
	
}
