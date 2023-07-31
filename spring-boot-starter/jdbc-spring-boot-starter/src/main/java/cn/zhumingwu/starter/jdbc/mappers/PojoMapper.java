package cn.zhumingwu.starter.jdbc.mappers;

import lombok.SneakyThrows;

import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PojoMapper<T> implements RowMapper<T> {

    private Class<T> clazz;

    public PojoMapper() {
        this.clazz = (Class<T>) ((ParameterizedType) (this.getClass().getGenericInterfaces()[0])).getActualTypeArguments()[0];
    }


    @Override

    public T mapRow(ResultSet resultSet, int rowNum) {
        T o = null;
        try {
            o = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (var f : clazz.getFields()) {
            var name = f.getName();
            Object value = null;
            try {
                value = resultSet.getObject(name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                f.set(o, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return o;
    }
}
