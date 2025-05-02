package hellojpa;


import jakarta.persistence.*;
import lombok.*;

//Item 상속
@Entity
@Table(name="Album")
@Getter
@Setter
@NoArgsConstructor
public class Album extends Item{


    private String artist;


}
