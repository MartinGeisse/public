/**
 * This file was generated from the database schema.
 */
package name.martingeisse.hackathon.entity;

import name.martingeisse.wicket.model.database.EntityModel;
import name.martingeisse.sql.terms.IEntityWithId;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import name.martingeisse.sql.EntityConnectionManager;
import org.apache.wicket.model.IModel;
import java.io.Serializable;

/**
 * This class represents rows from table 'dummy'.
 */
public class Dummy implements Serializable, IEntityWithId<Long> {

    /**
     * Constructor.
     */
    public Dummy() {
    }

    /**
     * the foo
     */
    private String foo;

    /**
     * the id
     */
    private Long id;

    /**
     * Getter method for the foo.
     * @return the foo
     */
    public String getFoo() {
        return foo;
    }

    /**
     * Setter method for the foo.
     * @param foo the foo to set
     */
    public void setFoo(String foo) {
        this.foo = foo;
    }

    /**
     * Getter method for the id.
     * @return the id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Setter method for the id.
     * @param id the id to set
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{Dummy. foo = " + foo + ", id = " + id + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static Dummy findById(long id) {
        final QDummy q = QDummy.dummy;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Creates a model that loads a record by id.
     * @param id the id of the record to load
     * @return the model loading the record
     */
    public static IModel<Dummy> getModelForId(long id) {
        return new EntityModel<Dummy>(QDummy.dummy, QDummy.dummy.id.eq(id));
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QDummy q = QDummy.dummy;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.foo, foo);
        id = insert.executeWithKey(Long.class);
    }

}

