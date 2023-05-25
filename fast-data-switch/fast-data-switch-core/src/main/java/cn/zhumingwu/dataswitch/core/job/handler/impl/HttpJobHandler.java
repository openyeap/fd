//package cn.zhumingwu.dataswitch.core.job.handler.impl;
//
//import cn.zhumingwu.dataswitch.core.job.executor.JobLogger;
//import cn.zhumingwu.dataswitch.core.model.Record;
//import cn.zhumingwu.dataswitch.core.pipeline.Process;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Arrays;
//
//public class HttpJobHandler implements Process {
//
//
//    @Override
//    public void execute(Record... records) {
//        Arrays.stream(records).map(record -> record.toNormalMap().get("cmd").toString()).forEach(
//                url -> {
//                    // request
//                    HttpURLConnection connection = null;
//                    BufferedReader bufferedReader = null;
//                    try {
//                        // connection
//                        URL realUrl = new URL(url);
//                        connection = (HttpURLConnection) realUrl.openConnection();
//
//                        // connection setting
//                        connection.setRequestMethod("GET");
//                        connection.setDoOutput(true);
//                        connection.setDoInput(true);
//                        connection.setUseCaches(false);
//                        connection.setReadTimeout(5 * 1000);
//                        connection.setConnectTimeout(3 * 1000);
//                        connection.setRequestProperty("connection", "Keep-Alive");
//                        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                        connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8");
//
//                        // do connection
//                        connection.connect();
//
//
//                        // valid StatusCode
//                        int statusCode = connection.getResponseCode();
//                        if (statusCode != 200) {
//                            throw new RuntimeException("Http Request StatusCode(" + statusCode + ") Invalid.");
//                        }
//
//                        // result
//                        bufferedReader =
//                                new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
//                        StringBuilder result = new StringBuilder();
//                        String line;
//                        while ((line = bufferedReader.readLine()) != null) {
//                            result.append(line);
//                        }
//                        String responseMsg = result.toString();
//
//                        JobLogger.log(responseMsg);
//
//                    } catch (Exception e) {
//                        JobLogger.log(e);
//
//                    } finally {
//                        try {
//                            if (bufferedReader != null) {
//                                bufferedReader.close();
//                            }
//                            if (connection != null) {
//                                connection.disconnect();
//                            }
//                        } catch (Exception e2) {
//                            JobLogger.log(e2);
//                        }
//                    }
//                });
//    }
//}