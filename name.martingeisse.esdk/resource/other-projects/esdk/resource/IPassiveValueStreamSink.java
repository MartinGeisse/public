/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.connect;

/**
 * A specialized {@link IPassiveValueSink} that never consumes its
 * value on its own. Instead, the consumeValue() method must be
 * called to make the current value take effect. Calling consumeValue()
 * again causes the same effect to occur again. Calling setValue()
 * multiple times between calls to consumeValue() should have the same
 * effect as if only the last call had occurred.
 * 
 * Cycle order: Clients should call setValue() first, then
 * consumeValue(). This well-defined order helps
 * both clients and implementations to maintain proper semantics.
 * 
 * This rule also implies that the initial value has no useful
 * meaning. Clients should not call consumeValue() using the
 * initially set value. Implementations may use a default value
 * or throw an exception if a client does it anyway.
 * 
 * TODO: evtl Streams nicht von Source/Sink ableiten, sondern
 * nur eine Methode für produce+return / set+consume. Das ist
 * einfacher zu implementieren und die Logik steckt dann mehr
 * im Connect.
 * 
 * @param <T> the value type
 */
public interface IPassiveValueStreamSink<T> extends IPassiveValueSink<T> {

	/**
	 * Consumes the currently set value.
	 */
	public void consumeValue();
	
}
