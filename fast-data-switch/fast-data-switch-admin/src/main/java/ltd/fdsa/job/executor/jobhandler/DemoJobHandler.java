package ltd.fdsa.job.executor.jobhandler;
 

import java.util.concurrent.TimeUnit;

import ltd.fdsa.job.core.biz.model.ReturnT;
import ltd.fdsa.job.core.handler.IJobHandler;
import ltd.fdsa.job.core.log.JobLogger;


public class DemoJobHandler extends IJobHandler {

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		JobLogger.log(", Hello World.");

		for (int i = 0; i < 5; i++) {
			JobLogger.log("beat at:" + i);
			TimeUnit.SECONDS.sleep(2);
		}
		return SUCCESS;
	}

}
