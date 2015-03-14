/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.halp.file;

import java.io.File;

import name.martingeisse.halp.Assessment;
import name.martingeisse.halp.DescribeProblem;
import name.martingeisse.halp.ProblemAssessor;
import name.martingeisse.halp.StringAssessment;

/**
 * This class is able to describe files, but not folders.
 */
public class FileDescriber implements ProblemAssessor<DescribeProblem<File>> {

	/* (non-Javadoc)
	 * @see name.martingeisse.halp.ProblemAssessor#assess(name.martingeisse.halp.Problem)
	 */
	@Override
	public Assessment assess(DescribeProblem<File> problem) {
		File file = problem.getThing();
		if (!file.isFile()) {
			return null;
		}
		return new StringAssessment("This is a file called " + file.getPath());
	}

}
