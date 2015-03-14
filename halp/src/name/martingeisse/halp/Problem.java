/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.halp;

/**
 * Base interface for all problems.
 * 
 * In HALP speech, a "problem" is a formal representation of
 * a query or command entered by the user. This interface separates
 * the stages of *parsing* the user input -- thus mapping a more
 * loose description of the problem to a formal one -- and *assessing*
 * the problem, usually by presenting data to the user and/or
 * offering one or more actions to perform.
 * 
 * There is no pre-defined structure to problem objects. Instead,
 * problems are matched against known patterns to find ways to
 * deal with them.
 */
public interface Problem {
}
