/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.halp.file;

import java.io.File;

import name.martingeisse.halp.Assessment;
import name.martingeisse.halp.DescribeProblem;
import name.martingeisse.halp.ProblemAssessor;

/**
 * This class is able to describe files and folders.
 */
public class FilesystemResourceDescriber implements ProblemAssessor<DescribeProblem<File>> {

	/* (non-Javadoc)
	 * @see name.martingeisse.halp.ProblemAssessor#assess(name.martingeisse.halp.Problem)
	 */
	@Override
	public Assessment assess(DescribeProblem<File> problem) {
		File file = problem.getThing();
		if (file.isFile()) {
			return new FileDescriber().assess(problem);
		} else if (file.isDirectory()) {
			return new FolderDescriber().assess(problem);
		} else {
			return null;
		}
	}
	
}
