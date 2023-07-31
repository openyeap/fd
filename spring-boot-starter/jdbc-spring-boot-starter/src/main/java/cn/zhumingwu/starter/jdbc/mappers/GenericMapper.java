package cn.zhumingwu.starter.jdbc.mappers;

import lombok.SneakyThrows;

import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenericMapper<T> implements RowMapper<T> {
    private Class<T> clazz;

    private GenericMapper(Class<T> clazz) {
        this.clazz = clazz;
    }
    public static <T> GenericMapper<T> build(Class<T> clazz) {
        return new GenericMapper<T>(clazz);
    }



    @Override
    public T mapRow(ResultSet rs, int rowNum) {
        T o = null;
        try {
            o = clazz.getDeclaredConstructor( ).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        for (var f : clazz.getFields()) {
            var name = f.getName();
            Object value = null;
            try {
                value = rs.getObject(name);
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