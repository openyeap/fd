package cn.zhumingwu.research.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;


@Getter
@Setter
@RelationshipEntity(type = "FRIEND_RELATION")
public class FriendRelation {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private PersonNode from;

    @EndNode
    private PersonNode to;
}
