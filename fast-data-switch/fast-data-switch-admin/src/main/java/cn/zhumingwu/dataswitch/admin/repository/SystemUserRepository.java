package cn.zhumingwu.dataswitch.admin.repository;


import cn.zhumingwu.dataswitch.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemUserRepository extends JpaRepository<User, Integer> {
}
