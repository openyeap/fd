package ltd.fdsa.switcher.core.job.log;

import ltd.fdsa.core.util.NamingUtils;
import org.assertj.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class JobLogger {
    private static Logger logger = LoggerFactory.getLogger("logger");

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
    private static void logDetail(StackTraceElement callInfo, String appendLog) {
        String formatAppendLog = NamingUtils.format(LogFormatPattern,
                DateUtil.formatAsDatetime(new Date()),
                callInfo.getClassName(),
                callInfo.getLineNumber(),
                Thread.currentThread().getName(), appendLog);
        // append log
        String logFileName = JobFileAppender.contextHolder.get();
        if (logFileName != null && logFileName.trim().length() > 0) {
            JobFileAppender.appendLog(logFileName, formatAppendLog);
        } else {
            logger.info(">>>>>>>>>>> {}", formatAppendLog);
        }
    }

    /**
     * append log with pattern
     *
     * @param appendLogPattern   like "aaa {} bbb {} ccc"
     * @param appendLogArguments like "111, true"
     */
    public static void log(String appendLogPattern, Object... appendLogArguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(appendLogPattern, appendLogArguments);
        String appendLog = ft.getMessage();
        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        logDetail(callInfo, appendLog);
    }

    /**
     * append exception stack
     *
     * @param e
     */
    public static void log(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String appendLog = stringWriter.toString();
        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        logDetail(callInfo, appendLog);
    }
}
