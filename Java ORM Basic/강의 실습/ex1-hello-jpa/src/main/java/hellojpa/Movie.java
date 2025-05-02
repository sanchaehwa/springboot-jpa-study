package hellojpa;


import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Table(name ="movie")
@Getter
@Setter
//Item의 dtype 컬럼에 값을 넣어줄때 Class 명이 아닌, 직접 설정해준 값으로 설정하기 위해 선언
@DiscriminatorValue("MOVIE")

public class Movie extends Item{

    private String director;
    private String actor;


}
