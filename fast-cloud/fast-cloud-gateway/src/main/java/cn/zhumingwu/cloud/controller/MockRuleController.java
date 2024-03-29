package cn.zhumingwu.cloud.controller;

import cn.zhumingwu.cloud.model.MockRule;
import cn.zhumingwu.cloud.service.IMockRuleService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.cloud.service.impl.MockRuleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/mockRule")
@Slf4j
public class MockRuleController {

    @Autowired
    private IMockRuleService mockRuleService;

    @PostMapping("/addOrUpdateMockRule")
    public Mono<ResponseEntity<Object>> addOrUpdateMockRule(@RequestBody MockRule mockRule) {


        if (Strings.isNullOrEmpty(mockRule.getRequestPath())) {
            return Mono.just((new ResponseEntity<Object>(HttpStatus.BAD_REQUEST)));
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
            return Mono.just((new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)));
        }
        return Mono.just((new ResponseEntity<Object>(HttpStatus.OK)));
    }

    @DeleteMapping("/deleteMockRule")
    public Mono<ResponseEntity<Object>> deleteMockRule(@RequestBody MockRule mockRule) {
        String requestPath = mockRule.getRequestPath();
        if (Strings.isNullOrEmpty(requestPath)) {
            return Mono.just((new ResponseEntity<Object>(HttpStatus.BAD_REQUEST)));
        }
        Map<String, MockRule> mockRuleMap = MockRuleServiceImpl.mockRuleMap;
        try {
            if (mockRuleMap.containsKey(requestPath)) {
                mockRuleService.deleteMockRule(requestPath);
            } else {
                return Mono.just((new ResponseEntity<Object>(HttpStatus.NO_CONTENT)));
            }
        } catch (Exception e) {
            log.error("deleteMockRule", e);
            return Mono.just((new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)));

        }
        return Mono.just((new ResponseEntity<Object>( HttpStatus.OK)));
    }

    @GetMapping("/getMockRuleList")
    public Mono<ResponseEntity<Object>> getMockRuleList(String requestPathKeyword) {
        try {
            return Mono.just((new ResponseEntity<Object>(mockRuleService.getMockRuleList(requestPathKeyword), HttpStatus.OK)));
        } catch (Exception e) {
            log.error("getMockRuleList", e);
            return Mono.just((new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)));
        }
    }
}
