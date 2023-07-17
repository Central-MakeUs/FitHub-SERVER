package fithub.app.domain;


import fithub.app.domain.common.BaseEntity;
import fithub.app.domain.mapping.ArticleHashTag;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class HashTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long articles;

    @Column(columnDefinition = "VARCHAR(20)", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "hashTag", cascade = CascadeType.ALL)
    private List<ArticleHashTag> articleHashTagList = new ArrayList<>();
}
