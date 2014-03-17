/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.equivalence;

import java.io.File;
import java.io.StringWriter;
import name.martingeisse.phunky.runtime.PhpRuntime;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * PHP equivalence tests using a single source file
 * from the single-source-file-equivalence folder.
 * This test case actually scans that folder and
 * executes all those tests.
 */
public final class SingleSourceFileEquivalenceTests {

	/**
	 * @throws Exception ...
	 */
	@Test
	public void runTests() throws Exception {
		runTests(new File("tests/single-source-file-equivalence"));
	}
	
	/**
	 * 
	 */
	private void runTests(File file) throws Exception {
		if (file.isDirectory()) {
			for (File sub : file.listFiles()) {
				runTests(sub);
			}
		} else if (file.isFile() && file.getName().endsWith(".php")) {
			runTest(file);
		}
	}
	
	/**
	 * @param file
	 */
	private void runTest(File file) throws Exception {
		
		// run with Phunky
		String phunkyOutput;
		{
			StringWriter phunkyStringWriter = new StringWriter();
			PhpRuntime runtime = new PhpRuntime(true);
			runtime.setOutputWriter(phunkyStringWriter);
			runtime.getInterpreter().execute(file);
			phunkyOutput = phunkyStringWriter.toString();
		}
		
		// run with PHP
		String phpOutput;
		{
			Process process = Runtime.getRuntime().exec("php " + file.getPath());
			phpOutput = IOUtils.toString(process.getInputStream(), "iso-8859-1");
		}

		// compare them
		Assert.assertEquals(phpOutput, phunkyOutput);
		
	}
	
}
