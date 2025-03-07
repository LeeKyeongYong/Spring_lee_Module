/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import org.jooq.generated.tables.JPostDetails;
import org.jooq.generated.tables.pojos.PostDetails;
import org.jooq.impl.TableRecordImpl;


/**
 * View 'msa.post_details' references invalid table(s) or column(s) or
 * function(s) or definer/invoker of view lack rights to use them
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PostDetailsRecord extends TableRecordImpl<PostDetailsRecord> {

    private static final long serialVersionUID = 1L;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PostDetailsRecord
     */
    public PostDetailsRecord() {
        super(JPostDetails.POST_DETAILS);
    }

    /**
     * Create a detached, initialised PostDetailsRecord
     */
    public PostDetailsRecord(PostDetails value) {
        super(JPostDetails.POST_DETAILS);

        resetChangedOnNotNull();
    }
}
