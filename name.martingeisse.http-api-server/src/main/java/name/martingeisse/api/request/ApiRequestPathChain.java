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
 * 
 * The empty chain is represented by the special value EMPTY
 * which is a singleton (i.e. no other code can create an empty
 * chain). The empty chain is the only chain whose head
 * segment is null.
 */
public final class ApiRequestPathChain implements Iterable<String> {

	/**
	 * The empty chain.
	 */
	public static final ApiRequestPathChain EMPTY = new ApiRequestPathChain();
	
	/**
	 * the head
	 */
	private final String head;
	
	/**
	 * the tail
	 */
	private final ApiRequestPathChain tail;
	
	/**
	 * Constructor.
	 */
	private ApiRequestPathChain() {
		this.head = null;
		this.tail = null;
	}
	
	/**
	 * Constructor.
	 * @param head the first segment of the chain
	 * @param tail the remaining segments of the chain
	 */
	public ApiRequestPathChain(String head, ApiRequestPathChain tail) {
		this.head = ParameterUtil.ensureNotNull(head, "head");
		this.tail = ParameterUtil.ensureNotNull(tail, "tail");
	}
	
	/**
	 * Parses a textual request path.
	 * @param requestPath the request path to parse
	 * @return the request path chain
	 * @throws MalformedRequestPathException if the path contains a leading slash or a double-slash
	 */
	public static ApiRequestPathChain parse(String requestPath) throws MalformedRequestPathException {
		
		// special case: empty path
		if (requestPath.isEmpty()) {
			return EMPTY;
		}
		
		// special case: single-segment path
		int firstSlash = requestPath.indexOf('/');
		if (firstSlash == -1) {
			return new ApiRequestPathChain(requestPath, EMPTY);
		}
		if (firstSlash == 0) {
			throw new MalformedRequestPathException("leading slash or double-slash in request path");
		}
		
		// general case: at least one (but possibly multiple) segment-slash prefixes, then a last segment
		return new ApiRequestPathChain(requestPath.substring(0, firstSlash), parse(requestPath.substring(firstSlash + 1)));
		
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
	public ApiRequestPathChain getTail() {
		return tail;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<String> iterator() {
		return new MyIterator(this);
	}
	
	/**
	 * Returns null if this chain is empty, false if nonempty. Note that EMPTY
	 * is the only empty chain, i.e. it is a singleton.
	 * @return the empty
	 */
	public boolean isEmpty() {
		return (head == null);
	}
	
	/**
	 * Converts this path back to a URI.
	 * @return the URI
	 */
	public String getUri() {
		if (isEmpty()) {
			return "/";
		} else {
			StringBuilder builder = new StringBuilder();
			for (String segment : this) {
				builder.append('/').append(segment);
			}
			return builder.toString();
		}
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
	 * An iterator for the segments in a request path chain.
	 */
	private static class MyIterator implements Iterator<String> {
		
		/**
		 * the nextNode
		 */
		private ApiRequestPathChain nextNode;

		/**
		 * Constructor.
		 * @param firstNode the first not of the chain
		 */
		public MyIterator(ApiRequestPathChain firstNode) {
			ParameterUtil.ensureNotNull(firstNode, "firstNode");
			this.nextNode = firstNode;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return !nextNode.isEmpty();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public String next() {
			if (nextNode.isEmpty()) {
				throw new NoSuchElementException();
			}
			String result = nextNode.head;
			nextNode = ParameterUtil.ensureNotNull(nextNode.tail, "tail");
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
