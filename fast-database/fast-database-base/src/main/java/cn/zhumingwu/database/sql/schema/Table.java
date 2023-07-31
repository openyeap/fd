package cn.zhumingwu.database.sql.schema;

import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.columns.DefaultColumn;
import cn.zhumingwu.database.sql.columns.DefaultColumnBuilder;
import cn.zhumingwu.database.sql.columns.datetime.*;
import cn.zhumingwu.database.sql.columns.number.doubletype.*;
import cn.zhumingwu.database.sql.columns.number.integer.*;
import cn.zhumingwu.database.sql.columns.string.*;
import cn.zhumingwu.database.sql.conditions.Condition;
import cn.zhumingwu.database.sql.queries.Select;
import lombok.Getter;
import lombok.ToString;

import cn.zhumingwu.database.sql.columns.datetime.*;
import cn.zhumingwu.database.sql.columns.number.doubletype.*;
import cn.zhumingwu.database.sql.columns.number.integer.*;
import cn.zhumingwu.database.sql.columns.string.*;
import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.domain.JoinableTable;
import cn.zhumingwu.database.sql.domain.Queryable;
import cn.zhumingwu.database.sql.utils.BuilderUtils;
import cn.zhumingwu.database.sql.utils.Indentation;

import java.util.Arrays;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@Getter
@ToString
public class Table implements Queryable {
    private final String name;
    private String alias;

    @ToString.Exclude
    private Schema schema;
    @ToString.Exclude
    private Column[] columns;
    @ToString.Exclude
    private String type;
    private String remark;

    Table(Schema schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    Table(Schema schema, String name, String alias) {
        this.schema = schema;
        this.name = name;
        this.alias = alias;
    }

    Table(Schema schema, String name, String alias, String type, String remark) {
        this.schema = schema;
        this.name = name;
        this.alias = alias;
        this.type = type;
        this.remark = remark;
    }

    @Override
    public String getValue(BuildingContext context, Indentation indentation) {
        return getFullName(context);
    }

    public String getFullName(BuildingContext context) {
        return ((getSchema() != null && getSchema().getName()!=null) ? BuilderUtils.columnApostrophe(getSchema().getName(), context) + "." : "") + BuilderUtils
                .columnApostrophe(getName(), context);
    }

    public String getFullNameOrAlias(BuildingContext context) {
        return alias != null ? BuilderUtils.columnApostrophe(alias, context) : getFullName(context);
    }

    /**
     * Creates a clone from the current {@link Table} but with the given alias
     *
     * @param alias the alias for the {@link Table}
     * @return the created {@link Table}-clone with the alias
     */
    public Table as(String alias) {
        this.alias = alias;
        return this;
    }

    public Table type(String type) {
        this.type = type;
        return this;
    }

    public Table remark(String remark) {
        this.remark = remark;
        return this;
    }

    /**
     * Creates a {@link JoinableTable} to be used in {@link Select#join(JoinableTable)} on the given {@link Condition}
     *
     * @param condition the {@link Condition} on which the join should be done
     * @return the {@link JoinableTable}
     */
    public JoinableTable on(Condition condition) {
        return new JoinableTable(null, this, condition);
    }

    /**
     * Creates a {@link BigIntColumnBuilder} to build a {@link BigIntColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link BigIntColumn} to be build
     * @return the {@link BigIntColumnBuilder} to build a {@link BigIntColumn} from
     */
    public BigIntColumnBuilder bigIntColumn(String columnName) {
        return new BigIntColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link CharColumnBuilder} to build a {@link CharColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link CharColumn} to be build
     * @return the {@link CharColumnBuilder} to build a {@link CharColumn} from
     */
    public CharColumnBuilder charColumn(String columnName) {
        return new CharColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link DecimalColumnBuilder} to build a {@link DecimalColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link DecimalColumn} to be build
     * @return the {@link DecimalColumnBuilder} to build a {@link DecimalColumn} from
     */
    public DecimalColumnBuilder decimalColumn(String columnName) {
        return new DecimalColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link DoubleColumnBuilder} to build a {@link DoubleColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link DoubleColumn} to be build
     * @return the {@link DoubleColumnBuilder} to build a {@link DoubleColumn} from
     */
    public DoubleColumnBuilder doubleColumn(String columnName) {
        return new DoubleColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link FloatColumnBuilder} to build a {@link FloatColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link FloatColumn} to be build
     * @return the {@link FloatColumnBuilder} to build a {@link FloatColumn} from
     */
    public FloatColumnBuilder floatColumn(String columnName) {
        return new FloatColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link IntColumnBuilder} to build a {@link IntColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link IntColumn} to be build
     * @return the {@link IntColumnBuilder} to build a {@link IntColumn} from
     */
    public IntColumnBuilder intColumn(String columnName) {
        return new IntColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link MediumIntColumnBuilder} to build a {@link MediumIntColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link MediumIntColumn} to be build
     * @return the {@link MediumIntColumnBuilder} to build a {@link MediumIntColumn} from
     */
    public MediumIntColumnBuilder mediumIntColumn(String columnName) {
        return new MediumIntColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link SmallIntColumnBuilder} to build a {@link SmallIntColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link SmallIntColumn} to be build
     * @return the {@link SmallIntColumnBuilder} to build a {@link SmallIntColumn} from
     */
    public SmallIntColumnBuilder smallIntColumn(String columnName) {
        return new SmallIntColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link TextColumnBuilder} to build a {@link TextColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link TextColumn} to be build
     * @return the {@link TextColumnBuilder} to build a {@link TextColumn} from
     */
    public TextColumnBuilder textColumn(String columnName) {
        return new TextColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link TinyIntColumnBuilder} to build a {@link TinyIntColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link TinyIntColumn} to be build
     * @return the {@link TinyIntColumnBuilder} to build a {@link TinyIntColumn} from
     */
    public TinyIntColumnBuilder tinyIntColumn(String columnName) {
        return new TinyIntColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link DefaultColumnBuilder} to build a {@link DefaultColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link TinyIntColumn} to be build
     * @return the {@link DefaultColumnBuilder} to build a {@link DefaultColumn} from
     */
    public DefaultColumnBuilder column(String columnName) {
        return new DefaultColumnBuilder(this, columnName);
    }

    public Column[] columns(String... names) {
        for (var name : names) {
            this.column(name).build();
        }
        return this.getColumns();
    }

    /**
     * Creates a {@link VarCharColumnBuilder} to build a {@link VarCharColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link VarCharColumn} to be build
     * @return the {@link VarCharColumnBuilder} to build a {@link VarCharColumn} from
     */
    public VarCharColumnBuilder varCharColumn(String columnName) {
        return new VarCharColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link DateColumnBuilder} to build a {@link DateColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link DateColumn} to be build
     * @return the {@link DateColumnBuilder} to build a {@link DateColumn} from
     */
    public DateColumnBuilder dateColumn(String columnName) {
        return new DateColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link DateTimeColumnBuilder} to build a {@link DateTimeColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link DateTimeColumn} to be build
     * @return the {@link DateTimeColumnBuilder} to build a {@link DateTimeColumn} from
     */
    public DateTimeColumnBuilder dateTimeColumn(String columnName) {
        return new DateTimeColumnBuilder(this, columnName);
    }

    /**
     * Creates a {@link TimeColumnBuilder} to build a {@link TimeColumn} with the given name for the current {@link Table}
     *
     * @param columnName the name for the {@link TimeColumn} to be build
     * @return the {@link TimeColumnBuilder} to build a {@link TimeColumn} from
     */
    public TimeColumnBuilder timeColumn(String columnName) {
        return new TimeColumnBuilder(this, columnName);
    }

    <T extends Column> T addColumn(T column) {

        if (this.columns == null || this.columns.length == 0) {
            this.columns = new Column[1];
            this.columns[0] = column;
        } else {
            this.columns = Arrays.copyOf(this.columns, this.columns.length + 1);
            this.columns[this.columns.length - 1] = column;
        }
        return column;
    }

    /**
     * Creates a {@link Table} with the given name
     *
     * @param name the name for the {@link Table}
     * @return the created {@link Table}
     */
    public static Table create(String name) {
        return new Table(null, name);
    }
}
