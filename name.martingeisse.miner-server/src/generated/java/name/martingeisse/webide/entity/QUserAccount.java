/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QUserAccount is a Querydsl query type for UserAccount
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QUserAccount extends com.mysema.query.sql.RelationalPathBase<UserAccount> {

    private static final long serialVersionUID = -1832878727;

    /**
     * The default instance of this class.
     */
    public static final QUserAccount userAccount = new QUserAccount("user_account");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'password_hash'
     */
    public final StringPath passwordHash = createString("password_hash");

    /**
     * Metamodel property for property 'username'
     */
    public final StringPath username = createString("username");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<UserAccount> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for reverse foreign key 'player_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Player> fk__playerIbfk1 = createInvForeignKey(id, "user_account_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QUserAccount(String variable) {
        super(UserAccount.class, forVariable(variable), "null", "user_account");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QUserAccount(Path<? extends UserAccount> path) {
        super(path.getType(), path.getMetadata(), "null", "user_account");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QUserAccount(PathMetadata<?> metadata) {
        super(UserAccount.class, metadata, "null", "user_account");
    }

}

