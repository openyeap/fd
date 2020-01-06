package ltd.fdsa.job.executor.jobhandler;
 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ltd.fdsa.job.core.biz.model.ReturnT;
import ltd.fdsa.job.core.handler.IJobHandler;
import ltd.fdsa.job.core.log.JobLogger;


public class HttpJobHandler extends IJobHandler {

	@Override
	public ReturnT<String> execute(String param) throws Exception {

		// valid
		if (param==null || param.trim().length()==0) {
			JobLogger.log("URL Empty");
			return FAIL;
		}

		// request
		HttpURLConnection connection = null;
		BufferedReader bufferedReader = null;
		try {
			// connection
			URL realUrl = new URL(param);
			connection = (HttpURLConnection) realUrl.openConnection();

			// connection setting
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setReadTimeout(5 * 1000);
			connection.setConnectTimeout(3 * 1000);
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8");

			// do connection
			connection.connect();

			//Map<String, List<String>> map = connection.getHeaderFields();

			// valid StatusCode
			int statusCode = connection.getResponseCode();
			if (statusCode != 200) {
				throw new RuntimeException("Http Request StatusCode("+ statusCode +") Invalid.");
			}

			// result
			bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line);
			}
			String responseMsg = result.toString();

			JobLogger.log(responseMsg);
			return SUCCESS;
		} catch (Exception e) {
			JobLogger.log(e);
			return FAIL;
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e2) {
				JobLogger.log(e2);
			}
		}

	}

}
