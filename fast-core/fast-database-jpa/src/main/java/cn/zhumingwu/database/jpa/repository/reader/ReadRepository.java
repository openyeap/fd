package cn.zhumingwu.database.jpa.repository.reader;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ReadRepository<Entity, ID> extends JpaRepository<Entity, ID> {
}