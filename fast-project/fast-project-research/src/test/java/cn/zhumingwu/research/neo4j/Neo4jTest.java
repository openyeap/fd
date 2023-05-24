package cn.zhumingwu.research.neo4j;


import cn.zhumingwu.research.ResearchApplication;
import cn.zhumingwu.research.model.entity.PersonNode;
import cn.zhumingwu.research.repository.PersonNeo4jRepository;
import cn.zhumingwu.research.service.CsvFileRead;
import cn.zhumingwu.research.service.Neo4jServiceImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = ResearchApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class Neo4jTest {
    @Autowired
    private PersonNeo4jRepository studentRepository;

    @Autowired
    private Neo4jServiceImpl neo4jService;

    /**
     * 保存批量节点
     */
    @Test
    public void saveAllPersonNode() {
        List<PersonNode> PersonNodeList = new ArrayList<>();
        PersonNode PersonNode;
        for (int i = 1; i <= 10; i++) {
            PersonNode = new PersonNode();
            PersonNode.setFirstName("张" + i);
            PersonNodeList.add(PersonNode);
        }
        studentRepository.saveAll(PersonNodeList);
    }


    /**
     * 分页查询
     */
    @Test
    public void pageFindAll() throws IOException {
        List<PersonNode> list = new ArrayList<>();
        var reader = new CsvFileRead("D:\\Applications\\neo4j\\import\\person.csv");
        var i = 0;
        while (reader.readRecord()) {
            i++;
            var person = reader.getRecords();
            var node = new PersonNode();

            node.setName(person.get("Name"));
            node.setFirstName(person.get("FirstName"));
            node.setLastName(person.get("LastName"));
            node.setGender(person.get("Gender"));
            node.setBirthday(person.get("birthday"));

            node.setEmail(person.get("email"));
            node.setMobileNumber(person.get("MobileNumber"));
            node.setPhoneNumber(person.get("PhoneNumber"));

            node.setProvince(person.get("province"));
            node.setCity(person.get("city"));
            node.setDistrict(person.get("district"));
            node.setPostCode(person.get("PostCode"));
            node.setAddress(person.get("address"));

            node.setSource(person.get("source"));
            list.add(node);
            if (i > 10000) {
                System.out.println(reader.getErrors().toString());
                studentRepository.saveAll(list);
                i = 0;
                list = new ArrayList<>();
            }
        }
        reader.close();
    }


    /**
     * 删除 1 -> 3 友谊关系
     */
    @Test
    public void deleteFriendship() {
        neo4jService.deleteFriendship("张1", "张3");
    }
}