package ltd.fdsa.starter.monitor.converter;

import com.alibaba.druid.pool.DruidDataSourceStatValue;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class DruidStatConverter {
    private final DruidDataSourceStatValue statValue;

    @Override
    public String toString() {

        if (this.statValue == null) {
            return "";
        }
        try {
            var url = statValue.getUrl();
            var dbType = statValue.getDbType();
            var name = statValue.getName();
            StringBuilder map = new StringBuilder();


            var id = new Id.Builder()
                    .name("druid_active_current")
                    .value(Double.valueOf(statValue.getActiveCount()))
                    .type(Type.GAUGE)
                    .description("The \"recent active connect\" for the whole system")
                    .tags("url", url).tags("db", dbType).tags("name", name)
                    .build();
            map.append(id.toString());
            if (statValue.getActivePeak() > 0) {
                map.append(new Id.Builder()
                        .name("druid_active_peak")
                        .value(statValue.getActivePeak())
                        .type(Type.GAUGE)
                        .description("")
                        .tags("url", url).tags("db", dbType).tags("name", name)
                        .build().toString());
//                map.append(new Id.Builder()
//                        .name("druid_active_peak_time")
//                        .value(statValue.getActivePeakTime().getTime)
//                        .type(Type.GAUGE)
//                        .description("")
//                        .build().toString());
            }
//            map.put("poolingCount", statValue.getPoolingCount());
//            if (statValue.getPoolingPeak() > 0) {
//                map.put("poolingPeak", statValue.getPoolingPeak());
//                map.put("poolingPeakTime", statValue.getPoolingPeakTime());
//            }
//            map.put("connectCount", statValue.getConnectCount());
//            map.put("closeCount", statValue.getCloseCount());
//
//            if (statValue.getWaitThreadCount() > 0) {
//                map.put("waitThreadCount", statValue.getWaitThreadCount());
//            }
//
//            if (statValue.getNotEmptyWaitCount() > 0) {
//                map.put("notEmptyWaitCount", statValue.getNotEmptyWaitCount());
//            }
//
//            if (statValue.getNotEmptyWaitMillis() > 0) {
//                map.put("notEmptyWaitMillis", statValue.getNotEmptyWaitMillis());
//            }
//
//            if (statValue.getLogicConnectErrorCount() > 0) {
//                map.put("logicConnectErrorCount", statValue.getLogicConnectErrorCount());
//            }
//
//            if (statValue.getPhysicalConnectCount() > 0) {
//                map.put("physicalConnectCount", statValue.getPhysicalConnectCount());
//            }
//
//            if (statValue.getPhysicalCloseCount() > 0) {
//                map.put("physicalCloseCount", statValue.getPhysicalCloseCount());
//            }
//
//            if (statValue.getPhysicalConnectErrorCount() > 0) {
//                map.put("physicalConnectErrorCount", statValue.getPhysicalConnectErrorCount());
//            }
//
//            if (statValue.getExecuteCount() > 0) {
//                map.put("executeCount", statValue.getExecuteCount());
//            }
//
//            if (statValue.getErrorCount() > 0) {
//                map.put("errorCount", statValue.getErrorCount());
//            }
//
//            if (statValue.getCommitCount() > 0) {
//                map.put("commitCount", statValue.getCommitCount());
//            }
//
//            if (statValue.getRollbackCount() > 0) {
//                map.put("rollbackCount", statValue.getRollbackCount());
//            }
//
//            if (statValue.getPstmtCacheHitCount() > 0) {
//                map.put("pstmtCacheHitCount", statValue.getPstmtCacheHitCount());
//            }
//
//            if (statValue.getPstmtCacheMissCount() > 0) {
//                map.put("pstmtCacheMissCount", statValue.getPstmtCacheMissCount());
//            }
//
//            if (statValue.getStartTransactionCount() > 0) {
//                map.put("startTransactionCount", statValue.getStartTransactionCount());
//                map.put("transactionHistogram", rtrim(statValue.getTransactionHistogram()));
//            }
//
//            if (statValue.getConnectCount() > 0) {
//                map.put("connectionHoldTimeHistogram", rtrim(statValue.getConnectionHoldTimeHistogram()));
//            }
//
//            if (statValue.getClobOpenCount() > 0) {
//                map.put("clobOpenCount", statValue.getClobOpenCount());
//            }
//
//            if (statValue.getBlobOpenCount() > 0) {
//                map.put("blobOpenCount", statValue.getBlobOpenCount());
//            }
//
//            if (statValue.getSqlSkipCount() > 0) {
//                map.put("sqlSkipCount", statValue.getSqlSkipCount());
//            }

            ArrayList<Map<String, Object>> sqlList = new ArrayList<Map<String, Object>>();

//            if (statValue.getSqlList()!=null &statValue.getSqlList().size() > 0) {
//                for (JdbcSqlStatValue sqlStat : statValue.getSqlList()) {
//                    Map<String, Object> sqlStatMap = new LinkedHashMap<String, Object>();
//                    sqlStatMap.put("sql", sqlStat.getSql());
//
//                    if (sqlStat.getExecuteCount() > 0) {
//                        sqlStatMap.put("executeCount", sqlStat.getExecuteCount());
//                        sqlStatMap.put("executeMillisMax", sqlStat.getExecuteMillisMax());
//                        sqlStatMap.put("executeMillisTotal", sqlStat.getExecuteMillisTotal());
//
//                        sqlStatMap.put("executeHistogram", rtrim(sqlStat.getExecuteHistogram()));
//                        sqlStatMap.put("executeAndResultHoldHistogram", rtrim(sqlStat.getExecuteAndResultHoldHistogram()));
//                    }
//
//                    long executeErrorCount = sqlStat.getExecuteErrorCount();
//                    if (executeErrorCount > 0) {
//                        sqlStatMap.put("executeErrorCount", executeErrorCount);
//                    }
//
//                    int runningCount = sqlStat.getRunningCount();
//                    if (runningCount > 0) {
//                        sqlStatMap.put("runningCount", runningCount);
//                    }
//
//                    int concurrentMax = sqlStat.getConcurrentMax();
//                    if (concurrentMax > 0) {
//                        sqlStatMap.put("concurrentMax", concurrentMax);
//                    }
//
//                    if (sqlStat.getFetchRowCount() > 0) {
//                        sqlStatMap.put("fetchRowCount", sqlStat.getFetchRowCount());
//                        sqlStatMap.put("fetchRowCount", sqlStat.getFetchRowCountMax());
//                        sqlStatMap.put("fetchRowHistogram", rtrim(sqlStat.getFetchRowHistogram()));
//                    }
//
//                    if (sqlStat.getUpdateCount() > 0) {
//                        sqlStatMap.put("updateCount", sqlStat.getUpdateCount());
//                        sqlStatMap.put("updateCountMax", sqlStat.getUpdateCountMax());
//                        sqlStatMap.put("updateHistogram", rtrim(sqlStat.getUpdateHistogram()));
//                    }
//
//                    if (sqlStat.getInTransactionCount() > 0) {
//                        sqlStatMap.put("inTransactionCount", sqlStat.getInTransactionCount());
//                    }
//
//                    if (sqlStat.getClobOpenCount() > 0) {
//                        sqlStatMap.put("clobOpenCount", sqlStat.getClobOpenCount());
//                    }
//
//                    if (sqlStat.getBlobOpenCount() > 0) {
//                        sqlStatMap.put("blobOpenCount", sqlStat.getBlobOpenCount());
//                    }
//
//                    sqlList.add(sqlStatMap);
//                }
//
//                map.put("sqlList", sqlList);
//            }

//            if (statValue.getKeepAliveCheckCount() > 0) {
//                map.put("keepAliveCheckCount", statValue.getKeepAliveCheckCount());
//            }

            return map.toString();
        } catch (Exception ex) {
            log.error("writeInternal:ex", ex);
            return "";
        }
    }


}
