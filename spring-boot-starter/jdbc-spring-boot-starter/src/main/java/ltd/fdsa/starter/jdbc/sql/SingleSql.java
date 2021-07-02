package ltd.fdsa.starter.jdbc.sql;

import lombok.SneakyThrows;
import lombok.var;
import ltd.fdsa.starter.jdbc.mappers.GenericMapper;
import ltd.fdsa.starter.jdbc.mappers.PojoMapper;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.*;

public final class SingleSql<T> {
    Class<T> selectClazz;
    Class<?> includeClazz;
    List<String> excludeFields;
    List<String> selects = new ArrayList<>();
    SpelExpressionParser ep = new SpelExpressionParser();
    Map<String, Object> wheres = new HashMap<>();
    String tableName;
    static DataSource dataSource;

    private SingleSql(Class<T> clazz) {
        this.selectClazz = clazz;
    }

    private SingleSql(String tableName) {
        this.tableName = tableName;
    }

    public static <T> SingleSql<T> build(String tableName) {
        return new SingleSql(tableName);
    }

    public static <T> SingleSql<T> build(Class<T> clazz) {
        return new SingleSql<T>(clazz);
    }


    public SingleSql<T> select(String... fields) {
        this.selects.addAll(Arrays.asList(fields));
        return this;
    }

    public SingleSql<T> include(Class<?> clazz) {
        this.includeClazz = clazz;
        return this;
    }

    public SingleSql<T> exclude(String... fields) {
        this.excludeFields.addAll(Arrays.asList(fields));
        ;
        return this;
    }

    public SingleSql<T> where(String expressionString, Object... obj) {
        if (obj.length > 0) {
            this.wheres.put(expressionString, obj[0]);
        } else {
            this.wheres.put(expressionString, null);
        }
        return this;
    }

    @SneakyThrows
    public static <T> List<T> exec(String sql, Object args, Class<T> clazz) {
        var db = dataSource.getConnection().createStatement();
        var jdbcTemplate = new JdbcTemplate(dataSource);
        var list = jdbcTemplate.query(sql, new Object[0], GenericMapper.build(clazz));

        PojoMapper<T> pojo = new PojoMapper<T>();
        var lists = jdbcTemplate.query(sql, new Object[0], pojo);
        return list;
    }

    public static <T> int insert(Class<T> clazz, T... data) {
        return 0;
    }

    public int update(Object data) {

        return 0;
    }

    public int delete(boolean... physical) {
        return 0;
    }

    /*
     * index,size
     * default size:10000
     * */
    public List<T> pagedQuery(int... page) {
        return null;
    }
}
