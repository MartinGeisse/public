/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.connect;

/**
 * This class is the entry point for building connections to devices.
 * 
 * No part of this class is *needed* to connect some device to another.
 * However, this class simplifies connection building for code that doesn't
 * want to deal with the specific type of connection needed. In this case,
 * this class can be passed the device to connect to and a desired connection
 * type, and this class will build an appropriate connection.
 * 
 * For example the user might want to connect a binary output-only device
 * (e.g. a simulated LED) to a softcore CPU (e.g. a PicoBlaze). He can, of
 * course, attach the LED manually to the PicoBlaze's I/O port. However, this
 * requires an enormous amount of different custom-tailored connections when
 * the project later needs to connect (simulated) LEDs, switches, buttons,
 * keyboards etc. to a PicoBlaze, high-end 64-bit CPU, or custom digital
 * logic, all at multiple abstraction levels. The intention of {@link ConnectUtil}
 * is to reduce the behavior of the various devices to a few abstracted
 * interfaces, then select one of a set of pre-built connections for them.
 */
public class ConnectUtil {

	/**
	 * Creates a connection to the specified device and of the specified type. If the
	 * connection can make use of a bus address, it uses the specified address (if any).
	 * Some connections do not use addresses, and the address is ignored in such
	 * connections. The address is optional, and a default behavior is assumed when null
	 * is passed.
	 * 
	 * @param <T> the intended connection interface type (e.g. IBusSlave32).
	 * @param device the device to connect to
	 * @param type the class object for type T
	 * @param address the bus address, or null for none
	 * @return the connection
	 */
	public static <T extends IConnectable> T connect(IConnectable device, Class<T> type, Integer address) {
		
		// simple case: the device is already of the desired type
		if (type.isAssignableFrom(device.getClass())) {
			return type.cast(device);
		}
		
		// handle connecting a device as a bus slave
		if (type == IBusSlave32.class || type == IBusSlave64.class) {
			return connectBusSlave(device, type, address);
		}
		
		throw new CannotConnectException("connect(): Not all connection types implemented yet -- " + type);
	}
	
	/**
	 * Connects the specified device as either an IBusSlave32 or IBusSlave64.
	 */
	private static <T extends IConnectable> T connectBusSlave(IConnectable device, Class<T> type, Integer address) {
		
		// handle bus size adapters
		if (type == IBusSlave32.class && (device instanceof IBusSlave64)) {
			return uncheckedCast(new BusSlave32FromBusSlave64((IBusSlave64)device));
		}
		if (type == IBusSlave64.class && (device instanceof IBusSlave32)) {
			return uncheckedCast(new BusSlave64FromBusSlave32((IBusSlave32)device));
		}
		
		// handle continuous streams
		if (device instanceof IPassiveValueSink<?>) {
			
		}
		weiteres problem: autoconnect muss nicht nur transfer-arten anpassen, sondern auch wertetypen!
		Das könnte man umgehen, indem man einheitliche Typen (z.B. int, long) verwendet. Dann wäre das
		hier z.B. ein vereinfachtes Connect-System mit nur int (long zu selten nötig, int gängiger)
		und das HDL-Package hätte seine allgemeine ValueSource<T> zurück. Für die meisten Anwendungsfälle, 
		wofür das hier gedacht war, reicht int vollkommen!
		
		
		
		
		// since the address argument is 32 bits wide, we can connect them to a 32-bit bus, then optionally adapt to 64
		! das stimmt nur für adressen aber nicht für werte!
		
		IBusSlave32 busSlave32 = connectNonBusSlaveAsBusSlave32(device, address);
		if (type == IBusSlave32.class) {
			return uncheckedCast(busSlave32);
		}
		if (type == IBusSlave64.class) {
			return uncheckedCast(new BusSlave64FromBusSlave32(busSlave32));
		}
		
		// cannot handle this type
		throw new CannotConnectException("connectBusSlave(): cannot handle desired connection type: " + type.getName());

	}

	/**
	 * Creates an IBusSlave32 from a device that isn't a bus slave at all.
	 */
	private static IBusSlave32 connectNonBusSlaveAsBusSlave32(IConnectable device, Integer address) {
		
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T uncheckedCast(Object x) {
		return (T)x;
	}
	
}
