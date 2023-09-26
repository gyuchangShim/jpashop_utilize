package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {
    // domain의 member class와 비슷한 역할 - thymeleaf에서의 에러 출력을 위해 따로 배정
    // 요구사항이 단순한 경우가 보통 없기 때문에 비슷한 entity여도 다른 form을 만들어 사용 - 유지보수의 기능 향상
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
