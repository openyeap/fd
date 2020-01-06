package ltd.fdsa.job.executor.jobhandler;

import ltd.fdsa.job.core.biz.model.ReturnT;
import ltd.fdsa.job.core.handler.IJobHandler;
import ltd.fdsa.job.core.log.JobLogger;
import ltd.fdsa.job.core.util.ShardingUtil;


public class ShardingJobHandler extends IJobHandler {

	@Override
	public ReturnT<String> execute(String param) throws Exception {

		// 分片参数
		ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
		JobLogger.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardingVO.getIndex(), shardingVO.getTotal());

		// 业务逻辑
		for (int i = 0; i < shardingVO.getTotal(); i++) {
			if (i == shardingVO.getIndex()) {
				JobLogger.log("第 {} 片, 命中分片开始处理", i);
			} else {
				JobLogger.log("第 {} 片, 忽略", i);
			}
		}

		return SUCCESS;
	}
	
}
