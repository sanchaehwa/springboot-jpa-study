package hellojpa.jpql;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor


public class MemberDTO {
    private String username;
    private int age;
    public MemberDTO(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
