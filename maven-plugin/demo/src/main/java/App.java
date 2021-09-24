package ltd.fdsa.demo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.maven.codegg.EggMojo;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class App {
    @SneakyThrows
    public static void main(String[] args) {

        //得到模板文件
        var root = EggMojo.class.getClassLoader().getResources("./templates");
        if (root == null) {
            log.info("failed!");
            return;
        }

        while (root.hasMoreElements()) {
            System.out.println(root.nextElement());
        }
        //得到class描述

        JarFile jarFile = new JarFile("D:\\Work\\java\\fast-data\\maven-plugin\\demo\\target\\demo-2.1.5-SNAPSHOT.jar");
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            log.info(entries.nextElement().toString());
        }

    }
}
