package cn.zhumingwu.dataswitch.core.job.handler.impl;

import cn.zhumingwu.dataswitch.core.job.executor.JobContext;
import cn.zhumingwu.dataswitch.core.job.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
@Slf4j
public class CommandJobHandler extends IJobHandler {

    @Override
    public void execute() throws Exception {
        JobContext.getJobContext().getJobParam().forEach(
                command -> {
                    BufferedReader bufferedReader = null;
                    try {
                        // command process
                        java.lang.Process process = Runtime.getRuntime().exec(command);
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
                        bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

                        // command log
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            JobContext.getJobContext().log(line);
                        }

                        // command exit
                        process.waitFor();
                    } catch (Exception e) {
                        JobContext.getJobContext().log(e);
                    } finally {
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e) {
                                log.error("error", e);
                            }
                        }
                    }
                });
    }
}