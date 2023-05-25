package cn.zhumingwu.dataswitch.core.util;

import cn.zhumingwu.dataswitch.core.job.model.LogResult;
import cn.zhumingwu.dataswitch.core.util.DateUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * store trigger log in each log-file
 */
@Slf4j
public class JobFileAppender {

    private static final String logBasePath = "/logs/job/";

    // {BasePath}/{Handler}/{JobID}/{yyyy-MM-dd}/{TaskID}.log
    public static String getLogFile(String handler, Long jobId, Long taskId, long timestamp) {
        // init
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // avoid concurrent problem, can not be static
        var day = sdf.format(new Date(timestamp));
        var logPath = MessageFormat.format("{}/{}/{}/{}/{}.log", logBasePath, handler, jobId, day, taskId);
        File logFilePath = new File(logPath);
        return logFilePath.getPath();
    }


    public static boolean initLogDir(String logPath) {
        File logPathDir = new File(logPath).getParentFile();
        if (!logPathDir.exists()) {
            return logPathDir.mkdirs();
        }
        return true;
    }


    /**
     * [LogTime]-[ClassName]-[MethodName]-[LineNumber]-[ThreadName] msg";
     */
    private static String LogFormatPattern = "[{}]-[{}]-[{}]-[{}] {}";

    /**
     * append log
     *
     * @param callInfo
     * @param appendLog
     */
    public static boolean logDetail(String logFileName, StackTraceElement callInfo, String appendLog) {

        String formatAppendLog = MessageFormat.format(LogFormatPattern,
                DateUtil.formatDateTime(new Date()),
                callInfo.getClassName(),
                callInfo.getLineNumber(),
                Thread.currentThread().getName(), appendLog);

        // log file
        if (Strings.isNullOrEmpty(logFileName)) {
            return false;
        }
        File logFile = new File(logFileName);

        if (!logFile.exists()) {
            try {
                if (!logFile.createNewFile()) {
                    return false;
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return false;
            }
        }


        // append file content
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logFile, true);
            fos.write(formatAppendLog.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return true;

    }

    /**
     * support read log-file
     *
     * @param logFileName
     * @return log content
     */
    public static LogResult readLog(String logFileName, int fromLineNum) {

        // valid log file
        if (logFileName == null || logFileName.trim().length() == 0) {
            return new LogResult(fromLineNum, 0, "readLog fail, logFile not found", true);
        }
        File logFile = new File(logFileName);

        if (!logFile.exists()) {
            return new LogResult(fromLineNum, 0, "readLog fail, logFile not exists", true);
        }

        // read file
        StringBuffer logContentBuffer = new StringBuffer();
        int toLineNum = 0;
        LineNumberReader reader = null;
        try {
            // reader = new LineNumberReader(new FileReader(logFile));
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(logFile), StandardCharsets.UTF_8));
            String line = null;

            while ((line = reader.readLine()) != null) {
                toLineNum = reader.getLineNumber(); // [from, to], start as 1
                if (toLineNum >= fromLineNum) {
                    logContentBuffer.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        // result
        LogResult logResult = new LogResult(fromLineNum, toLineNum, logContentBuffer.toString(), false);
        return logResult;

    /*
    // it will return the number of characters actually skipped
    reader.skip(Long.MAX_VALUE);
    int maxLineNum = reader.getLineNumber();
    maxLineNum++;	// 最大行号
    */
    }

}
