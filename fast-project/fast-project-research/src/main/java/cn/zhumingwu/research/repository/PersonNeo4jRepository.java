package cn.zhumingwu.research.repository;

import cn.zhumingwu.research.model.entity.PersonNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface PersonNeo4jRepository extends Neo4jRepository<PersonNode, Long> {

    Optional<PersonNode> findById(Long id);

    Optional<PersonNode> findByFirstName(String name);

//    @Query(" match(inviter:User)-[r:Invite*1..]->(invitee:User) where inviter.userId = {inviterId} return  invitee")
//    List<PersonNode> getAllInvitees(@Param("inviterId") Long inviterId);
//
//    @Query(" match(invitee:User)<-[r:Invite*1..]-(inviter:User) where invitee.userId = {inviteeId} return  inviter")
//    List<PersonNode> getAllInviters(@Param("inviteeId") Long inviteeId);
//
//    @Query(" match(invitee:User)<-[r:Invite]-(inviter:User) where invitee.userId = {inviteeId} return  inviter")
//    PersonNode getInviter(@Param("inviteeId") Long inviteeId);
//
//    @Query(" match(inviter:User)-[r:Invite]->(invitee:User) where inviter.userId = {inviterId} return  count(invitee.userId)")
//    long countDirectByInviterId(@Param("inviterId") Long inviterId);
//
//    @Query(" match(inviter:User)-[r:Invite]->(invitee:User) where inviter.userId = {inviterId} and invitee.mallingBonusRole in {inviteeRoleList} return  count(invitee.userId)")
//    long countDirectByInviterIdAndInviteeRole(@Param("inviterId") Long inviterId, @Param("inviteeRoleList") List<Byte> inviteeRoleList);
//
//    @Query(" match(inviter:User)-[r:Invite]->(invitee:User) where inviter.userId = {inviterId} and invitee.mallingBonusRole={inviteeRole} return  count(invitee.userId)")
//    long countDirectByInviterIdAndInviteeRole(@Param("inviterId") Long inviterId, @Param("inviteeRole") Byte inviteeRole);
//
//    @Query(" match(inviter:User)-[r:Invite]->(invitee:User) where inviter.userId = {inviterId} return  count(invitee.userId)")
//    PersonNode getNearestInviter(@Param("inviterId") Long inviterId);
//
//    @Query(value = " match(inviter:User)-[r:Invite*0..]->(invitee:User) where inviter.userId = {inviterId} and invitee.userId in {userIdList}" +
//            "  and invitee.totalDirectInviteJpCount<{mallingBonusJpUpgradeSpCondition} return invitee order by totalDirectInviteJpCount desc ",
//            countQuery = " match(inviter:User)-[r:Invite*0..]->(invitee:User) where inviter.userId = {inviterId} and invitee.userId in {userIdList}" +
//                    "  and invitee.totalDirectInviteJpCount<{mallingBonusJpUpgradeSpCondition} return count(invitee) ")
//    Page<PersonNode> getInviteesForUpgradeEp(@Param("legId") Long legId, @Param("userIdList") List<Long> userIdList, Integer mallingBonusJpUpgradeSpCondition, Pageable pageable);
//
//    @Query(" match(inviter:User)-[r1:Invite]->(leg:User)-[r2:Invite*0..]->(invitee:User) where inviter.userId = {inviterId} and invitee.userId={userId} return leg ")
//    PersonNode findLeg(@Param("inviterId") Long inviterId, @Param("userId") Long userId);
//
//    @Query("match(inviter:User)-[r:Invite*]->(invitee:User) where inviter.userId = {userId} and not (invitee)-->() RETURN count(invitee)")
//    Long getFarthestInviteeCount(@Param("userId") Long userId);
//
//    @Query("match(inviter:User)-[r:Invite*]->(invitee:User) where inviter.userId = {userId} and not (invitee)-->() RETURN invitee order by invitee.id skip {skip} limit {limit}")
//    List<PersonNode> getFarthestInvitee(@Param("userId") Long userId, @Param("skip") Long skip, @Param("limit") Long limit);
//
//    @Query("match(head:User{userId:{headUserId}}), (farthest:User{userId:{farthestUserId}}), p=shortestPath((head)-[Invite*]-(farthest)) RETURN p")
//    List<PersonNode> getInviteePath(@Param("headUserId") Long headUserId, @Param("farthestUserId") Long farthestUserId);
//
//    @Query(value = "match(inviter:User)-[r:Invite]->(invitee:User) where inviter.userId = {userId} return invitee order by invitee.userId desc",
//            countQuery = "match(inviter:User)-[r:Invite]->(invitee:User) where inviter.userId = {userId} return count(invitee.userId)")
//    Page<PersonNode> getDirectInviteePage(@Param("userId") Long userId, Pageable pageable);
//
//    @Query("match(inviter:User)-[r:Invite*]->(invitee:User) where inviter.userId = {userId} return count(invitee.userId)")
//    long countAllInvitee(@Param("userId") Long userId);
//
//    @Query("match(:User{userId:{topUserId}})-[:Invite*1]->(leg:User)-[:Invite*0..]-> (:User{userId:{childUserId}}) return leg")
//    PersonNode getLegUser(@Param("topUserId") Long headUserId, @Param("childUserId") Long childUserId);
}

