//package cn.zhumingwu.dataswitch.core.job.handler.impl;
//
//import cn.zhumingwu.dataswitch.core.job.executor.JobLogger;
//import cn.zhumingwu.dataswitch.core.model.Record;
//import cn.zhumingwu.dataswitch.core.pipeline.Process;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.Arrays;
//
//public class CommandJobHandler implements Process {
//
//    @Override
//    public void execute(Record... records) {
//        Arrays.stream(records).map(record -> record.toNormalMap().get("cmd").toString()).forEach(
//                command -> {
//                    BufferedReader bufferedReader = null;
//                    try {
//                        // command process
//                        java.lang.Process process = Runtime.getRuntime().exec(command);
//                        BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
//                        bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));
//
//                        // command log
//                        String line;
//                        while ((line = bufferedReader.readLine()) != null) {
//                            JobLogger.log(line);
//                        }
//
//                        // command exit
//                        process.waitFor();
//                    } catch (Exception e) {
//                        JobLogger.log(e);
//                    } finally {
//                        if (bufferedReader != null) {
//                            try {
//                                bufferedReader.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//        );
//    }
//}
