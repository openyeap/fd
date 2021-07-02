package ltd.fdsa.starter.jdbc.mappers;

import lombok.SneakyThrows;
import lombok.var;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

public class GenericMapper<T> implements RowMapper<T> {
    private Class<T> clazz;

    private GenericMapper(Class<T> clazz) {
        this.clazz = clazz;
    }
    public static <T> GenericMapper<T> build(Class<T> clazz) {
        return new GenericMapper<T>(clazz);
    }


    @SneakyThrows
    @Override
    public T mapRow(ResultSet rs, int rowNum) {
        T o = clazz.newInstance();
        for (var f : clazz.getFields()) {
            var name = f.getName();
            var value = rs.getObject(name);
            f.set(o, value);
        }
        return o;
    }
}