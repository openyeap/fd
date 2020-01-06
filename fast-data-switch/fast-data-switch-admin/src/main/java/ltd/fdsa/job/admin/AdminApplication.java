package ltd.fdsa.job.admin;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.job.executor.config.FrameLessJobConfig;

@SpringBootApplication
@Slf4j
public class AdminApplication {

	public static void main(String[] args) {
		try {
			// start
			// FrameLessJobConfig.getInstance().initJobExecutor();
			SpringApplication.run(AdminApplication.class, args);
//	            while (true) {
//	                TimeUnit.HOURS.sleep(1);
//	            }
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			// destory
			FrameLessJobConfig.getInstance().destoryJobExecutor();
		}

	}

}