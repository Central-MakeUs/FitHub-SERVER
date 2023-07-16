package fithub.app.domain;


import fithub.app.domain.common.BaseEntity;
import fithub.app.domain.enums.Gender;
import fithub.app.domain.enums.SocialType;
import fithub.app.domain.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true ,nullable = true)
    private String email;

    @Column(length = 11, unique = true, nullable = true)
    private String phoneNum;

    @Column(length = 12, unique = true, nullable = true)
    private String nickname;

    private String profileUrl;

    @Column(length = 20, nullable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = true)
    private Integer age;

    @Column(nullable = true)
    private Boolean isSocial;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String password;

    @Column(nullable = true)
    private Boolean marketingAgree;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'ACTIVE'")
    private Status status;

    private LocalDateTime inactiveDate;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long reported;

    private String socialId;

    public User update(String name){
        this.name = name;
        return this;
    }

    public User socialJoin(String email, SocialType socialType){
        this.email = email;
        this.socialType = socialType;
        return this;
    }

    public User setPassword(String password){
        this.password = password;
        return this;
    }
}
