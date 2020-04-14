package ltd.fdsa.starter.jdbc.config;
 

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;

import lombok.Data;
import lombok.extern.slf4j.Slf4j; 

@Configuration
@ConfigurationProperties(prefix = "jdbc")
@Data
@Slf4j
public class DataSource4ReadConfig {
	private JdbcProperty config;

	@Bean("jdbcProperty")
	public JdbcProperty getJdbcConfig() {
		return this.config;
	}
	

}
