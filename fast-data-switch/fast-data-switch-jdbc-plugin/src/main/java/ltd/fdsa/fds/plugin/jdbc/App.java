package ltd.fdsa.fds.plugin.jdbc;
//package com.daoshu.fds.plugin.jdbc;
//
//import java.util.*;
//
//import com.alibaba.fastjson.JSON;
//import com.daoshu.fds.plugin.jdbc.implement.ExpressionPipeLine;
//import com.daoshu.fds.plugin.jdbc.implement.JDBCSource;
//import com.daoshu.fds.plugin.jdbc.implement.JDBCTarget;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class App {
//
//	public static void main(String[] args) {
//
//		JDBCSource source = new JDBCSource();
//		List<Map<String, Object>> input = source.read();
//		JDBCTarget target = new JDBCTarget();
//		target.write(input);
//		
//		ExpressionPipeLine pipeLine = new ExpressionPipeLine();
//		pipeLine.prepare("Name=data.firstName +' '+ data.lastName,Age=data.age");
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("firstName", "mike");
//		data.put("lastName", "json");
//		data.put("age", 18);
//		Map<String, Object> result = pipeLine.process(data);
//		log.debug(JSON.toJSONString(result));
//
//	}
//}
