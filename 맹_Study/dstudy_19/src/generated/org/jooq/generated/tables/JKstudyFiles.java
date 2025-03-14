/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.generated.Indexes;
import org.jooq.generated.JMsa;
import org.jooq.generated.Keys;
import org.jooq.generated.enums.KstudyFilesStatus;
import org.jooq.generated.tables.records.KstudyFilesRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JKstudyFiles extends TableImpl<KstudyFilesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>msa.kstudy_files</code>
     */
    public static final JKstudyFiles KSTUDY_FILES = new JKstudyFiles();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<KstudyFilesRecord> getRecordType() {
        return KstudyFilesRecord.class;
    }

    /**
     * The column <code>msa.kstudy_files.author_id</code>.
     */
    public final TableField<KstudyFilesRecord, Long> AUTHOR_ID = createField(DSL.name("author_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>msa.kstudy_files.create_date</code>.
     */
    public final TableField<KstudyFilesRecord, LocalDateTime> CREATE_DATE = createField(DSL.name("create_date"), SQLDataType.LOCALDATETIME(0), this, "");

    /**
     * The column <code>msa.kstudy_files.file_size</code>.
     */
    public final TableField<KstudyFilesRecord, Long> FILE_SIZE = createField(DSL.name("file_size"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>msa.kstudy_files.id</code>.
     */
    public final TableField<KstudyFilesRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>msa.kstudy_files.modify_date</code>.
     */
    public final TableField<KstudyFilesRecord, LocalDateTime> MODIFY_DATE = createField(DSL.name("modify_date"), SQLDataType.LOCALDATETIME(0), this, "");

    /**
     * The column <code>msa.kstudy_files.checksum</code>.
     */
    public final TableField<KstudyFilesRecord, String> CHECKSUM = createField(DSL.name("checksum"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>msa.kstudy_files.content_type</code>.
     */
    public final TableField<KstudyFilesRecord, String> CONTENT_TYPE = createField(DSL.name("content_type"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>msa.kstudy_files.file_path</code>.
     */
    public final TableField<KstudyFilesRecord, String> FILE_PATH = createField(DSL.name("file_path"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>msa.kstudy_files.file_type</code>.
     */
    public final TableField<KstudyFilesRecord, String> FILE_TYPE = createField(DSL.name("file_type"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>msa.kstudy_files.original_file_name</code>.
     */
    public final TableField<KstudyFilesRecord, String> ORIGINAL_FILE_NAME = createField(DSL.name("original_file_name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>msa.kstudy_files.stored_file_name</code>.
     */
    public final TableField<KstudyFilesRecord, String> STORED_FILE_NAME = createField(DSL.name("stored_file_name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>msa.kstudy_files.status</code>.
     */
    public final TableField<KstudyFilesRecord, KstudyFilesStatus> STATUS = createField(DSL.name("status"), SQLDataType.VARCHAR(7).nullable(false).asEnumDataType(KstudyFilesStatus.class), this, "");

    private JKstudyFiles(Name alias, Table<KstudyFilesRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private JKstudyFiles(Name alias, Table<KstudyFilesRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>msa.kstudy_files</code> table reference
     */
    public JKstudyFiles(String alias) {
        this(DSL.name(alias), KSTUDY_FILES);
    }

    /**
     * Create an aliased <code>msa.kstudy_files</code> table reference
     */
    public JKstudyFiles(Name alias) {
        this(alias, KSTUDY_FILES);
    }

    /**
     * Create a <code>msa.kstudy_files</code> table reference
     */
    public JKstudyFiles() {
        this(DSL.name("kstudy_files"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JMsa.MSA;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.KSTUDY_FILES_FKFVD4P0KVN4TXV97717U8CY68N);
    }

    @Override
    public Identity<KstudyFilesRecord, Long> getIdentity() {
        return (Identity<KstudyFilesRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<KstudyFilesRecord> getPrimaryKey() {
        return Keys.KEY_KSTUDY_FILES_PRIMARY;
    }

    @Override
    public JKstudyFiles as(String alias) {
        return new JKstudyFiles(DSL.name(alias), this);
    }

    @Override
    public JKstudyFiles as(Name alias) {
        return new JKstudyFiles(alias, this);
    }

    @Override
    public JKstudyFiles as(Table<?> alias) {
        return new JKstudyFiles(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public JKstudyFiles rename(String name) {
        return new JKstudyFiles(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JKstudyFiles rename(Name name) {
        return new JKstudyFiles(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public JKstudyFiles rename(Table<?> name) {
        return new JKstudyFiles(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKstudyFiles where(Condition condition) {
        return new JKstudyFiles(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKstudyFiles where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKstudyFiles where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKstudyFiles where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JKstudyFiles where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JKstudyFiles where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JKstudyFiles where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JKstudyFiles where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKstudyFiles whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKstudyFiles whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
