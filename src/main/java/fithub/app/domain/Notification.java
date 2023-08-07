package fithub.app.domain;

import fithub.app.domain.common.BaseEntity;
import fithub.app.domain.enums.NotificationCategory;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationCategory notificationCategory;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "target_ad")
    private Advertise advertise;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "record_id")
    private Record record;

    private String notificationBody;

    private Boolean isConfirmed;

    public void setUser(User user){
        if(this.user != null){
            user.getNotificationList().remove(this);
        }
        this.user = user;
        user.getNotificationList().add(this);
    }

    public void setIsConfirmed(){
        this.isConfirmed = true;
    }
}
