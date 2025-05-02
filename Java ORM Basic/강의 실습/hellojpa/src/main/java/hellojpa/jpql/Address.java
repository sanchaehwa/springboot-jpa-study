package hellojpa.jpql;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Address {
    private String street;
    private String city;
    private String zipcode;

}
