/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.backend;

/**
 * This exception type indicates a problem connecting to the
 * backend. It does not indicate the kind of problem -- this
 * should be analyzed by looking at the logs.
 */
public final class BackendConnectionException extends RuntimeException {
}
