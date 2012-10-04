package phorum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumPmFolders is a Querydsl query type for PhorumPmFolders
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumPmFolders extends com.mysema.query.sql.RelationalPathBase<PhorumPmFolders> {

    private static final long serialVersionUID = 1040834106;

    public static final QPhorumPmFolders phorumPmFolders = new QPhorumPmFolders("phorum_pm_folders");

    public final StringPath foldername = createString("foldername");

    public final NumberPath<Integer> pmFolderId = createNumber("pm_folder_id", Integer.class);

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumPmFolders> primary = createPrimaryKey(pmFolderId);

    public QPhorumPmFolders(String variable) {
        super(PhorumPmFolders.class, forVariable(variable), "null", "phorum_pm_folders");
    }

    public QPhorumPmFolders(Path<? extends PhorumPmFolders> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_pm_folders");
    }

    public QPhorumPmFolders(PathMetadata<?> metadata) {
        super(PhorumPmFolders.class, metadata, "null", "phorum_pm_folders");
    }

}

