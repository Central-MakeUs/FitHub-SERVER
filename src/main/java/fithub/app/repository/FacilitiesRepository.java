package fithub.app.repository;

import fithub.app.domain.Facilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FacilitiesRepository extends JpaRepository<Facilities, Long> {



//    @Query("select new fithub.app.web.dto.responseDto.FacilitiesInfoDto(f.name, f.address, f.roadAddress, f.imageUrl, f.phoneNum, f.x, f.y, f.exerciseCategory.name, FUNCTION('ST_DISTANCE_SPHERE', FUNCTION('POINT', f.x, f.y), FUNCTION('POINT', :targetX, :targetY))) from Facilities f where FUNCTION('ST_DISTANCE_SPHERE', FUNCTION('POINT', f.x, f.y), FUNCTION('POINT', :targetX, :targetY)) <= :maxDistance and f.exerciseCategory = :category order by FUNCTION('ST_DISTANCE_SPHERE', FUNCTION('POINT', f.x, f.y), FUNCTION('POINT', :targetX, :targetY))")

    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) <= ? and category = ? and (name like ? or address like ? or road_address like ?) order by  distance;",nativeQuery = true)
    List<Object[]> findFacilitiesCategory(Float targetX, Float targetY, Float targetX2,Float targetY2,Integer maxDistance, Integer category, String keyword1, String keyword2,String keyword3);

    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) <= ? and category = ? order by  distance;",nativeQuery = true)
    List<Object[]> findFacilitiesCategoryAll(Float targetX, Float targetY, Float targetX2,Float targetY2,Integer maxDistance, Integer category);

    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) <= ? and (name like ? or address like ? or road_address like ?) order by  distance;",nativeQuery = true)
    List<Object[]> findFacilitiesAllKeyword(Float targetX, Float targetY, Float targetX2,Float targetY2,Integer maxDistance, String keyword1, String keyword2,String keyword3);

    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) <= ?  order by  distance;",nativeQuery = true)
    List<Object[]> findFacilitiesAll(Float targetX, Float targetY, Float targetX2,Float targetY2,Integer maxDistance);

    Optional<Facilities> findByKakaoId(String kakaoId);
}
