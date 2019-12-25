package ltd.fdsa.fdsql.web.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.*;
import java.util.Map;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

	@Bean
	public JdbcTemplate primaryTemplate() {

		DataSource dataSource = DataSourceBuilder
				.create()
				.driverClassName("org.postgresql.Driver")
				.url("jdbc:postgresql://10.168.4.22:5321/landplan?currentSchema=landplan")
				.username("postgres")
				.password("postgres")
				.build();

		return new JdbcTemplate(dataSource);
	}

}