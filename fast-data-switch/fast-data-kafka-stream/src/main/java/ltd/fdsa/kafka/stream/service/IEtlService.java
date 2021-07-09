package ltd.fdsa.kafka.stream.service;

import ltd.fdsa.kafka.stream.view.EtlReq;

public interface IEtlService {

    /**
     * 新增流程
     * @param etlReq
     */
    void addEtl(EtlReq etlReq);
}
