package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // constructor를 사용한 초기화보다 아래 방식이 더 형식적
    // 객체 생성 이후에 추가 작업을 하지 않는 것이 중요 - hibernate에서 파악 불가(persist로 인한 패키징)
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
