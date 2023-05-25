package cn.zhumingwu.dataswitch.admin.service.impl;

import cn.zhumingwu.dataswitch.admin.repository.JobGroupRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobRegistryRepository;
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
    @Resource
    private JobRegistryRepository jobRegistryRepository;

    public List<JobGroup> findByAddressList(int i) {
        return this.jobGroupRepository.findAll();
    }

    public List<Integer> findDead(int deadTimeout, Date date) {
        var list = this.jobRegistryRepository.findAll();
        return list.stream().map(x -> {
            return x.getId();
        }).collect(Collectors.toList());
    }

    public List<JobRegistry> findAll(int deadTimeout, Date date) {
        return this.jobRegistryRepository.findAll();
    }
}
