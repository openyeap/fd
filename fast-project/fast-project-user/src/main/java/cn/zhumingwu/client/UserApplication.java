package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
*
* @ClassName:Application
* @description:Application
* @author:zhumingwu
* @since:2020-12-09
**/
@SpringBootApplication
@Slf4j
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}