//package cn.zhumingwu.dataswitch.core.job.handler.impl;
//
//import cn.zhumingwu.dataswitch.core.job.executor.JobLogger;
//import cn.zhumingwu.dataswitch.core.model.Record;
//import cn.zhumingwu.dataswitch.core.pipeline.Process;
//import cn.zhumingwu.dataswitch.core.util.ShardingUtil;
//
//public class ShardingJobHandler implements Process {
//    @Override
//    public void execute(Record... records) {
//        // 分片参数
//        ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
//        JobLogger.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardingVO.getIndex(), shardingVO.getTotal());
//
//        // 业务逻辑
//        for (int i = 0; i < shardingVO.getTotal(); i++) {
//            if (i == shardingVO.getIndex()) {
//                JobLogger.log("第 {} 片, 命中分片开始处理", i);
//            } else {
//                JobLogger.log("第 {} 片, 忽略", i);
//            }
//        }
//    }
//}
