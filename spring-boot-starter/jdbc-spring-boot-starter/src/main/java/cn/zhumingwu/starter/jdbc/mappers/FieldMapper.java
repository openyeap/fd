package cn.zhumingwu.starter.jdbc.mappers;

import java.sql.ResultSet;

@FunctionalInterface
public interface FieldMapper {
    void map(ResultSet resultSet, Object object);
}
