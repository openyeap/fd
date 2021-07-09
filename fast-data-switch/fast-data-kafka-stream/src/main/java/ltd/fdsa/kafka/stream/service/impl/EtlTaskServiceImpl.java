package ltd.fdsa.kafka.stream.service.impl;


import com.alibaba.fastjson.JSONObject;
import ltd.fdsa.database.jpa.service.BaseJpaService;
import ltd.fdsa.kafka.stream.properties.KafkaProperties;
import ltd.fdsa.kafka.stream.entity.EtlTask;
import ltd.fdsa.kafka.stream.repository.reader.EtlTaskReader;
import ltd.fdsa.kafka.stream.repository.writer.EtlTaskWriter;
import ltd.fdsa.kafka.stream.view.EtlTaskReq;
import ltd.fdsa.kafka.stream.service.IEtlTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtlTaskServiceImpl extends BaseJpaService<EtlTask, Integer, EtlTaskWriter, EtlTaskReader> implements IEtlTaskService {

    @Autowired
    private KafkaProperties kafkaProperties;


    public void addEtlTask(EtlTask etlTask) {
//
//        //定义贴源表前缀
//        String backupTableName = "";
//
//        //任务保存
//        etlTask.setSourceConnectorName(etlTask.getId() + "_source");
//        etlTask.setSinkConnectorName(etlTask.getId() + "_sink");
//        etlTask.setTargetSourceConnectorName(etlTask.getId() + "_target_source");
//        etlTask.setTargetSinkConnectorName(etlTask.getId() + "_target_sink");
//        this.writer.save(etlTask);
//
//        //预定数据处理需要用到，输入topic
//        String inputTopicPrefix = etlTask.getId() + "_input_";
//        String outputTopicPrefix = etlTask.getId() + "_out_";
//
//
//        String url = "http://" + kafkaProperties.getConnectUrl() + "/connectors";
//
//        //----------------1、接入源 至 贴源 配置 begin----------------
//
//        //向kafka connect 请求配对 source
//        JSONObject source = JSONUtil.parseObj(etlTask.getSourceConnectorConfig());
//        JSONObject sourceConfig = new JSONObject();
//        source.putOpt("topic.prefix", inputTopicPrefix);
//        sourceConfig.putOpt("name", etlTask.getSourceConnectorName());
//        sourceConfig.putOpt("config", source);
//
//        JSONObject res = requestPost(url, JSONUtil.toJsonStr(sourceConfig));
//        //从配置中获取 inputTopic
//        String inputTopic = inputTopicPrefix + res.get("config", JSONObject.class).getStr("table.whitelist");
//        backupTableName = inputTopic;
//
//        //向kafka connect 请求配对 sink
//        JSONObject sink = JSONUtil.parseObj(etlTask.getSinkConnectorConfig());
//        JSONObject sinkConfig = new JSONObject();
//        sink.putOpt("topics", inputTopic);
//
//        sinkConfig.putOpt("name", etlTask.getSinkConnectorName());
//        sinkConfig.putOpt("config", sink);
//        JSONObject sinkObject = requestPost(url, JSONUtil.toJsonStr(sinkConfig));
//
//        //----------------1、接入源 至 贴源 配置  end ----------------
//
//        //-----------------2、是否需要数据 处理 begin---------------------
//        if (etlTask.getIsDataHandling()) {
////            outputTopic = "outputTopic"+ etlTask.getId();
//        }
//        //-----------------2、是否需要数据 处理 end ---------------------
//
//        //----------------3、贴源 至 落地 配置 begin ------------------
//        //向kafka connect 请求配对 source
//        String outputTopic = "";
//        if (!etlTask.getIsDataHandling()) {
//            JSONObject targetSource = JSONUtil.parseObj(etlTask.getTargetSourceConnectorConfig());
//            JSONObject targetSourceConfig = new JSONObject();
//            targetSource.putOpt("topic.prefix", outputTopicPrefix);
//            targetSource.putOpt("table.whitelist", backupTableName);
//            targetSourceConfig.putOpt("name", etlTask.getTargetSourceConnectorName());
//            targetSourceConfig.putOpt("config", targetSource);
//
//
//            res = requestPost(url, JSONUtil.toJsonStr(targetSourceConfig));
//            outputTopic = outputTopicPrefix + inputTopic;
//        }
//
//        //向kafka connect 请求配对 sink
//        JSONObject targetSink = JSONUtil.parseObj(etlTask.getTargetSinkConnectorConfig());
//        JSONObject taregetSinkConfig = new JSONObject();
//        if (etlTask.getIsDataHandling()) {
//            targetSink.putOpt("topics", outputTopic);
//            // TODO: 2021/4/8 实现kafka stream 实例调用
//        } else {
//            targetSink.putOpt("topics", outputTopic);
//
//        }
//        taregetSinkConfig.putOpt("name", etlTask.getTargetSinkConnectorName());
//        taregetSinkConfig.putOpt("config", targetSink);
//        res = requestPost(url, JSONUtil.toJsonStr(taregetSinkConfig));
//
//        //----------------3、贴源 至 落地 配置 end ------------------

    }

    public boolean editEtlTask(EtlTask etlTask) {
//        EtlTask old = getEtlTaskInfo(etlTask.getId());
//        if (ObjectUtil.isNotNull(old)) {
//            old.setIsDataHandling(etlTask.getIsDataHandling());
//            old.setSinkConnectorConfig(etlTask.getSinkConnectorConfig());
//            old.setSourceConnectorConfig(etlTask.getSourceConnectorConfig());
//            old.setTaskName(etlTask.getTaskName());
//
//            this.writer.save(old);
//
//            String url = "http://%s/connectors/%s/config";
//            //向kafka connect 请求配对 source
//            requestPut(String.format(url, kafkaProperties.getConnectUrl(), old.getSourceConnectorName()), etlTask.getSourceConnectorConfig());
//            //向kafka connect 请求配对 sink
//            requestPut(String.format(url, kafkaProperties.getConnectUrl(), old.getSinkConnectorName()), etlTask.getSinkConnectorConfig());
//            return true;
//        }
        return false;
    }

    public EtlTask getEtlTaskInfo(Integer id) {
        return this.reader.findById(id).get();
    }

    public boolean deleteEtlTask(Integer id) {
        EtlTask etlTask = getEtlTaskInfo(id);

//        if (ObjectUtil.isNull(etlTask)) {
//            return false;
//        }
        // delete source connector
        String url = "http://%s/connectors/%s";
        String deleteSourceUrl = String.format(url, kafkaProperties.getConnectUrl(), etlTask.getSourceConnectorName());
        requestDelete(deleteSourceUrl);

        // TODO: 2021/4/9
        //delete sink connector
        String deleteSinkUrl = String.format(url, kafkaProperties.getConnectUrl(), etlTask.getSinkConnectorName());
        requestDelete(deleteSinkUrl);


        // delete source connector

        deleteSourceUrl = String.format(url, kafkaProperties.getConnectUrl(), etlTask.getTargetSourceConnectorName());
        requestDelete(deleteSourceUrl);

        //delete sink connector
        deleteSinkUrl = String.format(url, kafkaProperties.getConnectUrl(), etlTask.getTargetSinkConnectorConfig());
        requestDelete(deleteSinkUrl);
        this.writer.deleteById(Integer.valueOf(id));
        return true;
    }

    @Override
    public List<EtlTask> pageList(EtlTaskReq etlTaskReq) {
        Example example = Example.of(etlTaskReq);
        return this.reader.findAll(example);
    }


    /**
     * resful post 请求
     *
     * @param url
     * @param json
     * @return
     */
    private JSONObject requestPost(String url, String json) {
//        String res = HttpRequest.post(url)
//                .body(json)
//                .execute().body();
//        return JSONUtil.parseObj(res);
        return null;
    }

    /**
     * resful post 请求
     *
     * @param url
     * @param json
     * @return
     */
    private boolean requestPut(String url, String json) {
//        String res = HttpRequest.put(url)
//                .body(json)
//                .execute().body();
//        System.out.print(res);
//        JSONObject jsonObject = JSONUtil.parseObj(res);
//        if (jsonObject.containsKey("error_code")) {
//            return false;
//        }
        return true;
    }

    /**
     * get 请求
     *
     * @param url
     * @return
     */
    private boolean requestDelete(String url) {

//        String res = HttpRequest.delete(url).contentType("application/json").execute().body();
//        if (ObjectUtil.isNotEmpty(res)) {
//            JSONObject jsonObject = JSONUtil.parseObj(res);
//            if (jsonObject.containsKey("error_code")) {
//                return false;
//            } else {
//                return true;
//            }
//        } else {
        return false;
//        }
    }
}
