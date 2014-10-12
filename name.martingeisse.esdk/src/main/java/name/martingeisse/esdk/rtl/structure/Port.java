/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.rtl.structure;

/**
 * Common base class for {@link InputPort} and {@link OutputPort}.
 * 
 * This class defines port meta-data for synthesis.
 *
 * @param <P> the port subclass, used to implement method chaining
 */
public abstract class Port<T, P extends Port<T, P>> extends ValueNexus<T, P> {
}
