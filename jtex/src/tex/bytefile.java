/*5:*/
/*2:*/
package tex;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class bytefile extends DataInputStream {
	public int filebuf;

	public String name;

	public bytefile(final InputStream in) {
		super(in);
	}

	public bytefile(final TeXFile texFile) throws FileNotFoundException {
		super(new DataInputStream(new BufferedInputStream(new FileInputStream(texFile))));
		try {
			filebuf = super.read();
		} catch (final IOException e) {
		}
		name = texFile.getName();
	}

	@Override
	public int read() {
		try {
			final int c = filebuf;
			filebuf = super.read();
			return c;
		} catch (final EOFException e) {
		} catch (final IOException e) {
		}
		return 0;
	}

	public void get() {
		try {
			filebuf = super.read();
		} catch (final IOException e) {
		}
	}

	@Override
	public void close() {
		try {
			super.close();
		} catch (final IOException e) {
			System.err.println();
			System.err.print("Problem closing " + name);
		}
	}
}/*:5*/
