//package fithub.app.auth.dto;
//
//import fithub.app.domain.User;
//import fithub.app.domain.enums.SocialType;
//import lombok.Builder;
//import lombok.Getter;
//
//import java.util.Map;
//import java.util.Objects;
//
//@Getter
//public class OAuthAttributes {
//
//    private Map<String, Object> attributes;
//
//    private String nameAttributeKey;
//
//    private String name;
//
//    private String email;
//
//    private String picture;
//
//    private SocialType socialType;
//
//    @Builder
//    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
//    String name, String email, String picture, SocialType socialType){
//        this.attributes = attributes;
//        this.nameAttributeKey = nameAttributeKey;
//        this.name = name;
//        this.email = email;
//        this.picture = picture;
//        this.socialType = socialType;
//    }
//
//    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
//
//        return ofKakao("id", attributes);
//        // oAuthAttributes.setProfileImage(ProfileImageSelector.select());
//    }
//
//    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
//        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
//        Map<String, Object> profile = (Map<String, Object>) response.get("profile");
//        return OAuthAttributes.builder()
//                .name((String) profile.get("nickname"))
//                .email((String) response.get("email"))
//                .picture((String) profile.get("profile_image_url"))
//                .attributes(attributes)
//                .nameAttributeKey(userNameAttributeName)
//                .socialType(SocialType.kakao)
//                .build();
//    }
//
//    public User toEntity() {
//        return User.builder()
//                .name(name)
//                .email(email)
//                .socialType(socialType)
//                .profileUrl(picture)
//                .build();
//    }
//}
