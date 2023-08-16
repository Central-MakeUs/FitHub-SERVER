package fithub.app.domain;

import fithub.app.domain.enums.RankingStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BestRecorder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer ranking;

    private LocalDate standardDate;

    @Enumerated(EnumType.STRING)
    private RankingStatus rankingStatus;

    private Long userId;

    private String nickname;

    private String exerciseName;

    private Integer level;

    private String gradeName;

    private Long recordCount;

    private String profileUrl;

    void setUserId(Long userId){

    }
}
