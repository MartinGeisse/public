/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.protocol.http.BufferedWebResponse;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;


/**
 * This component should be attached to an empty element. It does not touch the
 * element; rather, it looks for a {@link BufferedWebResponse} and flushes it.
 *
 * Flushing has been tested to work (using a delay after flushing) and has the
 * obvious effect that a subsequent exception will print the stack trace / page
 * expired below the flushed response data -- no resetting possible -- so use
 * the early flusher with care!
 */
public class ResponseFlusher extends WebComponent {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public ResponseFlusher(String id) {
		super(id);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);

		/*
		 * Flush early to allow the browser to load satellite files while the server
		 * is performing the search. Flushing has been tested to work (using a
		 * delay after flushing) and has the obvious effect that a subsequent
		 * exception will print the stack trace / page expired below the flushed
		 * HEAD, so use the early flusher with care!
		 */
		Response response = RequestCycle.get().getResponse();
		if (response instanceof BufferedWebResponse) {
			BufferedWebResponse bufferedWebResponse = (BufferedWebResponse)response;
			// the following lines are Wicketese for "flush"
			bufferedWebResponse.close();
			bufferedWebResponse.reset();
			try {
				((HttpServletResponse)bufferedWebResponse.getContainerResponse()).flushBuffer();
			} catch (IOException e) {
			}
		}
			
	}
	
}
