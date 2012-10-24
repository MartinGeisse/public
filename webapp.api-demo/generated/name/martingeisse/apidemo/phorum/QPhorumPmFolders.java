/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumPmFolders is a Querydsl query type for PhorumPmFolders
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumPmFolders extends com.mysema.query.sql.RelationalPathBase<PhorumPmFolders> {

    private static final long serialVersionUID = -448296607;

    /**
     * The default instance of this class.
     */
    public static final QPhorumPmFolders phorumPmFolders = new QPhorumPmFolders("phorum_pm_folders");

    /**
     * Metamodel property for property 'foldername'
     */
    public final StringPath foldername = createString("foldername");

    /**
     * Metamodel property for property 'pm_folder_id'
     */
    public final NumberPath<Integer> pmFolderId = createNumber("pm_folder_id", Integer.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumPmFolders> pk_primary = createPrimaryKey(pmFolderId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumPmFolders(String variable) {
        super(PhorumPmFolders.class, forVariable(variable), "null", "phorum_pm_folders");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumPmFolders(Path<? extends PhorumPmFolders> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_pm_folders");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumPmFolders(PathMetadata<?> metadata) {
        super(PhorumPmFolders.class, metadata, "null", "phorum_pm_folders");
    }

}

