/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.repltest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.BinaryLogClient.EventListener;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

/**
 * Hello world!
 */
public class Main {

	/**
	 * the tableMap
	 */
	private static Map<Long, String> tableNames = new HashMap<Long, String>();
	
	/**
	 * The main method.
	 * @param args ignored
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {
		
		// TODO use DESCRIBE TABLE to map column IDs to column names and
		// especially to find which one is the ID
		
		final BinaryLogClient client = new BinaryLogClient("localhost", 3306, "onlinecourses", "username", "password");
		client.registerEventListener(new EventListener() {
			@Override
			public void onEvent(final Event event) {
				EventData untypedEventData = event.getData();
				switch (event.getHeader().getEventType()) {
				
				case TABLE_MAP: {
					TableMapEventData data = (TableMapEventData)untypedEventData;
					if (data.getDatabase().equals("onlinecourses")) {
						tableNames.put(data.getTableId(), data.getTable());
					}
					break;
				}
					
				case WRITE_ROWS: {
					WriteRowsEventData data = (WriteRowsEventData)untypedEventData;
					long tableId = data.getTableId();
					String tableName = tableNames.get(tableId);
					for (Serializable[] row : data.getRows()) {
						long id = Long.parseLong(row[0].toString());
						System.out.println("insert " + tableName + "." + id);
					}
					break;
				}
					
				case UPDATE_ROWS: {
					UpdateRowsEventData data = (UpdateRowsEventData)untypedEventData;
					long tableId = data.getTableId();
					String tableName = tableNames.get(tableId);
					for (Map.Entry<Serializable[], Serializable[]> entry : data.getRows()) {
						Serializable[] oldRow = entry.getKey();
						long id = Long.parseLong(oldRow[0].toString());
						// Serializable[] newRow = entry.getValue();
						System.out.println("update: " + tableName + "." + id);
					}
					break;
				}
					
				case DELETE_ROWS: {
					DeleteRowsEventData data = (DeleteRowsEventData)untypedEventData;
					long tableId = data.getTableId();
					String tableName = tableNames.get(tableId);
					for (Serializable[] row : data.getRows()) {
						long id = Long.parseLong(row[0].toString());
						System.out.println("delete " + tableName + "." + id);
					}
					break;
				}
					
				default: {
					break;
				}
					
				}
			}
		});
		client.connect();
	}

}
