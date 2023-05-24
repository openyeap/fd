package cn.zhumingwu.research.service;

import cn.zhumingwu.research.model.entity.FriendRelation;
import cn.zhumingwu.research.model.entity.PersonNode;
import cn.zhumingwu.research.repository.PersonNeo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service("neo4jServiceImpl")
public class Neo4jServiceImpl {

    @Autowired
    private PersonNeo4jRepository repository;


    public void saveFriendship(String fromName, String toName) {
        PersonNode from = repository.findByFirstName(fromName).get();
        PersonNode to = repository.findByFirstName(toName).get();

        FriendRelation FriendRelation = new FriendRelation();
        FriendRelation.setFrom(from);
        FriendRelation.setTo(to);

        //只需要在from节点保存关系即可
        from.addRelation(FriendRelation);
        repository.save(from);
    }

    //删除节点时，使用find，save 需要@Transactional注解
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriendship(String fromName, String toName) {
        PersonNode fromPersonNode = repository.findByFirstName(fromName).get();
        List<FriendRelation> FriendRelationList = fromPersonNode.getFriendList();
        for (Iterator<FriendRelation> iterator = FriendRelationList.iterator(); iterator.hasNext(); ) {
            FriendRelation relation = iterator.next();
            PersonNode fromNode = relation.getFrom();
            PersonNode toNode = relation.getTo();

            String fromNodeName = fromNode.getFirstName();
            String toNodeName = toNode.getFirstName();
            //判断 fromName 和 toName 需要删除的关系是否相等
            if (fromNodeName.equals(fromName) && toNodeName.equals(toName)) {
                iterator.remove();
            }
        }
        //只需要在from节点保存关系即可
        repository.save(fromPersonNode);
    }

}