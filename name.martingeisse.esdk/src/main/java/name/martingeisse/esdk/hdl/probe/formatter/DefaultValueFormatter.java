/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.hdl.probe.formatter;


/**
 * Default implementation for {@link ValueFormatter} that
 * can handle various common cases. It can handle arbitrary
 * values using {@link #toString()} as a fallback.
 */
public class DefaultValueFormatter implements ValueFormatter<Object> {

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.probe.formatter.ValueFormatter#formatValue(java.lang.Object)
	 */
	@Override
	public String formatValue(Object value) {
		if (value == null) {
			return "(null)";
		} else if (value instanceof boolean[]) {
			
			// TODO do not use a plain boolean array since it does not respect
			// the convention to reverse index order. Java programmers *Expect*
			// boolean arrays to be written in ascending index order, while
			// HDL experts expect descending index order. Instead, create a
			// class called BitVector (or BinaryVector, to avoid confusion
			// even more) that specifies descending order, and also
			// is immutable. Could use java.util.BitSet internally.
			// Also supports concatenation. toString() also respects
			// descending-index notation.
			// boolean[] isn't used then.
			// --
			// java.util.BitSet already uses little-endian bit order when
			// creating a bit set from 'long's. So BinaryVector works
			// similar, only it is immutable and its operations are more
			// suited to hardware bit handling. 
			
			StringBuilder builder = new StringBuilder();
			for (boolean b : (boolean[])value) {
				builder.append(b ? '1' : '0');
			}
			return builder.toString();
		} else if (value instanceof Boolean[]) {
			
			// TODO see above
			
			StringBuilder builder = new StringBuilder();
			for (boolean b : (Boolean[])value) {
				builder.append(b ? '1' : '0');
			}
			return builder.toString();
		} else {
			return value.toString();
		}
	}

}
