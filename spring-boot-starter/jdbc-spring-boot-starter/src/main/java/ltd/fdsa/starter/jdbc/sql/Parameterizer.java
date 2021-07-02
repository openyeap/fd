package ltd.fdsa.starter.jdbc.sql;

import java.util.ArrayList;
import java.util.Collection;


public class Parameterizer {
//    public JoinSql parameterizeSql(String sql) {
//        StringBuilder sb = new StringBuilder();
//
//        Collection<String> parameterNames = new ArrayList<>();
//
//        int stoppingPoint = 0;
//
//        for (int i = sql.indexOf("@"); i >= 0; i = sql.indexOf("@", i + 1)) {
//            // We first get the parameter name
//            int end = i + 1;
//
//            while (end < (sql.length()) && sql.charAt(end) != ' ' && sql.charAt(end) != ',' && sql.charAt(end) != ')') {
//                end++;
//            }
//
//            String parameterName = sql.substring(i + 1, end);
//
//            parameterNames.add(parameterName);
//
//            // Next we need to get the SQL
//            sb.append(sql, stoppingPoint, i);
//
//            sb.append("?");
//
//            stoppingPoint = end;
//        }
//
//        if (stoppingPoint < sql.length()) {
//            sb.append(sql.substring(stoppingPoint));
//        }
//
//        return new JoinSql(sb.toString(), parameterNames);
//    }
}
