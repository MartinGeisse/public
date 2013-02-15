/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.verilog.compiler.wave;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the data from a value change dump (VCD) file.
 */
public final class ValueChangeDump {

	/**
	 * the timescaleAmount
	 */
	private final long timescaleAmount;
	
	/**
	 * the timescaleUnit
	 */
	private final String timescaleUnit;
	
	/**
	 * the variables
	 */
	private final List<Variable> variables;
	
	/**
	 * Constructor.
	 * @param sourceCode the VCD source code
	 * @throws IOException on I/O errors
	 * @throws SyntaxException on syntax errors
	 */
	public ValueChangeDump(final String sourceCode) throws IOException, SyntaxException {
		this(new StringReader(sourceCode));
	}
	
	/**
	 * Constructor.
	 * @param sourceCode the VCD source code
	 * @throws IOException on I/O errors
	 * @throws SyntaxException on syntax errors
	 */
	public ValueChangeDump(final Reader sourceCode) throws IOException, SyntaxException {
		Tokenizer tokenizer = new Tokenizer(sourceCode);
		
		// parse definitions
		long timescaleAmount = 1;
		String timescaleUnit = "ns";
		this.variables = new ArrayList<ValueChangeDump.Variable>();
		while (true) {
			String token = tokenizer.expectKeywordToken();
			if ("$enddefinitions".contentEquals(token)) {
				tokenizer.expectEnd();
				break;
			} else if ("$timescale".contentEquals(token)) {
				timescaleAmount =  tokenizer.expectLong();
				timescaleUnit = tokenizer.expectToken().toString();
				tokenizer.expectEnd();
			} else if ("$var".contentEquals(token)) {
				String type = tokenizer.expectToken().toString();
				int size = tokenizer.expectInt();
				String internalIdentifier = tokenizer.expectToken().toString();
				String originalIdentifier = tokenizer.expectToken().toString();
				variables.add(new Variable(type, size, internalIdentifier, originalIdentifier));
			}
		}
		this.timescaleAmount = timescaleAmount;
		this.timescaleUnit = timescaleUnit;
		
		// parse value changes
		long currentTime = 0;
		while (true) {
			String token = tokenizer.nextToken();
			if (token == null) {
				break;
			}
			char firstChar = token.charAt(0);
			String rest = token.substring(1);
			Object value;
			String variableName;
			if (firstChar == '#') {
				try {
					currentTime = Long.parseLong(rest);
				} catch (NumberFormatException e) {
					throw new SyntaxException("invalid time specifier: " + token);
				}
				continue;
			} else if (firstChar == 'b') {
				value = parseBinaryValue(rest);
				variableName = tokenizer.expectToken();
			} else if (firstChar == 'r') {
				throw new SyntaxException("real-number values not yet supported");
			} else {
				if (firstChar == '0') {
					value = false;
				} else if (firstChar == '1') {
					value = true;
				} else if (firstChar == 'z' || firstChar == 'Z') {
					value = ValueChange.VALUE_Z;
				} else {
					value = null;
				}
				variableName = rest;
			}
			Variable variable = findVariableByInternalIdentifier(variableName);
			if (variable == null) {
				throw new SyntaxException("unknown variable: " + variableName);
			}
			variable.getValueChanges().add(new ValueChange(currentTime, value));
		}
		
		// sort value changes
		for (Variable variable : variables) {
			Collections.sort(variable.getValueChanges(), ValueChange.TIME_COMPARATOR);
		}
		
	}
	
	/**
	 * 
	 * @param rest
	 * @return
	 */
	private static Object parseBinaryValue(String rest) {
		return null;
	}
	
	/**
	 * Returns the variable with the specified internal identifier, or null if not found.
	 * @param internalIdentifier the internal identifier to look for
	 * @return the variable or null
	 */
	public Variable findVariableByInternalIdentifier(String internalIdentifier) {
		for (Variable variable : variables) {
			if (variable.getInternalIdentifier().equals(internalIdentifier)) {
				return variable;
			}
		}
		return null;
	}
	
	/**
	 * Returns the variable with the specified original identifier, or null if not found.
	 * @param originalIdentifier the original identifier to look for
	 * @return the variable or null
	 */
	public Variable findVariableByOriginalIdentifier(String originalIdentifier) {
		for (Variable variable : variables) {
			if (variable.getOriginalIdentifier().equals(originalIdentifier)) {
				return variable;
			}
		}
		return null;
	}
	
	/**
	 * Getter method for the timescaleAmount.
	 * @return the timescaleAmount
	 */
	public long getTimescaleAmount() {
		return timescaleAmount;
	}
	
	/**
	 * Getter method for the timescaleUnit.
	 * @return the timescaleUnit
	 */
	public String getTimescaleUnit() {
		return timescaleUnit;
	}
	
	/**
	 * Getter method for the variables.
	 * @return the variables
	 */
	public List<Variable> getVariables() {
		return variables;
	}
	
	/**
	 * A VCD variable.
	 */
	public static final class Variable {
	
		/**
		 * the type
		 */
		private final String type;
		
		/**
		 * the size
		 */
		private final int size;
		
		/**
		 * the internalIdentifier
		 */
		private final String internalIdentifier;
		
		/**
		 * the originalIdentifier
		 */
		private final String originalIdentifier;
		
		/**
		 * the valueChanges
		 */
		private final List<ValueChange> valueChanges;

		/**
		 * Constructor.
		 * @param type the variable type (wire, reg, ...)
		 * @param size the bit size
		 * @param internalIdentifier the identifier used internally in the VCD file
		 * @param originalIdentifier the original identifier used by the simulated design
		 */
		public Variable(final String type, final int size, final String internalIdentifier, final String originalIdentifier) {
			this.type = type;
			this.size = size;
			this.internalIdentifier = internalIdentifier;
			this.originalIdentifier = originalIdentifier;
			this.valueChanges = new ArrayList<ValueChange>();
		}

		/**
		 * Getter method for the type.
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		
		/**
		 * Getter method for the size.
		 * @return the size
		 */
		public int getSize() {
			return size;
		}
		
		/**
		 * Getter method for the internalIdentifier.
		 * @return the internalIdentifier
		 */
		public String getInternalIdentifier() {
			return internalIdentifier;
		}
		
		/**
		 * Getter method for the originalIdentifier.
		 * @return the originalIdentifier
		 */
		public String getOriginalIdentifier() {
			return originalIdentifier;
		}
		
		/**
		 * Getter method for the valueChanges.
		 * @return the valueChanges
		 */
		public List<ValueChange> getValueChanges() {
			return valueChanges;
		}
		
	}
	
}
