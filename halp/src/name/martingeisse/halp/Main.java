/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.halp;

import java.io.File;

import name.martingeisse.halp.file.FilesystemResourceDescriber;

/**
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DescribeProblem<File> problem = new DescribeProblem<File>(new File("."));
		ProblemAssessor<DescribeProblem<File>> assessor = new FilesystemResourceDescriber();
		Assessment assessment = assessor.assess(problem);
		System.out.println(assessment.getText());
	}
	
}
