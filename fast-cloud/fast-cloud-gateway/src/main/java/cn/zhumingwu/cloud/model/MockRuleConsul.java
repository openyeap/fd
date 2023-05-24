package cn.zhumingwu.cloud.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MockRuleConsul {

    private String updateTime;
    private List<MockRule> data;

}
