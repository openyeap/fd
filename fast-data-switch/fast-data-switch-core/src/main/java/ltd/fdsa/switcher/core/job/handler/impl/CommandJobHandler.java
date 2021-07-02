package ltd.fdsa.switcher.core.job.handler.impl;

import ltd.fdsa.switcher.core.job.handler.JobHandler;
import ltd.fdsa.switcher.core.job.log.JobLogger;
import ltd.fdsa.web.view.Result;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class CommandJobHandler implements JobHandler {
    @Override
    public Result<Object> execute(Map<String, String> context) {
        String command = context.get("cmd");
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
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (exitValue == 0) {
            return SUCCESS;
        } else {

            return Result.fail(FAIL.getCode(), "command exit value(" + exitValue + ") is failed");
        }
    }
}
