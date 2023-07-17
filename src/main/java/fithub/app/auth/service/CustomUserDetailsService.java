//package fithub.app.auth.service;
//
//import fithub.app.auth.dto.OAuthAttributes;
//import fithub.app.domain.User;
//import fithub.app.repository.ArticleRepositories.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService extends DefaultOAuth2UserService {
//    private final UserRepository userRepository;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
//        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
//
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
//                .getUserInfoEndpoint().getUserNameAttributeName();
//
//        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//        Map<String, Object> modifiableAttributes = new HashMap<>(attributes.getAttributes());
//        modifiableAttributes.put("registrationId", registrationId);
//        modifiableAttributes.put("userNameAttributeName", userNameAttributeName);
//
//        User user = saveOrUpdate(attributes);
////        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getMemberRole().getAuthority()));
//
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority("USER")),
//                modifiableAttributes,
//                attributes.getNameAttributeKey());
//    }
//
//    private User newUser(OAuthAttributes attributes) {
//        User newUser = attributes.toEntity();
//        return newUser;
//    }
//
//    private User saveOrUpdate(OAuthAttributes attributes) {
//        User user = userRepository.findByEmailAndSocialType(attributes.getEmail(), attributes.getSocialType())
//                .map(entity -> entity.update(attributes.getName()))
//                .orElseGet(() -> newUser(attributes));
//
//        return userRepository.save(user);
//    }
//}
