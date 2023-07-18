package fithub.app.domain.mapping;
import fithub.app.domain.HashTag;
import fithub.app.domain.Record;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordHashTag {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hash_tag_id")
    private HashTag hashTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    public void setRecord(Record record){
        if (this.record != null) {
            this.record.getRecordHashTagList().remove(this);
        }
        this.record = record;
        record.getRecordHashTagList().add(this);
    }

    public void setHashTag(HashTag hashTag){
        if (this.hashTag != null) {
            this.hashTag.getRecordHashTagList().remove(this);
        }
        this.hashTag = hashTag;
        hashTag.getRecordHashTagList().add(this);
    }
}
