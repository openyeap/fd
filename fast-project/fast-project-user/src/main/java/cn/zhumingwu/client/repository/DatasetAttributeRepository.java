package cn.zhumingwu.client.repository;

import cn.zhumingwu.client.entity.DatasetAttribute;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.annotation.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DatasetAttributeRepository {

    static Cache<String, Expression> local = CacheBuilder.newBuilder().maximumSize(1024)
        .expireAfterAccess(10L, TimeUnit.MINUTES).removalListener(removalNotification -> { })
        .build();


    @Resource
    JdbcTemplate jdbcTemplate;
    private final ExpressionParser parser = new SpelExpressionParser();
    @Resource
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<DatasetAttribute> page(int index, int pageSize, String order, String where, Object... param) {
        String limit = MessageFormat.format("LIMIT {0} OFFSET {1}", pageSize, pageSize * index);
        String sql = "SELECT cid,\nattribute_id,\ndataset_id,\nblob_id,\npath,\ntype,\nsize,\nwidth,\nheight,\ntags,\ntypes,\nscenes,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_dataset_attribute " +
             where +
             order +
             limit;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }


    public List<DatasetAttribute> query(String where, Object... param) {
        String sql = "SELECT cid,\nattribute_id,\ndataset_id,\nblob_id,\npath,\ntype,\nsize,\nwidth,\nheight,\ntags,\ntypes,\nscenes,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_dataset_attribute " + where;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }

    public List<DatasetAttribute> queryByPrimaryKey(Object key) {
        String sql = "SELECT cid,\nattribute_id,\ndataset_id,\nblob_id,\npath,\ntype,\nsize,\nwidth,\nheight,\ntags,\ntypes,\nscenes,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_dataset_attribute  WHERE cid=? \n;";
        return this.jdbcTemplate.query(sql, this::extractData, key);
    }

    public int insert(DatasetAttribute... entities) {
        String sql = "INSERT INTO t_dataset_attribute (cid,\nattribute_id,\ndataset_id,\nblob_id,\npath,\ntype,\nsize,\nwidth,\nheight,\ntags,\ntypes,\nscenes,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\n) VALUES (\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?\n)";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var datasetId = entity.getDatasetId();
            var fileId = entity.getFileId();
            var path = entity.getPath();
            var type = entity.getType();
            var size = entity.getSize();
            var width = entity.getWidth();
            var height = entity.getHeight();
            var tags = entity.getTags();
            var types = entity.getTypes();
            var scenes = entity.getScenes();
            var cid = entity.getCid();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, cid,id,datasetId,fileId,path,type,size,width,height,tags,types,scenes,createdTime,updatedTime,createdBy,updatedBy,status);
        }
        return result;
    }

    public int update(DatasetAttribute... entities) {

        String sql = "UPDATE t_dataset_attribute SET attribute_id=?,\ndataset_id=?,\nblob_id=?,\npath=?,\ntype=?,\nsize=?,\nwidth=?,\nheight=?,\ntags=?,\ntypes=?,\nscenes=?,\ncreate_time=?,\nupdate_time=?,\ncreate_by=?,\nupdate_by=?,\nstatus=?\n WHERE cid=? \n;";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var datasetId = entity.getDatasetId();
            var fileId = entity.getFileId();
            var path = entity.getPath();
            var type = entity.getType();
            var size = entity.getSize();
            var width = entity.getWidth();
            var height = entity.getHeight();
            var tags = entity.getTags();
            var types = entity.getTypes();
            var scenes = entity.getScenes();
            var cid = entity.getCid();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, id,datasetId,fileId,path,type,size,width,height,tags,types,scenes,createdTime,updatedTime,createdBy,updatedBy,status,cid);
        }
        return result;
    }

    public int delete(int... ids) {
        String sql = "UPDATE t_dataset_attribute SET status = -1 WHERE cid=? \n;";
        int result = 0;
        for (var id : ids) {
            result += this.jdbcTemplate.update(sql, id);
        }
        return result;
    }

    public int deleteAll(Integer... ids) {
        String sql = "DELETE FROM t_dataset_attribute WHERE cid=? \n;";
        var list = Arrays.asList(ids);
        int result = 0;
        for (var partition : Lists.partition(list, 1000)) {
            result += Arrays.stream(this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, partition.get(i));
                }
                @Override
                public int getBatchSize() {
                    return partition.size();
                }
            })).sum();
        }
        return result;
    }

    public <T> T getNamedQuery(String sql, ResultSetExtractor<T> rse, Map<String, ?> paramMap) {
        return this.namedParameterJdbcTemplate.query(sql, paramMap, rse);
    }

    public <T> T getQuery(String sql, ResultSetExtractor<T> rse, @Nullable Object... args) {
        return this.jdbcTemplate.query(sql, rse, args);
    }

    public <T> T getExpQuery(String spel, ResultSetExtractor<T> rse, Map<String, ?> paramMap) {
        var expression = local.getIfPresent(spel);
        if (expression == null) {
            expression = this.parser.parseExpression(spel);
            local.put(spel, expression);
        }
        var sql = expression.getValue(paramMap, String.class);
        assert sql != null;
        return this.jdbcTemplate.query(sql, rse);
    }

    List<DatasetAttribute> extractData(ResultSet rs)  {
        List<DatasetAttribute> list = new ArrayList<>();
		try {
            while (rs.next()) {
                DatasetAttribute entity = new DatasetAttribute();
                entity.setId(rs.getString("attribute_id"));
                entity.setDatasetId(rs.getString("dataset_id"));
                entity.setFileId(rs.getString("blob_id"));
                entity.setPath(rs.getString("path"));
                entity.setType(rs.getString("type"));
                entity.setSize(rs.getLong("size"));
                entity.setWidth(rs.getInt("width"));
                entity.setHeight(rs.getInt("height"));
                entity.setTags(rs.getLong("tags"));
                entity.setTypes(rs.getLong("types"));
                entity.setScenes(rs.getLong("scenes"));
                entity.setCid(rs.getInt("cid"));
                entity.setCreatedTime(rs.getDate("create_time"));
                entity.setUpdatedTime(rs.getDate("update_time"));
                entity.setCreatedBy(rs.getLong("create_by"));
                entity.setUpdatedBy(rs.getLong("update_by"));
                // todo: Status ;
                list.add(entity);
            }
        } catch (SQLException e) {
            log.error("error", e);
        }
        return list;
    }
}
