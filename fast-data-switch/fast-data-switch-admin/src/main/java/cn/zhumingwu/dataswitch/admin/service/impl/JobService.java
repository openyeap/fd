package cn.zhumingwu.dataswitch.admin.service.impl;

import cn.zhumingwu.dataswitch.admin.repository.JobGroupRepository;
import lombok.var;
import cn.zhumingwu.dataswitch.admin.entity.JobGroup;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Resource
    private JobGroupRepository jobGroupRepository;


    public List<JobGroup> findByAddressList(int i) {
        return this.jobGroupRepository.findAll();
    }

}
