package cn.zhumingwu.client.jpa.repository.writer;

import cn.zhumingwu.client.jpa.entity.Group;
import cn.zhumingwu.database.jpa.repository.writer.WriteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupWriter extends WriteRepository<Group, Integer> {
}
