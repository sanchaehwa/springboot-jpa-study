package hellojpa.jpql;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public enum MemberType {
    ADMIN,
    USER
}
