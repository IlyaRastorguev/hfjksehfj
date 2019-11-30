package utm.transport.app.entity.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utm.transport.app.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stop")
public class Stop extends BaseEntity {

    @Column(name = "lat")
    private Double lat;
    @Column(name = "lon")
    private Double lon;
}
