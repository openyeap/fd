package ltd.fdsa.kafka.stream.service;

import ltd.fdsa.database.service.DataAccessService;
import ltd.fdsa.kafka.stream.entity.EtlTask;
import ltd.fdsa.kafka.stream.view.EtlTaskReq;
import java.util.List;
public interface IEtlTaskService extends DataAccessService<EtlTask,Integer> {

    /**
     * 分页
     * @param etlTaskReq
     * @return
     */
    List<EtlTask> pageList(EtlTaskReq etlTaskReq);


}
