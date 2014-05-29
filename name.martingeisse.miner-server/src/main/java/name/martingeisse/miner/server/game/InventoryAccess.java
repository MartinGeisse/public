/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.miner.server.game;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.sql.JdbcEntityDatabaseConnection;
import name.martingeisse.webide.entity.PlayerInventorySlot;
import name.martingeisse.webide.entity.QPlayerInventorySlot;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * Database utility class to deal with players' inventories.
 */
public final class InventoryAccess {

	/**
	 * the playerId
	 */
	private final long playerId;

	/**
	 * Constructor.
	 * @param playerId the ID of the player whose inventory shall be accessed
	 */
	public InventoryAccess(long playerId) {
		this.playerId = playerId;
	}

	/**
	 * Getter method for the playerId.
	 * @return the playerId
	 */
	public long getPlayerId() {
		return playerId;
	}
	
	/**
	 * Adds an item to the player's inventory. This is equivalent to add(type, 1).
	 * @param type the item type
	 */
	public void add(ItemType type) {
		add(type, 1);
	}
	
	/**
	 * Adds an item to the player's inventory.
	 * @param type the item type
	 * @param quantity the quantity of the item stack
	 */
	public void add(ItemType type, int quantity) {
		QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
		
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery().from(qpis);
		int previousInventoryLength = (int)query.where(qpis.playerId.eq(playerId), qpis.equipped.isFalse()).count();
		
		SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(qpis);
		insert.set(qpis.playerId, playerId);
		insert.set(qpis.equipped, false);
		insert.set(qpis.index, previousInventoryLength);
		insert.set(qpis.type, type.ordinal());
		insert.set(qpis.quantity, quantity);
		insert.execute();
		
	}
	
	/**
	 * Lists all items in the player's inventory, both equipped and in the backpack.
	 * The returned list contains all backpack items first, then all equipped items.
	 * Both groups are sorted by index.
	 * 
	 * @return the list of items
	 */
	public List<PlayerInventorySlot> listAll() {
		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
		final SQLQuery q = EntityConnectionManager.getConnection().createQuery().from(qpis);
		q.where(qpis.playerId.eq(playerId)).orderBy(qpis.equipped.asc(), qpis.index.asc());
		return q.list(qpis);
	}
	
	/**
	 * Lists the items in the player's backpack, sorted by index.
	 * 
	 * @return the list of items
	 */
	public List<PlayerInventorySlot> listBackback() {
		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
		final SQLQuery q = EntityConnectionManager.getConnection().createQuery().from(qpis);
		q.where(qpis.playerId.eq(playerId), qpis.equipped.isFalse()).orderBy(qpis.index.asc());
		return q.list(qpis);
	}
	
	/**
	 * Lists the player's equipped items, sorted by index.
	 * 
	 * @return the list of items
	 */
	public List<PlayerInventorySlot> listEquipped() {
		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
		final SQLQuery q = EntityConnectionManager.getConnection().createQuery().from(qpis);
		q.where(qpis.playerId.eq(playerId), qpis.equipped.isTrue()).orderBy(qpis.index.asc());
		return q.list(qpis);
	}

	/**
	 * Deletes a backpack item, specified by index.
	 * @param index the item's index.
	 */
	public void deleteBackpackItemByIndex(int index) {
		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(qpis);
		delete.where(qpis.playerId.eq(playerId), qpis.equipped.isFalse(), qpis.index.eq(index));
		delete.execute();
		renumber(false);
	}
	
	/**
	 * Deletes an equipped item, specified by index.
	 * @param index the item's index.
	 */
	public void deleteEquippedItemByIndex(int index) {
		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(qpis);
		delete.where(qpis.playerId.eq(playerId), qpis.equipped.isTrue(), qpis.index.eq(index));
		delete.execute();
		renumber(true);
	}

	/**
	 * Equips the backpack item with the specified index.
	 * @param index the item's index
	 */
	public void equip(int index) {
		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
		SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(qpis);
		update.where(qpis.playerId.eq(playerId), qpis.equipped.isFalse(), qpis.index.eq(index));
		update.set(qpis.equipped, true);
		update.execute();
		renumber(false);
		renumber(true);
	}
	
	/**
	 * Unequips the equipped item with the specified index.
	 * @param index the item's index
	 */
	public void unequip(int index) {
		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
		SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(qpis);
		update.where(qpis.playerId.eq(playerId), qpis.equipped.isTrue(), qpis.index.eq(index));
		update.set(qpis.equipped, false);
		update.execute();
		renumber(false);
		renumber(true);
	}
	
	/**
	 * 
	 */
	private void renumber(boolean equipped) {
		try {
			JdbcEntityDatabaseConnection entityConnection = (JdbcEntityDatabaseConnection)EntityConnectionManager.getConnection();
			Connection connection = entityConnection.getJdbcConnection();
			Statement statement = connection.createStatement();
			statement.execute("SELECT (@a := -1);");
			statement.execute("UPDATE `player_inventory_slot` SET `index` = (@a := @a + 1) WHERE `player_id` = " + playerId + " AND `equipped` = " + (equipped ? '1' : '0') + ";");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Finds an item in the player's inventory by item type.
	 * @param type the item type
	 * @param equipped whether to look for equipped items or backpack items
	 * @return the index, or -1 if not found
	 */
	public int findByType(ItemType type, boolean equipped) {
		final QPlayerInventorySlot qpis = QPlayerInventorySlot.playerInventorySlot;
		final SQLQuery q = EntityConnectionManager.getConnection().createQuery().from(qpis);
		q.where(qpis.playerId.eq(playerId), qpis.equipped.eq(equipped), qpis.type.eq(type.ordinal()));
		Integer result = q.singleResult(qpis.index);
		return (result == null ? -1 : result);
	}
	
}
