package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumFiles is a Querydsl query type for PhorumFiles
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumFiles extends com.mysema.query.sql.RelationalPathBase<PhorumFiles> {

    private static final long serialVersionUID = -975744196;

    public static final QPhorumFiles phorumFiles = new QPhorumFiles("phorum_files");

    public final NumberPath<Integer> addDatetime = createNumber("add_datetime", Integer.class);

    public final StringPath fileData = createString("file_data");

    public final NumberPath<Integer> fileId = createNumber("file_id", Integer.class);

    public final StringPath filename = createString("filename");

    public final NumberPath<Integer> filesize = createNumber("filesize", Integer.class);

    public final StringPath link = createString("link");

    public final NumberPath<Integer> messageId = createNumber("message_id", Integer.class);

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumFiles> primary = createPrimaryKey(fileId);

    public QPhorumFiles(String variable) {
        super(PhorumFiles.class, forVariable(variable), "null", "phorum_files");
    }

    public QPhorumFiles(Path<? extends PhorumFiles> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_files");
    }

    public QPhorumFiles(PathMetadata<?> metadata) {
        super(PhorumFiles.class, metadata, "null", "phorum_files");
    }

}

