package cn.zhumingwu.database.sql.columns;

import cn.zhumingwu.database.sql.conditions.EqualityConditions;
import cn.zhumingwu.database.sql.conditions.NullableConditions;
import cn.zhumingwu.database.sql.domain.*;
import lombok.Getter;
import lombok.ToString;
import cn.zhumingwu.database.sql.domain.*;
import cn.zhumingwu.database.sql.schema.Table;
import cn.zhumingwu.database.sql.utils.BuilderUtils;
import cn.zhumingwu.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@Getter
@ToString
public abstract class Column implements Groupable, Selectable, Definable, NullableConditions, EqualityConditions, SqlTypeSupplier {
    @ToString.Exclude
    protected Table table;
    protected String name;

    private String alias;
    protected ColumnDefinition columnDefinition;

    private int sqlType;
    private String remark;

    public Column(Table table, String name, String alias, ColumnDefinition columnDefinition, int sqlType) {
        this.table = table;
        this.name = name;
        this.alias = alias;
        this.columnDefinition = columnDefinition;
        this.sqlType = sqlType;
    }

    public Column remark(String remark) {
        this.remark = remark;
        return this;
    }

    public Column as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String getValue(BuildingContext context, Indentation indentation) {
        return getFullNameOrAlias(context);
    }

    public String getFullName(BuildingContext context) {
        return table.getFullName(context) + "." + BuilderUtils.columnApostrophe(name, context);
    }

    public String getFullNameOrAlias(BuildingContext context) {
        return table.getFullNameOrAlias(context) + "." + BuilderUtils.columnApostrophe(name, context);
    }
}
