package ltd.fdsa.switcher.plugin;

import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.switcher.core.PluginType;
import ltd.fdsa.switcher.core.pipeline.Channel;
import ltd.fdsa.switcher.core.config.Configuration;
import ltd.fdsa.switcher.core.pipeline.impl.AbstractPipeline;
import ltd.fdsa.web.view.Result;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.util.Arrays;
import java.util.Map;

/*
使用Kafka实现数据渠道功能
*/
@Slf4j
public class KafkaChannel extends AbstractPipeline implements Channel, Callback<String, Boolean> {
    KafkaClient kafkaClient;
    HttpClient httpClient = new HttpClient(null);

    @Override
    public Result<String> init(Configuration configuration) {
        this.config = configuration;
        this.name = this.config.getString("name", this.getClass().getCanonicalName());
        this.type = PluginType.valueOf(this.config.getString("type", "pipeline"));
        this.description = this.config.getString("description", this.getClass().getCanonicalName());
        String[] hosts = Arrays.stream(this.config.getString("hosts").split(",")).map(m -> m.trim()).toArray(String[]::new);
        this.kafkaClient = new KafkaClient(this.config.getString("topic"), hosts);
        // Send Job Config to Cluster
        var configurations = this.config.getConfigurations("pipelines");
        if (configurations != null && configurations.length > 0) {
            // invoke rpc to init
            httpClient.post("/api/job/init", RequestBody.create(MediaType.get("application/json"), this.config.toString()));
        }
        // Cluster will return related job Token
        // create clients to manager cluster nodes
        // then we can remote process call to collect data;
        return Result.success();
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            // monitor cluster nodes
            kafkaClient.start(this::call);
        }
    }


    @Override
    public void collect(Map<String, Object>... records) {
        if (!this.isRunning()) {
            return;
        }
        httpClient.post("/api/job/collect", RequestBody.create(MediaType.get("app"), ""));
    }

    @Override
    public Boolean call(String param) {
        return false;
    }
}
