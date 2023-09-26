package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// member service의 기본적인 기능이 대부분 조회이기 때문에 readOnly 설정을 true로 -> 최적화
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    /** 단점이 있는 field injection
    @Autowired
    private MemberRepository memberRepository;
    */

    /** setter injection - 수정 가능성 존재
    private MemberRepository memberRepository;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
     */

    // constructor injection - RequiredArgsConstructor를 통해 constructor 자동 생성
    private final MemberRepository memberRepository;

    // 조회의 기능이 아닐경우만 추가로 transactional 설정
    @Transactional
    // 회원 가입
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    public void validateDuplicateMember(Member member) {
        // exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
