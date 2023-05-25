package cn.zhumingwu.dataswitch.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.googlecode.aviator.Expression;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.dataswitch.core.container.Plugin;
import cn.zhumingwu.dataswitch.core.model.Column;
import cn.zhumingwu.dataswitch.core.model.Record;
import cn.zhumingwu.dataswitch.core.pipeline.Process;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


@Slf4j
@ToString
@Plugin(name = "表达式", description = "通过表达式增加数据")
public class JsonPipeline implements Process {
    private static final Map<String, Expression> cache = new HashMap<String, Expression>();
    private static final String FIELDS_KEY = "fields";
    private String[] fields;

    @Override
    public void init() {
        this.fields = this.context().get(FIELDS_KEY).split(",");
    }

    @Override
    public void execute(Record... records) {
        // 判断是否在运行
        if (!this.isRunning()) {
            return;
        }
        // 解析json内容并将结果放回数据记录
        for (var record : records) {
            for (var item : this.fields) {
                var value = record.toNormalMap().get(item).toString();
                var result = this.parserValue(value);
                for (var entry : result.entrySet()) {
                    record.add(new Column(entry.getKey(), entry.getValue()));
                }
            }
        }
        // 下沉数据
        for (var item : this.nextSteps()) {
            item.execute(records);
        }
    }

    Map<String, String> parserValue(String content) {
        try {
            ObjectMapper jm = new ObjectMapper();
            var jsonNode = jm.readTree(content);
            JavaPropsMapper pm = new JavaPropsMapper();
            return pm.writeValueAsMap(jsonNode);

        } catch (Exception e) {
            return new TreeMap<String, String>();
        }
    }
}