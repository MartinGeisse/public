/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.EntityExpressionUtil;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.Wildcard;

/**
 * An {@link IDataProvider} that fetches entity instances and returns
 * them as {@link EntityInstance} objects.
 */
public class EntityInstanceDataProvider implements IDataProvider<EntityInstance> {

	/**
	 * the entityModel
	 */
	private final IModel<EntityDescriptor> entityModel;

	/**
	 * the filter
	 */
	private final Predicate filter;

	/**
	 * the orderSpecifiers
	 */
	private final OrderSpecifier<? extends Comparable<?>>[] orderSpecifiers;

	/**
	 * Constructor.
	 * @param entityModel the model that provides the entity descriptor
	 */
	public EntityInstanceDataProvider(final IModel<EntityDescriptor> entityModel) {
		this.entityModel = entityModel;
		this.filter = null;
		this.orderSpecifiers = null;
	}

	/**
	 * Constructor.
	 * @param entityModel the model that provides the entity descriptor
	 * @param filter the filter
	 * @param orderSpecifiers the order specifiers that define the order of the results
	 */
	public EntityInstanceDataProvider(final IModel<EntityDescriptor> entityModel, final Predicate filter, final OrderSpecifier<? extends Comparable<?>>[] orderSpecifiers) {
		this.entityModel = entityModel;
		this.filter = filter;
		this.orderSpecifiers = orderSpecifiers;
	}

	/**
	 * Getter method for the entityModel.
	 * @return the entityModel
	 */
	public IModel<EntityDescriptor> getEntityModel() {
		return entityModel;
	}

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return getEntityModel().getObject();
	}

	/**
	 * Getter method for the filter.
	 * @return the filter
	 */
	public Predicate getFilter() {
		return filter;
	}

	/**
	 * Getter method for the orderSpecifiers.
	 * @return the orderSpecifiers
	 */
	public OrderSpecifier<? extends Comparable<?>>[] getOrderSpecifiers() {
		return orderSpecifiers;
	}

	/**
	 * This method can be implemented by subclasses to trigger additional behavior
	 * when the fetch result is available. The default implementation does nothing.
	 * @param resultSetMetaData the result set meta-data
	 */
	protected void onResultAvailable(final ResultSetMetaData resultSetMetaData) throws SQLException {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
	 */
	@Override
	public int size() {
		return (int)getEntity().count(filter);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int, int)
	 */
	@Override
	public Iterator<? extends EntityInstance> iterator(final int first, final int count) {
		try {
			
			// obtain a ResultSet
			final EntityDescriptor entity = getEntity();
			SQLQuery query = entity.query(EntityExpressionUtil.ALIAS);
			if (filter != null) {
				query = query.where(filter);
			}
			if (orderSpecifiers != null) {
				query = query.orderBy(orderSpecifiers);
			}
			final ResultSet resultSet = query.limit(count).offset(first).getResults(Wildcard.all);
			
			// fetch rows
			entity.checkDataRowMeta(resultSet);
			final List<EntityInstance> rows = new ArrayList<EntityInstance>();
			while (resultSet.next()) {
				rows.add(new EntityInstance(entity, resultSet));
			}
			
			// additional client-specific behavior
			onResultAvailable(resultSet.getMetaData());
			
			// clean up
			resultSet.close();
			
			// return the rows as an iterator
			return rows.iterator();
		
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
	 */
	@Override
	public IModel<EntityInstance> model(final EntityInstance object) {
		return Model.of(object);
	}

}
