/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.pojos;


import java.io.Serializable;


/**
 * View 'msa.member_view' references invalid table(s) or column(s) or
 * function(s) or definer/invoker of view lack rights to use them
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MemberView implements Serializable {

    private static final long serialVersionUID = 1L;


    public MemberView() {}

    public MemberView(MemberView value) {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MemberView other = (MemberView) obj;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MemberView (");


        sb.append(")");
        return sb.toString();
    }
}
