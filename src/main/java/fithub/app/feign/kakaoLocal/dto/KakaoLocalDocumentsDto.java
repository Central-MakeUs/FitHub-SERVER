package fithub.app.feign.kakaoLocal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoLocalDocumentsDto {
    String id;
    String place_name;
    String category_name;
    String category_group_code;
    String category_group_name;
    String phone;
    String address_name;
    String road_address_name;
    String x;
    String y;
    String place_url;
    String distance;
}
