/*7:*/
/*2:*/
package tex;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class wordfile extends DataInputStream {
	public boolean eof = false;

	public String name;

	public wordfile(final InputStream in) {
		super(in);
	}

	public wordfile(final TeXFile texFile) throws FileNotFoundException {
		super(new DataInputStream(new BufferedInputStream(new FileInputStream(texFile))));
		name = texFile.getName();
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
}/*:7*/
