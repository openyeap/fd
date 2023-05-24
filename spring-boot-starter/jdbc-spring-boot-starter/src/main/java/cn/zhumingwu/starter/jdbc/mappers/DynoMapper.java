package cn.zhumingwu.starter.jdbc.mappers;


import cn.zhumingwu.starter.jdbc.pojo.Dyno;
import cn.zhumingwu.starter.jdbc.pojo.DynoCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DynoMapper implements RowMapper<Dyno> {

    private DynoCreator dynoCreator;

    @Override
    public Dyno mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        if (dynoCreator == null) {
            dynoCreator = new DynoCreator(resultSet);
        }

        return dynoCreator.createDyno();
    }
}
