package jpabook.jpashop.domain;

import lombok.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
// 다른 클래스 등에서 new를 통한 할당이 무분별하게 이뤄지는 것을 방지
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // FetchType.EAGER - 즉시 로딩(관련된 모든 데이터를 가져오는 것 - 절대 사용 X)
    // FetchType.LAZY - 지연 로딩(원하는 데이터만을 가져올 때)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // JPQL -> select * from order : 같은 쿼리를 연관된 수 + 1만큼 반복 사용의 가능성 존재

    // OneToMany : 기본적으로 지연 로딩 -> 따로 LAZY로 변환할 필요 X
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    
    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [Order, Cancle]

    // 연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancle() {
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCLE);
        for(OrderItem orderItem : orderItems) {
            orderItem.cancle();
        }
    }

    //==조회 로직==//

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        /*
        int totalPrice = 0;
        for(OrderItems orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice
        */

        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }


}
