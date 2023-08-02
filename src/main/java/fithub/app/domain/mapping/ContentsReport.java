package fithub.app.domain.mapping;


import fithub.app.domain.Article;
import fithub.app.domain.ContentsReportCategory;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.domain.common.BaseEntity;
import fithub.app.domain.enums.ContentsType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentsReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  reason;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "report_user")
    private User user;

    @Enumerated(EnumType.STRING)
    private ContentsType contentsType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private ContentsReportCategory contentsReportCategory;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "target_record")
    private Record record;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_article")
    private Article article;
}
