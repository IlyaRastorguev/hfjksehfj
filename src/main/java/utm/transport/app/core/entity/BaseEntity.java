package utm.transport.app.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GenericGenerator(name = "string_uuid_sequence", strategy = "utm.transport.app.core.hibernate.HibernateStringUUIDGenerator")
    @GeneratedValue(generator = "string_uuid_sequence")
    @Column(name = "ID", updatable = false, nullable = false)
    protected String id;

}
