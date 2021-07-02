package ltd.fdsa.starter.jdbc.mappers;


import ltd.fdsa.starter.jdbc.pojo.Dyno;
import ltd.fdsa.starter.jdbc.pojo.DynoCreator;
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
