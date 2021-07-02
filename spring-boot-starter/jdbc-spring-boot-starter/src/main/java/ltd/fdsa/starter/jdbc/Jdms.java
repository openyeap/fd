//package ltd.fdsa.starter.jdbc;
//
//
//import lombok.SneakyThrows;
//import lombok.experimental.var;
//import ltd.fdsa.starter.jdbc.collection.LazyResultSetCollection;
//import ltd.fdsa.starter.jdbc.mappers.DynoMapper;
//import ltd.fdsa.starter.jdbc.mappers.Mapper;
//import ltd.fdsa.starter.jdbc.mappers.PojoMapper;
//import ltd.fdsa.starter.jdbc.pojo.Dyno;
//import ltd.fdsa.starter.jdbc.pojo.DynoCreator;
//import ltd.fdsa.starter.jdbc.sql.ParameterConsumer;
//import ltd.fdsa.starter.jdbc.sql.ParameterizedSql;
//import ltd.fdsa.starter.jdbc.sql.Parameterizer;
//import org.apache.tomcat.util.http.fileupload.MultipartStream;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class Jdms {
//    private final JdbcTemplate jdbcTemplate;
//    private ParameterConsumer parameterConsumer;
//    private Parameterizer parameterizer;
//
//    public Jdms(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//        this.parameterConsumer = new ParameterConsumer();
//        this.parameterizer = new Parameterizer();
//    }
//
//    @SneakyThrows
//    public Collection<Dyno> query(String sql, boolean buffered) {
//        Statement statement = this.jdbcTemplate.getDataSource().getConnection().createStatement();
//        ResultSet rs = statement.executeQuery(sql);
//
//        if (buffered) {
//            List<Dyno> dynos = new ArrayList<>();
//
//            DynoCreator creator = new DynoCreator(rs);
//
//            while (rs.next()) {
//                dynos.add(creator.createDyno());
//            }
//
//            return dynos;
//        } else {
//            return new LazyResultSetCollection<>(rs, new DynoMapper(), Dyno.class);
//        }
//    }
//
//    public Collection<Dyno> query(String sql) {
//        return query(sql, true);
//    }
//
//    @SneakyThrows
//    public <T> Collection<T> query(String sql, Class<T> clazz, boolean buffered) {
//        Statement statement = this.jdbcTemplate.getDataSource().getConnection().createStatement();
//
//        ResultSet rs = statement.executeQuery(sql);
//
//        var mapper = new PojoMapper.build(clazz);
//
//        if (buffered) {
//            List<T> results = new ArrayList<>();
//
//            while (rs.next()) {
//                results.add(mapper.(rs, clazz));
//            }
//
//            return results;
//        } else {
//            return new LazyResultSetCollection<>(rs, mapper, clazz);
//        }
//    }
//
//    public <T> Collection<T> query(String sql, Class<T> clazz) {
//        return query(sql, clazz, true);
//    }
//
//    public <T> Collection<T> query(String sql, Class<T> clazz, Object param) {
//        return query(sql, clazz, param, true);
//    }
//
//    @SneakyThrows
//    public <T> Collection<T> query(String sql, Class<T> clazz, Object param, boolean buffered) {
//
//        Mapper mapper = new PojoMapper();
//
//        ParameterizedSql parameterizedSql = parameterizer.parameterizeSql(sql);
//
//        PreparedStatement ps = this.jdbcTemplate.getDataSource().getConnection().prepareStatement(parameterizedSql.sql);
//
//        parameterConsumer.consumeParameter(ps, parameterizedSql.parameterNames, param);
//
//        ResultSet rs = ps.executeQuery();
//
//        if (buffered) {
//            List<T> results = new ArrayList<>();
//
//            while (rs.next()) {
//                results.add(mapper.createObjectFromResultSet(rs, clazz));
//            }
//
//            return results;
//        } else {
//            return new LazyResultSetCollection<>(rs, mapper, clazz);
//        }
//    }
//
//    @SneakyThrows
//    public int execute(String sql) {
//        Statement statement = this.jdbcTemplate.getDataSource().getConnection().createStatement();
//        return statement.executeUpdate(sql);
//    }
//
//    @SneakyThrows
//    public int executeBatch(String sql, Collection params) {
//        ParameterizedSql parameterizedSql = parameterizer.parameterizeSql(sql);
//
//        PreparedStatement ps =  this.jdbcTemplate.getDataSource().getConnection().prepareStatement(parameterizedSql.sql);
//
//        for (Object param : params) {
//            parameterConsumer.consumeParameter(ps, parameterizedSql.parameterNames, param);
//            ps.addBatch();
//        }
//
//        int[] counts = ps.executeBatch();
//
//        int sum = 0;
//
//        for (int count : counts) {
//            sum += count;
//        }
//
//        return sum;
//    }
//
//    @SneakyThrows
//    public int execute(String sql, Object param) {
//        ParameterizedSql parameterizedSql = parameterizer.parameterizeSql(sql);
//
//        PreparedStatement ps =  this.jdbcTemplate.getDataSource().getConnection().prepareStatement(parameterizedSql.sql);
//
//        parameterConsumer.consumeParameter(ps, parameterizedSql.parameterNames, param);
//
//        return ps.executeUpdate();
//    }
//}
