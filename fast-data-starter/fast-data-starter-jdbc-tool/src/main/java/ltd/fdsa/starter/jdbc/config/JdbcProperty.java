package ltd.fdsa.starter.jdbc.config;

import com.baomidou.mybatisplus.annotation.DbType;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JdbcProperty {

	private String driver;
	private String URL;
	private String parantName;
	private String userName;
	private String password;
	private String schemaName;
	private String[] tablePrefix;
	private String[] table;
	private DbType dbType;
	private String packageName;

}
