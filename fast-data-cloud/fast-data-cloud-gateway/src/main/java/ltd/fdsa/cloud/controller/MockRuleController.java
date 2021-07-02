package ltd.fdsa.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.model.MockRule;

import ltd.fdsa.cloud.service.IMockRuleService;
import ltd.fdsa.cloud.service.impl.MockRuleServiceImpl;

import ltd.fdsa.web.enums.HttpCode;
import ltd.fdsa.web.view.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mockRule")
@Slf4j
public class MockRuleController {

    @Autowired
    private IMockRuleService mockRuleService;

    @PostMapping("/addOrUpdateMockRule")
    public  Result addOrUpdateMockRule(@RequestBody MockRule mockRule) {
        if (StringUtils.isBlank(mockRule.getRequestPath())) {
            return Result.fail(HttpCode.PARAMETER_EMPTY);
        }
        Map<String, MockRule> mockRuleMap = MockRuleServiceImpl.mockRuleMap;
        try {
            if (mockRuleMap.containsKey(mockRule.getRequestPath())) {
                mockRuleService.updateMockRule(mockRule);
            } else {
                mockRuleService.addMockRule(mockRule);
            }
        } catch (Exception e) {
            log.error("addOrUpdateMockRule", e);
            return Result.fail(HttpCode.INTERNAL_SERVER_ERROR);
        }
        return Result.success();
    }

    @DeleteMapping("/deleteMockRule")
    public Result deleteMockRule(@RequestBody MockRule mockRule) {
        String requestPath = mockRule.getRequestPath();
        if (StringUtils.isEmpty(requestPath)) {
            return Result.fail(HttpCode.PARAMETER_EMPTY);
        }
        Map<String, MockRule> mockRuleMap = MockRuleServiceImpl.mockRuleMap;
        try {
            if (mockRuleMap.containsKey(requestPath)) {
                mockRuleService.deleteMockRule(requestPath);
            } else {
                return Result.fail(HttpCode.NO_CONTENT);
            }
        } catch (Exception e) {
            log.error("deleteMockRule", e);
            return Result.fail(HttpCode.INTERNAL_SERVER_ERROR);
        }
        return Result.success();
    }

    @GetMapping("/getMockRuleList")
    public Result getMockRuleList(String requestPathKeyword) {
        try {
            return Result.success(mockRuleService.getMockRuleList(requestPathKeyword));
        } catch (Exception e) {
            log.error("getMockRuleList", e);
            return Result.fail(HttpCode.INTERNAL_SERVER_ERROR);
        }
    }
}
