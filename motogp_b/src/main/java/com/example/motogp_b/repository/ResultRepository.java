package com.example.motogp_b.repository;

import com.example.motogp_b.dto.ConstructorStandingDto;
import com.example.motogp_b.dto.RiderStandingDto;
import com.example.motogp_b.dto.TeamStandingDto;
import com.example.motogp_b.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {
    @Query("SELECT r FROM Result r WHERE " +
            "(:sessionId IS NULL OR r.session.id = :sessionId) AND " +
            "(:riderId IS NULL OR r.rider.riderId = :riderId) AND " +
            "(:teamId IS NULL OR r.team.id = :teamId) AND " +
            "(:manufacturerId IS NULL OR r.manufacturer.id = :manufacturerId) AND " +
            "(:position IS NULL OR r.position = :position) AND " +
            "(:status IS NULL OR r.status = :status)")
    List<Result> search(
            @Param("sessionId") String sessionId,
            @Param("riderId") String riderId,
            @Param("teamId") String teamId,
            @Param("manufacturerId") String manufacturerId,
            @Param("position") Integer position,
            @Param("status") String status);

    @Query("""
            select new com.example.motogp_b.dto.RiderStandingDto(r.firstName,r.lastName,r.nationality,r.photoUrl,t.name,sum(r1.points))
            from Rider r
            inner join Contract c on r.riderId = c.riderId
            inner join Team t on t.id = c.teamId
            left join Result r1 on r1.rider.riderId = c.riderId
            inner join Session s on s.id = r1.session.id
            where (:seasonYear is NULL or c.seasonId = :seasonYear)
            and (:categoryId is NULL or c.categoryId = :categoryId)
            and (s.sessionType in ('race','sprint'))
            group by r.riderId, r.firstName, r.lastName, r.nationality, r.photoUrl, t.name
            order by sum(r1.points) desc
            """)
    List<RiderStandingDto> getRiderStanding(@Param("seasonYear") Integer seasonId,@Param("categoryId") String categoryId);

    @Query("""
           select new com.example.motogp_b.dto.TeamStandingDto(t.name,sum(r.points))
           from Contract c
           inner join Team t on t.id = c.teamId
           left join Result r on r.team.id = c.teamId
           left join Session s on s.id = r.session.id
           where c.riderId = r.rider.riderId
           and (:seasonYear is NULL or c.seasonId = :seasonYear)
           and (:categoryId is NULL or c.categoryId = :categoryId)
           and (s.sessionType in ('race','sprint'))
           group by t.id, t.name
           order by sum(r.points) desc
           """)
    List<TeamStandingDto> getTeamStanding(@Param("seasonYear") Integer seasonId,@Param("categoryId") String categoryId);

    @Query("""
            select new com.example.motogp_b.dto.RiderStandingDto(r.firstName,r.lastName,r.nationality,r.photoUrl,t.name,sum(r1.points))
            from Rider r
            inner join Contract c on r.riderId = c.riderId
            inner join Team t on t.id = c.teamId
            left join Result r1 on r1.rider.riderId = c.riderId
            inner join Session s on s.id = r1.session.id
            where (:seasonYear is NULL or c.seasonId = :seasonYear)
            and (c.categoryId = 'cat_motogp')
            and (s.sessionType in ('q1','q2'))
            group by r.riderId, r.firstName, r.lastName, r.nationality, r.photoUrl, t.name
            order by sum(r1.points) desc
            """)
    List<RiderStandingDto> getRiderStandingByBMW(@Param("seasonYear") Integer seasonId);

    @Query("""
    SELECT new com.example.motogp_b.dto.ConstructorStandingDto(
        r.manufacturer.id,
        SUM(r.points)
    )
    FROM Result r
    JOIN r.session s
    WHERE s.sessionType = 'race'
      AND (:categoryId IS NULL OR s.category.categoryId = :categoryId)
      AND r.points = (
          SELECT MAX(r2.points)
          FROM Result r2
          WHERE r2.session.id = r.session.id
            AND r2.manufacturer.id = r.manufacturer.id
      )
    GROUP BY r.manufacturer.id
    ORDER BY SUM(r.points) desc
    """)
    List<ConstructorStandingDto> getConstructorStanding(@Param("seasonYear") Integer seasonId, @Param("categoryId") String categoryId);

}