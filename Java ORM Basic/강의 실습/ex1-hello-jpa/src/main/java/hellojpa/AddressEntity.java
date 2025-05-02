package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ADDRESSENTITY")
@NoArgsConstructor
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    public AddressEntity(String street, String city, String zipcode) {
        this.address = new Address(street, city,zipcode);
    }
    public AddressEntity(Address address) {
        this.address = address;
    }
}
