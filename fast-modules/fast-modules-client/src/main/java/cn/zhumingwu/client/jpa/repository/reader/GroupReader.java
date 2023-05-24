package cn.zhumingwu.client.jpa.repository.reader;


import cn.zhumingwu.client.jpa.entity.Group;
import cn.zhumingwu.database.jpa.repository.reader.ReadRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupReader extends ReadRepository<Group, Integer> {
}
