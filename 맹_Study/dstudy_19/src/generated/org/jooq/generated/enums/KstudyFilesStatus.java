/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.enums;


import org.jooq.Catalog;
import org.jooq.EnumType;
import org.jooq.Schema;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public enum KstudyFilesStatus implements EnumType {

    ACTIVE("ACTIVE"),

    DELETED("DELETED");

    private final String literal;

    private KstudyFilesStatus(String literal) {
        this.literal = literal;
    }

    @Override
    public Catalog getCatalog() {
        return null;
    }

    @Override
    public Schema getSchema() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    /**
     * Lookup a value of this EnumType by its literal. Returns
     * <code>null</code>, if no such value could be found, see {@link
     * EnumType#lookupLiteral(Class, String)}.
     */
    public static KstudyFilesStatus lookupLiteral(String literal) {
        return EnumType.lookupLiteral(KstudyFilesStatus.class, literal);
    }
}
