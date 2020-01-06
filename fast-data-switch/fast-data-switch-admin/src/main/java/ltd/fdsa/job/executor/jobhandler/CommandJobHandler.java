package ltd.fdsa.job.executor.jobhandler;

 
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import ltd.fdsa.job.core.biz.model.ReturnT;
import ltd.fdsa.job.core.handler.IJobHandler;
import ltd.fdsa.job.core.log.JobLogger;

public class CommandJobHandler extends IJobHandler {

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        String command = param;
        int exitValue = -1;

        BufferedReader bufferedReader = null;
        try {
            // command process
            Process process = Runtime.getRuntime().exec(command);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

            // command log
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                JobLogger.log(line);
            }

            // command exit
            process.waitFor();
            exitValue = process.exitValue();
        } catch (Exception e) {
            JobLogger.log(e);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

        if (exitValue == 0) {
            return IJobHandler.SUCCESS;
        } else {
            return new ReturnT<String>(IJobHandler.FAIL.getCode(), "command exit value("+exitValue+") is failed");
        }
    }

}
