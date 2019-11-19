package com.daoshu.logging.util;
//package com.daoshu.logback.util;
//
//import java.util.concurrent.ThreadLocalRandom;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class LogUtil {
//	    
//    /**
//	 * 生成日志随机数
//	 * 
//	 * @return
//	 */
//	public static String getTraceId() {
//		int i = 0;
//		StringBuilder st = new StringBuilder();
//		while (i < 5) {
//			i++;
//			st.append(ThreadLocalRandom.current().nextInt(10));
//		}
//		return st.toString() + System.currentTimeMillis();
//	}
//	 
//}
