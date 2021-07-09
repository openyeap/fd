package ltd.fdsa.kafka.stream.service.impl;


import ltd.fdsa.kafka.stream.properties.KafkaProperties;
import ltd.fdsa.kafka.stream.service.IEtlService;
import ltd.fdsa.kafka.stream.view.EtlReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EtlServiceImpl implements IEtlService {

    @Autowired
    private KafkaProperties kafkaProperties;

    public void addEtl(EtlReq etlReq) {

//        // TODO: 2021/4/6  source
//        String url = "http://"+ kafkaProperties.getConnectUrl()+"/connectors";
//
//        String res = HttpRequest.post(url)
//                .body(etlReq.getSourceConnectorConfig())
//                .execute().body();
//        System.out.print(res);
//
//        // TODO: 2021/4/6  sink
//        res = HttpRequest.post(url)
//                .body(etlReq.getSinkConnectorConfig())
//                .execute().body();
//
//        System.out.print(res);
    }
}
