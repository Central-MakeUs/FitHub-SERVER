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

    @Column(unique = true ,nullable = false)
    private String email;

    @Column(length = 11, unique = true, nullable = false)
    private String phoneNum;

    @Column(length = 12, unique = true, nullable = false)
    private String nickname;

    private String profileUrl;

    @Column(length = 20, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Boolean isSocial;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String password;

    @Column(nullable = false)
    private Boolean marketingAgree;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'ACTIVE'")
    private Status status;

    private LocalDateTime inactiveDate;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long reported;
}
