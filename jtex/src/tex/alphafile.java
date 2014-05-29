/*3:*/
/*2:*/
package tex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class alphafile extends BufferedReader {
	public int filebuf;

	public boolean eof = false;

	public String name;

	public alphafile(final Reader in) {
		super(in);
	}

	public alphafile(final TeXFile texFile) throws FileNotFoundException {
		super(new BufferedReader(new FileReader(texFile)));
		try {
			filebuf = super.read();
		} catch (final IOException e) {
			System.err.println("I/O error reading " + name);
			eof = true;
		}
		name = texFile.getName();
	}

	public alphafile(final InputStream in) {
		super(new BufferedReader(new InputStreamReader(in)));
		name = "System.in";
	}

	@Override
	public int read() {
		try {
			int c = filebuf;
			filebuf = super.read();
			if (c == -1) {
				eof = true;
				c = 0;
			}
			return c;
		} catch (final IOException e) {
			eof = true;
		}
		return 0;
	}

	public void get() {
		try {
			filebuf = super.read();
		} catch (final IOException e) {
			eof = true;
		}
	}

	public boolean eoln() {
		return (eof || filebuf == '\n' || filebuf == '\r');
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
}/*:3*/
