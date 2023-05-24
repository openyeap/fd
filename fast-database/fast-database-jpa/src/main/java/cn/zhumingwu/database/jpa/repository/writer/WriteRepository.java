package cn.zhumingwu.database.jpa.repository.writer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface WriteRepository<Entity, ID> extends JpaRepository<Entity, ID> {

}