package ltd.fdsa.research.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NodeEntity(label = "PERSON")
public class PersonNode implements Serializable {

    private static final long serialVersionUID = 3870028896024217898L;

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "name")
    private String name;

    @Property(name = "firstName")
    private String firstName;

    @Property(name = "lastName")
    private String lastName;

    @Property(name = "gender")
    private String gender;

    @Property(name = "birthday")
    private String birthday;

    @Property(name = "email")
    private String email;

    @Property(name = "mobileNumber")
    private String mobileNumber;

    @Property(name = "phoneNumber")
    private String phoneNumber;

    @Property(name = "source")
    private String source;

    @Property(name = "province")
    private String province;

    @Property(name = "city")
    private String city;

    @Property(name = "district")
    private String district;

    @Property(name = "postCode")
    private String postCode;

    @Property(name = "address")
    private String address;


    /**
     * 关系，定义为友谊
     */
    @Relationship(type = "FRIENDSHIP_RELATION")
    private List<FriendRelation> friendList;

    /**
     * 添加友谊的关系
     *
     * @param friendRelation
     */
    public void addRelation(FriendRelation friendRelation) {
        if (this.friendList == null) {
            this.friendList = new ArrayList<>();
        }
        this.friendList.add(friendRelation);
    }
}



