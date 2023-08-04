package fithub.app.domain;

import fithub.app.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer level;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long users;

    @Column(columnDefinition = "VARCHAR(10)", unique = true)
    private String name;

    private Integer maxContiguous;

    private Integer maxExp;

    private String gradeIcon;

    private String summary;

    private String description;
}
