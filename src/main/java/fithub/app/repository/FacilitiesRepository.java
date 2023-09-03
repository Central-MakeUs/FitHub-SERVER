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

//    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where (name like ? or address like ? or road_address like ?) order by ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) limit 15;",nativeQuery = true)
//    List<Object[]> findFacilitiesMyLoc(Float targetX, Float targetY,String keyword,String keyword2,String keyword3, Float targetX2,Float targetY2);

    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) <= ? order by distance;",nativeQuery = true)
    List<Object[]> findFacilities(Float userX, Float userY, Float targetX2,Float targetY2, Integer distance);

    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) <= ? and category = ? order by distance;",nativeQuery = true)
    List<Object[]> findFacilitiesCategory(Float userX, Float userY, Float targetX2,Float targetY2, Integer distance, Integer categoryId);

    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where (name like ? or address like ? or road_address like ?) and ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) <= ? order by distance;",nativeQuery = true)
    List<Object[]> findFacilitiesKeyword(Float userX, Float userY,String keyword,String keyword2,String keyword3, Float targetX2,Float targetY2, Integer distance);

    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where (name like ? or address like ? or road_address like ?) and ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) <= ? and category = ? order by distance;",nativeQuery = true)
    List<Object[]> findFacilitiesKeywordCategory(Float userX, Float userY,String keyword,String keyword2,String keyword3, Float targetX2,Float targetY2, Integer distance, Integer categoryId);

//    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where (name like ? or address like ? or road_address like ?) order by ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) limit 15;",nativeQuery = true)
//    List<Object[]> findFacilities(Float targetX, Float targetY,String keyword,String keyword2,String keyword3, Float targetX2,Float targetY2);

    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) <= ? and (name like ? or address like ? or road_address like ?) order by  distance;",nativeQuery = true)
    List<Object[]> findFacilitiesAllKeyword(Float targetX, Float targetY, Float targetX2,Float targetY2,Integer maxDistance, String keyword1, String keyword2,String keyword3);

    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) <= ?  order by  distance;",nativeQuery = true)
    List<Object[]> findFacilitiesAll(Float targetX, Float targetY, Float targetX2,Float targetY2,Integer maxDistance);

    @Query(value = "select name, address, road_address, image_url, phone_num, x, y, category, ROUND(ST_Distance_Sphere(POINT(facilities.x, facilities.y), Point(?,?))) as distance from facilities where ST_Distance_Sphere(Point(cast(facilities.x as float), cast(facilities.y as float )), Point(?, ?)) <= ? and category = ? order by  distance;",nativeQuery = true)
    List<Object[]> findFacilitiesCategoryAll(Float targetX, Float targetY, Float targetX2,Float targetY2,Integer maxDistance, Integer category);

//    @Query(value = "select name, address, road_address, image_url, phone_num, f.x, f.y, category, ROUND(ST_Distance_Sphere(POINT(f.x, f.y), Point(?,?))) as distance, target.x as centerX, target.y as centerY from facilities as f join (select sub_f.x, sub_f.y from facilities as sub_f where name like ? or address like ? limit 1) as target where ST_Distance_Sphere(Point(cast(f.x as float), cast(f.y as float )), Point(target.x, target.y)) <= ? and f.name like ? or f.address like ? order by distance;",nativeQuery = true)
@Query(value = "select name, address, road_address, image_url, phone_num, f.x, f.y, category, ROUND(ST_Distance_Sphere(POINT(f.x, f.y), Point(?,?))) as distance from facilities as f where f.name like ? or f.address like ? order by distance limit 15;", nativeQuery = true)
    List<Object[]> findFacilitiesByKeyword(Float userX,Float userY, String queryKeyword1, String queryKeyword2);

    // 카드뷰 바로 띄우기 위한 쿼리
//    @Query(value = "select name, address, road_address, image_url, phone_num, f.x, f.y, category, ROUND(ST_Distance_Sphere(POINT(f.x, f.y), Point(?,?))) as distance from facilities as f join (select sub_f.x, sub_f.y from facilities as sub_f where name like ? or address like ? limit 1) as target where ST_Distance_Sphere(Point(cast(f.x as float), cast(f.y as float )), Point(target.x, target.y)) <= ? and category = ? order by distance;",nativeQuery = true)


    @Query(value = "select name, address, road_address, image_url, phone_num, f.x, f.y, category, ROUND(ST_Distance_Sphere(POINT(f.x, f.y), Point(?,?))) as distance from facilities as f where f.name like ? or f.address like ? and category = ? order by distance limit 15;", nativeQuery = true)
    List<Object[]> findFacilitiesByKeywordCategory(Float userX,Float userY, String queryKeyword1, String queryKeyword2, Integer categoryId);

    Optional<Facilities> findByKakaoId(String kakaoId);
}
