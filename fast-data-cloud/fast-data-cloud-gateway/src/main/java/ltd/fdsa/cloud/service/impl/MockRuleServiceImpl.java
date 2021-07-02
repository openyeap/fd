package ltd.fdsa.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.constant.Constant;
import ltd.fdsa.cloud.constant.MockHandleTypeEnum;
import ltd.fdsa.cloud.model.MockRule;
import ltd.fdsa.cloud.model.MockRuleConsul;
import ltd.fdsa.cloud.service.IMockRuleService;
import ltd.fdsa.cloud.util.DateTimeUtil;
import ltd.fdsa.core.util.NamingUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Slf4j
public class MockRuleServiceImpl implements IMockRuleService {


    public static Map<String, MockRule> mockRuleMap = new ConcurrentHashMap<String, MockRule>();
    private static String updateTime = "";
    @Autowired
    private ConsulClient consulClient;

    @Autowired
    private RestTemplate restTemplate;

    public List<MockRule> getMockRuleList(String requestPathKeyword) throws Exception {

        if (StringUtils.isBlank(requestPathKeyword)) {

            return new ArrayList<MockRule>(mockRuleMap.values());

        }

        List<MockRule> list = new ArrayList<MockRule>();

        for (Map.Entry<? extends String, ? extends MockRule> e : mockRuleMap.entrySet()) {

            if (e.getKey().contains(requestPathKeyword)) {

                list.add(e.getValue());
            }
        }

        return list;
    }


    @PostConstruct
    private void init() {

        Runnable run = new Runnable() {
            @Override
            public void run() {
                while (true) {

                    Response<GetValue> kvValue = consulClient.getKVValue(Constant.MOCK_RULE_KEY_CONSUL);

                    try {

                        if (null == kvValue.getValue()) {
                            Thread.sleep(5000L);
                            continue;
                        }

                        String value = kvValue.getValue().getValue();

                        String mockRuleConfig = new String(Base64Utils.decodeFromString(value), "UTF-8");

                        MockRuleConsul mockRuleConsul = JSONObject.parseObject(mockRuleConfig, MockRuleConsul.class);
                        if (!StringUtils.equals(updateTime, mockRuleConsul.getUpdateTime())) {
                            updateTime = mockRuleConsul.getUpdateTime();
                            listToMap(mockRuleConsul.getData());
                            NamingUtils.formatLog(log,"拉取 mock 规则数据成功。");
                        }

                        Thread.sleep(8000L);//8s加载一次;

                    } catch (Exception e) {
                        log.error("mock 规则信息初始化 异常，请检查。", e);
                        break;
                    }

                }

            }
        };
        new Thread(run).start();

    }

    private void listToMap(List<MockRule> data) {

        for (MockRule mockRule : data) {

            mockRuleMap.put(mockRule.getRequestPath(), mockRule);

        }

        int size = data.size();
        int mapSize = mockRuleMap.keySet().size();
        //检测服务器是否有删除配置项，本地缓存同步删除。
        if (size != mapSize) {

            Map<String, MockRule> map = new HashMap<String, MockRule>();
            for (MockRule mockRule : data) {
                map.put(mockRule.getRequestPath(), mockRule);
            }

            for (Map.Entry<? extends String, ? extends MockRule> e : mockRuleMap.entrySet()) {

                if (!map.containsKey(e.getKey())) {
                    mockRuleMap.remove(e.getKey());
                }

            }
            NamingUtils.formatLog(log,"mock规则同步删除本地缓存数据成功。");
        }

    }


    @Override
    public Boolean addMockRule(MockRule mockRule) throws Exception {

        mockRule.setCreateTime(DateTimeUtil.getCurrentDateTimeStr());
        mockRule.setUpdateTime(DateTimeUtil.getCurrentDateTimeStr());

        List<MockRule> mockValues = new ArrayList<MockRule>(mockRuleMap.values());

        mockValues.add(mockRule);

        MockRuleConsul rul = new MockRuleConsul(DateTimeUtil.getCurrentDateTimeStr(), mockValues);

        consulClient.setKVValue(Constant.MOCK_RULE_KEY_CONSUL, JSONObject.toJSONString(rul));

        mockRuleMap.put(mockRule.getRequestPath(), mockRule);

        return true;
    }

    @Override
    public Boolean updateMockRule(MockRule mockRule) throws Exception {

        mockRule.setUpdateTime(DateTimeUtil.getCurrentDateTimeStr());
        Map<String, MockRule> map = new HashMap<String, MockRule>(mockRuleMap);

        map.put(mockRule.getRequestPath(), mockRule);

        MockRuleConsul rul = new MockRuleConsul(DateTimeUtil.getCurrentDateTimeStr(), new ArrayList<MockRule>(map.values()));

        consulClient.setKVValue(Constant.MOCK_RULE_KEY_CONSUL, JSONObject.toJSONString(rul));

        mockRuleMap.put(mockRule.getRequestPath(), mockRule);

        return true;
    }

    @Override
    public Boolean deleteMockRule(String requestPath) throws Exception {

        Map<String, MockRule> map = new HashMap<String, MockRule>(mockRuleMap);

        map.remove(requestPath);

        MockRuleConsul rul = new MockRuleConsul(DateTimeUtil.getCurrentDateTimeStr(), new ArrayList<MockRule>(map.values()));

        consulClient.setKVValue(Constant.MOCK_RULE_KEY_CONSUL, JSONObject.toJSONString(rul));

        mockRuleMap.remove(requestPath);

        return true;
    }

    @Override
    public MockRule getMockRule(String requestPath) throws Exception {

        return mockRuleMap.get(requestPath);
    }


    @Override
    public String getMockData(String requestPath) throws Exception {

        MockRule rule = mockRuleMap.get(requestPath);

        String resultData = "";

        //获取总体数据
        if (StringUtils.isNotBlank(rule.getResponseData())) {
            resultData = rule.getResponseData();
        } else {
            if (StringUtils.isBlank(rule.getMockUrl())) {
                return "";
            }
            resultData = restTemplate.getForObject(rule.getMockUrl(), String.class);
        }

        if (StringUtils.isBlank(resultData)) {
            return "";
        }

        try {
            JSONArray jsonArray = JSON.parseArray(resultData);

            if (jsonArray.size() == 1) {
                //如果只有一条直接返回
                return JSON.toJSONString(jsonArray.get(0));
            }

            //按规则处理数据
            resultData = handleData(jsonArray, rule);

        } catch (Exception e) {

            try {
                JSON.parseObject(resultData);
            } catch (Exception e1) {
                log.error("mock 数据格式错误！", e);
                return "";
            }

        }


        return resultData;
    }


    private String handleData(JSONArray jsonArray, MockRule rule) {

        int size = jsonArray.size();
        String result = "";

        try {
            if (MockHandleTypeEnum.RANDOM.getCode() == rule.getHandleType()) {
                //随机模式
                result = JSON.toJSONString(jsonArray.get(RandomUtils.nextInt(size)));

            } else if (MockHandleTypeEnum.ROLL.getCode() == rule.getHandleType()) {
                //轮询模式
                int validTime = rule.getValidTime() * 60; //转换成秒
                LocalDateTime updateTime = DateTimeUtil.parseLocalDateTime(rule.getUpdateTime());
                long seconds = DateTimeUtil.periodSeconds(updateTime, LocalDateTime.now());
                int tempDataIndex = 0;
                if (validTime < seconds) { //如果过了有效时间，则重新轮询
                    result = JSON.toJSONString(jsonArray.get(0));
                    tempDataIndex = 1;
                } else {
                    result = JSON.toJSONString(jsonArray.get(rule.getDataIndex()));

                    tempDataIndex = rule.getDataIndex() + 1;
                    if (size <= tempDataIndex) {
                        tempDataIndex = 0;
                    }
                }
                String now = DateTimeUtil.getCurrentDateTimeStr();

                //更新配置中心数据
                rule.setDataIndex(tempDataIndex);
                rule.setUpdateTime(now);
                if (!updateMockRule(rule)) {
                    String s = JSON.toJSONString(rule);
                    log.error("更新mock数据异常:{}", s);
                    throw new Exception("更新mock数据异常：" + s);
                }

            } else { //默认模式
                result = jsonArray.toJSONString();
            }

        } catch (Exception e) {
            log.error("按规则处理mock数据出现异常！", e);
        }


        return result;

    }

}
