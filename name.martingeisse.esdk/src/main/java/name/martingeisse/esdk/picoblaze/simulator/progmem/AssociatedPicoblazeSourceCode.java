/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator.progmem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;
import name.martingeisse.esdk.picoblaze.assembler.Range;
import name.martingeisse.esdk.picoblaze.assembler.ast.AstBuilder;
import name.martingeisse.esdk.picoblaze.assembler.ast.Context;
import name.martingeisse.esdk.picoblaze.assembler.ast.PsmFile;

/**
 * Loads the source code for a Picoblaze program from
 * a file associated with this class (or a class passed
 * as a constructor parameter). The file extension
 * to use is ".psm"
 */
public class AssociatedPicoblazeSourceCode extends PicoblazeInstructionMemory {

	/**
	 * Constructor.
	 */
	public AssociatedPicoblazeSourceCode() {
		this(null);
	}

	/**
	 * Constructor.
	 * @param anchorClass the class that determines the location and name
	 * of the source code file.
	 */
	public AssociatedPicoblazeSourceCode(Class<?> anchorClass) {
		if (anchorClass == null) {
			anchorClass = getClass();
		}
		IPicoblazeAssemblerErrorHandler errorHandler = new IPicoblazeAssemblerErrorHandler() {
			
			@Override
			public void handleWarning(Range range, String message) {
				System.err.println(range + ": " + message);
			}
			
			@Override
			public void handleError(Range range, String message) {
				String fullMessage = range + ": " + message;
				System.err.println(fullMessage);
				throw new RuntimeException(fullMessage);
			}
			
		};
		AstBuilder astBuilder = new AstBuilder();
		String fileName = anchorClass.getSimpleName() + ".psm";
		try (InputStream inputStream = anchorClass.getResourceAsStream(fileName)) {
			if (inputStream == null) {
				throw new RuntimeException("could not open classpath resource: " + anchorClass + " -- " + fileName);
			}
			try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
				astBuilder.parse(reader, errorHandler);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		PsmFile psmFile = astBuilder.getResult();
		Context context = new Context(errorHandler);
		psmFile.collectConstantsAndLabels(context);
		setInstructions(psmFile.encode(context, errorHandler));
	}
	
}
