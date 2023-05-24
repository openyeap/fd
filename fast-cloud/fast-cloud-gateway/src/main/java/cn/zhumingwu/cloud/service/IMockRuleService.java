package cn.zhumingwu.cloud.service;

import cn.zhumingwu.cloud.model.MockRule;

import java.util.List;

/**
 * mock规则操作  
 */
public interface IMockRuleService {


    List<MockRule> getMockRuleList(String requestPathKeyword) throws Exception;

    Boolean addMockRule(MockRule mockRule) throws Exception;

    Boolean updateMockRule(MockRule mockRule) throws Exception;

    Boolean deleteMockRule(String requestPath) throws Exception;

    MockRule getMockRule(String requestPath) throws Exception;

    String getMockData(String requestPath) throws Exception;


}
