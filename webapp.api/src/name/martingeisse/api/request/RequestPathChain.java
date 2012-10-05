/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

import java.util.Iterator;
import java.util.NoSuchElementException;

import name.martingeisse.common.util.ParameterUtil;

/**
 * The request path is decoded into a chain of segments.
 */
public final class RequestPathChain implements Iterable<String> {

	/**
	 * the head
	 */
	private final String head;
	
	/**
	 * the tail
	 */
	private final RequestPathChain tail;
	
	/**
	 * Constructor.
	 * @param head the first segment of the chain
	 * @param tail the remaining segments of the chain
	 */
	public RequestPathChain(String head, RequestPathChain tail) {
		ParameterUtil.ensureNotNull(head, "head");
		this.head = head;
		this.tail = tail;
	}
	
	/**
	 * Parses a textual request path. Note that this method returns null
	 * for the empty path, since an empty chain is represented by null.
	 * @param requestPath the request path to parse
	 * @return the request path chain
	 * @throws MalformedRequestPathException if the path contains a leading slash or a double-slash
	 */
	public static RequestPathChain parse(String requestPath) throws MalformedRequestPathException {
		
		// special case: empty path
		if (requestPath.isEmpty()) {
			return null;
		}
		
		// special case: single-segment path
		int firstSlash = requestPath.indexOf('/');
		if (firstSlash == -1) {
			return new RequestPathChain(requestPath, null);
		}
		if (firstSlash == 0) {
			throw new MalformedRequestPathException("leading slash or double-slash in request path");
		}
		
		// general case: at least one (but possibly multiple) segment-slash prefixes, then a last segment
		return new RequestPathChain(requestPath.substring(0, firstSlash), parse(requestPath.substring(firstSlash + 1)));
		
	}

	/**
	 * Getter method for the head.
	 * @return the head
	 */
	public String getHead() {
		return head;
	}
	
	/**
	 * Getter method for the tail.
	 * @return the tail
	 */
	public RequestPathChain getTail() {
		return tail;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<String> iterator() {
		return new MyIterator(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		boolean first = true;
		for (String s : this) {
			if (first) {
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append(s);
		}
		builder.append(']');
		return builder.toString();
	}
	
	/**
	 * Null-safe version of toString(). Needed since the empty chain
	 * is represented by null.
	 * @param chain the chain
	 * @return the string
	 */
	public static String toString(RequestPathChain chain) {
		return (chain == null ? "[]" : chain.toString());
	}

	/**
	 * An interator for the segments in a request path chain.
	 */
	private static class MyIterator implements Iterator<String> {
		
		/**
		 * the nextNode
		 */
		private RequestPathChain nextNode;

		/**
		 * Constructor.
		 * @param firstNode the first not of the chain
		 */
		public MyIterator(RequestPathChain firstNode) {
			this.nextNode = firstNode;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return (nextNode != null);
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public String next() {
			if (nextNode == null) {
				throw new NoSuchElementException();
			}
			String result = nextNode.head;
			nextNode = nextNode.tail;
			return result;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
