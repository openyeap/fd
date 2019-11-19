package ltd.fdsa.fds.core.config;
//package com.daoshu.fds.core.config;
//
//public class WriterConfig extends Configuration {
//
//	private static final String FLOWLIMIT_KEY = "limit";
//	private static final long DEFAULT_FLOWLIMIT = 0;
//
//	private static final long serialVersionUID =0L;
//
//	public WriterConfig() {
//		super();
//	}
//
//	public long getFlowLimit() {
//		long flowLimit = getLong(FLOWLIMIT_KEY, DEFAULT_FLOWLIMIT);
//		if (flowLimit < 0) {
//			throw new IllegalArgumentException("Reader and Writer FLowLimit must be >= 0.");
//		}
//		return flowLimit;
//	}
//}
