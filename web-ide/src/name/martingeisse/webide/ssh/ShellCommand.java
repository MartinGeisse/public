/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

/**
 * The shell command.
 */
public class ShellCommand implements Command, Runnable {

	/**
	 * the in
	 */
	private InputStream in;
	
	/**
	 * the out
	 */
	private OutputStream out;
	
	/**
	 * the err
	 */
	private OutputStream err;
	
	/**
	 * the exitCallback
	 */
	private ExitCallback exitCallback;
	
	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#setInputStream(java.io.InputStream)
	 */
	@Override
	public void setInputStream(final InputStream in) {
		this.in = in;
	}

	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#setOutputStream(java.io.OutputStream)
	 */
	@Override
	public void setOutputStream(final OutputStream out) {
		this.out = out;
	}

	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#setErrorStream(java.io.OutputStream)
	 */
	@Override
	public void setErrorStream(final OutputStream err) {
		this.err = err;
	}

	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#setExitCallback(org.apache.sshd.server.ExitCallback)
	 */
	@Override
	public void setExitCallback(final ExitCallback callback) {
		this.exitCallback = callback;
	}

	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#start(org.apache.sshd.server.Environment)
	 */
	@Override
	public void start(final Environment env) throws IOException {
        new Thread(this, "SSH shell thread").start();
	}

	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#destroy()
	 */
	@Override
	public void destroy() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		TODO: LF -> CR LF, am besten direkt im charset oder streamreader/writer (wie macht "in" das genau,
		"out" erwartet scheinbar CRLF
		
		try {
			LineNumberReader reader = new LineNumberReader(new InputStreamReader(in, "utf-8"));
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "utf-8"));
			while (true) {
				String line = reader.readLine();
				if (line == null || line.equals("exit")) {
					writer.println("exit.");
					writer.flush();
					break;
				}
				writer.println("echo: " + line);
				writer.flush();
			}
		} catch (Exception e) {
		}
        if (exitCallback != null) {
        	exitCallback.onExit(0, "ok");
        }
	}
	
}
