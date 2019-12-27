package ltd.fdsa.fdsql.web.config;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.*; 

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
	@Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
	String driverClassName;
	@Value("${spring.datasource.url:url}")
	String url;

	@Value("${spring.datasource.username:postgres}")
	String username;
	@Value("${spring.datasource.password:postgres}")
	String password;

	@Bean
	public JdbcTemplate primaryTemplate() {

		return new JdbcTemplate(this.getDataSource());
	}

	private DataSource getDataSource() {
		DataSource dataSource = DataSourceBuilder
				.create()
				.driverClassName(driverClassName)
				.url(url)
				.username(username)
				.password(password)
				.build();
		
		return dataSource;
	}

	@Bean
	public Connection getConnection() throws SQLException {
		return this.getDataSource().getConnection();
	}
}