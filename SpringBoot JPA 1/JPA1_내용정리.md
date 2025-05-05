### JPA

- JPA(Java Persistence API)의 약자로 자바 객체(엔티티)와 관계형 데이터베이스 사이를 연결해주는 ORM(Object Relation Mapping) 기술
- ORM 이란
    
    객체(Object ), 관계형 DB (Relation Database) 자동으로 매핑해주는 기술 
    

## 연관관계

### 연관관계 매핑

- 객체 지향 프로그래밍의 객체 간 관계를, 관계형 데이터베이스의 테이블 간 외래키(FK) 관계에 매핑
- JPA에는 이를 통해 엔티티 간의 관계를 정의하고 , SQL 없이 객체 지향 방식으로 데이터 조작을 가능하게 함.
- SELECT UPDATE JOIN 등 쿼리문을 직접 사용하지 않고도, 객체 간의 관계와 상태 만으로 데이터 접근 및 변경 가능.
- 자바는  참조를 통해 객체간의 관계를 맺고, 데이터베이스는 외래키(FK)를 통해 테이블 간의 관계를 맺음
    
    ⇒ 이 둘 사이의 패러다임 불일치를 해결하기 위해 연관관계 매핑이 필요함.
    

### 엔티티와 테이블 매핑

- 자바에서 객체로 데이터를 다루고, 데이터 베이스에서는 테이블로 데이터를 저장
- JPA는 @Entity 에너테이션을 사용해, 자바 클래스와 테이블간의 매핑을 설정하고, 해당 클래스가 엔티티 객체임을 인식
- 객체 중심 프로그래밍 가능.

```java
@Entity
@Table(name="members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_id")
    private Long id;
 }

```

### 연관관계 매핑 종류

- `@OneToOne` : 일대일 관계
- `@OneToMany` : 일대다 관계
- `@ManyToOne` : 다대일 관계
- `@ManyToMany` : 다대다 관계

> `@ManyToMany` 는 자동 생성되는 중간 테이블은 외래 키외에 추가 필드를 포함할수 없기 때문에 사용을 지양한다. `@ManyToMany` 사용 대신, 중간 테이블을 위한 엔티티를 직접 생성해. 이를 통해, 1-N N-1 관계로 구성하는 방식이 바람직하다.  중간테이블을 직접 정의하게되면 추가 필드도 포함 할 수 있다.
> 

### 단방향 , 양방향 매핑

**단방향 매핑: 한쪽 엔티티만 관계를 알고 있는 구조**

```java
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id")  // FK
    private Team team; //Member는 Team을 참조함 
    
}

@Entity
public class Team {

    @Id @GeneratedValue
    private Long id;
    
    private String name;

    //Team은 Member를 모름.
}
```

**양방향 매핑: 양쪽 엔티티가 서로 참조하는 구조.** 

```java
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    //Team의 외래키(FK)를 가지고 있음.
    @JoinColumn(name = "team_id")
    private Team team;

    // 양방향 관계이므로, 연관관계 편의 메서드 권장 (동기화)
    public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}

@Entity
public class Team {

    @Id @GeneratedValue
    private Long id;
    
    private String name;
		
	
    @OneToMany(mappedBy = "team") 
    //연관관계 주인이 아닌쪽에 mappedBy(연관관계의 주인은 외래키가 있는쪽)
  
    private List<Member> members = new ArrayList<>();
}

//비 주인쪽으로(연관관계 주인이 아닌) 연관관계를 설정한 경우
Team team = new Team();
Member member = new Member();

member.setTeam(teamA) //주인(member) 쪽에서 설정했기때문에 , 외래키 값이 실제로 설정
team.getMembers().add(member) //주인이 아닌쪽(Team)에서 설정 하였기 때문에 DB에 아무 영향 없음.
```

**다대일(N:1) 관계**

- 외래 키는 항상 다(N) 쪽에 위치함.
- 여러 개의 `Order`는 하나의 `User`에 속하므로, `Order` 테이블에 `user_id` 외래 키가 존재.
- JPA에서도 `@ManyToOne`이 있는 쪽이 연관관계의 주인이 되며, 외래 키를 소유하게 됨.

**일대일(1:1) 관계**

- 외래 키를 어느 테이블에 둘 것인지는 자유롭지만, 일반적으로 다음 기준에 따라 결정
    - 더 자주 조회되는 테이블에 FK를 둔다.
    - 주 테이블이 명확하면 보조 테이블에 FK를 둔다.
- FK에는 `UNIQUE` 제약 조건을 걸어 1:1 관계를 보장해야 함.
    - `UNIQUE` : 어떤 칼럼에 같은 값이 두번 이상 들어가지 못하게 막는 규칙
    
       [참고]  PRIMARY KEY도 기본적으로 UNIQUE속성을 가지고 있음 (중복 불가 + NOT NULL)
                   UNIQUE 는 보조 유일 조건 → 유일하긴 해야하지만 없어도됨 (중복불가 + NULL)
    

### 엔티티에서 Setter 사용을 지양해야하는이유

- 외부에서 객체의 내부 상태를 마음대로 바꿀 수 있어,불변성(integrity) 이 깨질 수 있음.
- JPA는 트랜잭션 안에서 엔티티의 변경을 감지해서 SQL로 반영하는데, setter를 남용하면 변경 시점이나 원인을 추적하기 어려움. (⇒ JPA의 변경 감지 기능 (Dirty Checking) 에 방해됨)
- Form 객체는 **단순히 사용자 입력을 받는 구조**이므로, 복잡한 로직이 없고 변경 감지 대상도 아님 → setter 사용해도 문제 없음
- setter를 사용하지않고, 객체 설정하는 코드

```java
//1. 기본 생성자 + 접근 제어자 PROTECTED 설정
@NoArgsConstructor(access = AccessLevel.PROTECTED) 
//외부에서 기본 생성자로 객체 생성하지 못하게 막음(new Order() 가 안됨)
//JPA는 내부적으로 Proxy 생성을 위해 기본 생성자가 필요하므로 protected 로 열어둠

//@Builder Pattern의 생성자
@Builder
public Order(Member member, Delivery delivery, OrderStatus status)

//정적팩토리 매서드 - 객체 생성의 전과정을 캡슐화 (**Setter 없이도 OrderItem 과의 관계를 모두 설정)
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .status(OrderStatus.ORDERED)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        return order;
    }
//양방향 연관관계를 안전하게 설정(동기화)
public void addOrderItem(OrderItem orderItem)
public void setDelivery(Delivery delivery)
```

### 즉시로딩(EAGER) 이 아닌, 지연로딩(LAZY)로 설정

- 즉시 로딩(Eager Loading)은 SQL 실행 시 연관된 엔티티 까지 함께 가져오게 되는데, 이로 인해 실행되는 SQL을 예측하기 어렵고, 어떤 시점에 어떤 쿼리가 나갈지 추적하기 힘들다.
- 즉시로딩으로 JPQL을 사용할 때 N+1 문제가 자주 발생할 수 있으며, 연관된 엔티티를 함께 조회해야 하는 경우엔 fetch join 또는 @EntityGraph 같은 기능을 명시적으로 사용하는 것이 좋다.
    - N+1 문제 : 각 엔티티의 연관된 데이터를 가지고 오기 위해 추가로 N개의 쿼리가 실행 되는 문제
    - Fetch Join: 연관된 엔티티를 한번에 조회하기 위해 사용하는 JOIN 문
    - @EntityGraph : JPQL 없이도 특정 엔티티를 조회할때, 연관된 엔티티를 함꼐 로딩할 수 있게 도와주는 기능
- 또한, JPA에서는 `@ManyToOne`, `@OneToOne` 관계의 기본 fetch 전략이 즉시 로딩(EAGER)이므로, 지연 로딩(LAZY)으로 변경해주는 것이 권장된다. `(**One은 EAGER , **Many 는 LAZY)`

### **영속화란?**

- 자바 객체를 JPA가 관리하게 하는 것.
- `EntityManager.persist()`를 호출해서, **JPA의 영속성 컨텍스트**에 등록하는 것을 의미.

### **영속성 컨텍스트란?**

- 엔티티를 영구 저장하는 **JPA 내부 메모리 공간**.
- DB와 애플리케이션 사이의 **1차 캐시 역할**을 하며, 엔티티 객체들을 저장하고 관리함.
- 영속성 컨텍스트 상태(생명주기)

| **상태** | **설명** |
| --- | --- |
| **비영속 (new)** | JPA와 아무 관련 없는 상태 (new 객체 생성만 한 상태) |
| **영속 (managed)** | em.persist()로 JPA가 관리하는 상태 |
| **준영속 (detached)** | 관리하다가 em.detach() 등으로 관리 중지 |
| **삭제 (removed)** | em.remove()로 삭제된 상태 |
- 영속성 컨텍스트의 주요 기능

| **기능** | **설명** |
| --- | --- |
| **1차 캐시** | 같은 ID의 엔티티는 한번만 DB에서 조회 → 이후 캐시 사용 |
| **변경 감지 (Dirty Checking)** | 엔티티 값 변경 시 트랜잭션 커밋 시점에 자동 update |
| **지연 로딩 (Lazy Loading)** | 연관된 객체는 실제로 사용할 때 DB 조회 (최적화) |
| **쓰기 지연 저장소 (Flush)** | 쿼리들을 모아서 트랜잭션 커밋 시 한꺼번에 DB로 전송 |

### **Dirty Checking & em.flush()**

- Dirty Checking은 영속 상태의 엔티티가 **필드 변경되었는지 감지**하는 JPA 메커니즘.
- em.flush()는 변경된 내용을 실제 **DB에 반영(전송)**하는 시점.

> **작동 흐름**
> 
> 1. 엔티티 필드 값 변경: member.setName("new")
> 2. 트랜잭션 커밋 시 변경 감지 수행
> 3. em.flush() → JPA가 UPDATE SQL 자동 생성 후 DB 전송

### 1차 캐시

- 영속성 컨텍스트는 내부에 1차 캐시가 존재한다.
- 엔티티를 영속성 컨텍스트에 저장하는 순간, 1차 캐시에 
Key : @Id 로 선언한 필드 값, Value : 해당 엔티티 자체로 캐시에 저장

```java
@Entity
public class Member {
    @Id
    private Long id;
    private String name;
}
//em.find 하면 key - 1L , value - new mamber(id=1, name - ...)
```

- em.find() , 엔티티 매니저 내부의 1차 캐시부터 찾는다. 1차 캐시에 엔티티가 존재하면 바로 반환하고 DB를 들리지않는다. (*1차 캐시에 데이터가 없다면 데이터 베이스에서 조회한다.)
- 1차 캐시는 글로벌 하지않다, 해당 스레드 하나가 시작할때 부터 끝날때까지 그러니깐 트랜잭션이 끝날때까지만 잠깐 쓰는 것이다. 트랜잭션 범위 안에서만 사용하는 굉장히 짧은 캐시 레이어이다.

### **spring.jpa.open-in-view 설정**

**Open-in-view=true란**

- 뷰(View) 렌더링 시점까지 영속성 컨텍스트를 **열어두는 설정**
- 컨트롤러나 뷰에서 Lazy 로딩된 필드를 **바로 접근 가능**하게 도와줌

```java
@GetMapping("/orders")
public String orders(Model model) {
    List<Order> orders = orderRepository.findAll(); // Lazy 필드 있음
    model.addAttribute("orders", orders); // View에서 order.getMember() 접근
    return "orderList";
}
```

- 실무에서는 True가 아닌 False로 설정.

| **이유** | **설명** |
| --- | --- |
| **계층 분리 위반** | 컨트롤러/뷰에서 DB 접근은 **역할 분리 원칙 위반** |
| **트랜잭션 범위가 애매함** | 서비스 계층에서 트랜잭션이 끝났다고 생각해도 실제 쿼리는 View에서 나감 |
| **성능 문제 발생** | Lazy 로딩이 View까지 이어지면, **N+1 문제** 발생 가능 |

## 도메인 설계

### **트랜잭션**

- 트랜잭션은 하나의 작업 단위로, 여러 작업을 하나처럼 묶어 모두 성공하거나, 모두 실패하게 만드는 기능
- DB 작업이 모두 성공하거나, 하나라도 실패 시 **전부 롤백**되는 구조.

트랜잭션 핵심 속성 (ACID):

| **속성** | **설명** |
| --- | --- |
| 원자성 | 모든 작업이 하나의 단위로 실행, 하나라도 실패하면 전체 롤백 |
| 일관성 | 트랜잭션 전후로 DB의 상태가 항상 일관되게 유지 |
| 격리성 | 동시에 여러 트랜잭션이 실행되어도 서로 간섭하지 않음 |
| 지속성 | 트랜잭션이 커밋되면 결과가 영구히 DB에 저장됨 |

서비스에서 트랜잭션 써야하는 이유

- 비즈니스 로직은 도메인에 위임했지만, 트랜잭션 경계는 서비스에서 잡아줘야함.

### **Entity Manager (엔티티 매니저)**

- JPA에서 데이터베이스와 상호작용하는 핵심 객체

> JPA는 인터페이스만 제공하고, 실제로는 하이버네이트 같은 구현체가 내부적으로 EntityManager를 동작시켜줌. 즉, 우리가 em.persist() 이런 걸 쓰면, JPA가 하이버네이트를 통해 SQL로 바꿔서 DB에 날려주는 구조
> 

| **메서드** | **설명** |
| --- | --- |
| `persist()` | 엔티티를 영속성 컨텍스트에 저장하고 DB에 INSERT 함 |
| `find()` | PK로 엔티티를 조회함 |
| `remove()` | 엔티티를 삭제함 (DELETE) |
| `merge()` | 분리(detached) 상태의 엔티티를 영속 상태로 변경 |
| `createQuery()` | JPQL을 사용한 쿼리를 생성하고 실행함 (Ex. 단건 조회가 아닌 전체 조회가 필요할 때) |

### NotNull

- `@NotBlank` , `@NotEmpty` 의 차이
    - 둘다, null 값을 허용하지않는다는것.
    - `@NotEmpty` : String , Collection, Map, Array
    - `@NotBlank`  :오직 String 타입만 쓰임 (공백만 있는 문자열도 안됨)
- `@Column(nullable=false)` : DB 스키마 제약. DB 자체에서 Null 값 막는거
- `@NotBlank, @NotEmpty`  :  애플리케이션 수준의 유효성 검사. DB가기전에 막음.
- 숫자형 검증 (`@NotBlank, @NotEmpty` 사용 못함)
    - `@Min(1)`  : 최소가 1인거 (0 안된다)
    - `@Max(100)` : 최대가 100 (100까지만 입력)
    - `@Positve` : 양수인지 * 양수이면서 0 도 포함하려면 :  `@PositveOrZero`
    - `@Negative` : 음수 *음수이면서 0도 포함하려면 : `@NegativeOrZero`
    

### @NoArgsConstructor - @NoArgsConstructor(access=AccessLevel.PROTECTED)

- `@NoArgsConstructor` : 기본 생성자 (파리미터 없는 생성자 만들어줌)
- 기본 접근 제어자(Public)

```java
@NoArgsConstructor 
@Entity 
public class Member {
		private String name;
} 
//생성되는 코드 : public Member() {}
```

- `@NoArgsConstructor(access=AccessLevel.PROTECTED)` : 접근제어가가 Public이 아닌 Protected

```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    private String name;
}
//생성되는 코드
public class Member {
		private String name;
		protected Member() {
		
}
```

- JPA는 기본적으로 기본 생성자를 요구함. 이 때문에 `@NoArgsConstructor` 를 작성하게 됨.
- 접근권한을 Private로 설정하게 되면 User의 접근 권한이 Private이기때문에 지연로딩시 JPA 구현체가 User 프록시 객체를 생성할때 접근할 수 없음 → 사용을 권장하지 않음
- 접근권한을 Public으로 하면 무분별한 객체 생성 및 Setter를 통한 값 주입을 할 수 있기때문에, 접근 권한 Protected로 작성 다.

### @SuperBuilder 와 @AllArgsConstructor 의 관계

`@Builder`는 해당 클래스의 필드만 적용해 자동으로 빌더를 생성하기 때문에 상속 관계를 가지고 있다면 

`@SuperBuilder` 를 적용시켜줘야 한다.

`@Builder` : 단일 클래스의 빌더 패턴을 생성함. 기본적인 빌더 패턴에 필요한 매서드를 자동 생서. 해당 클래스와 해당 클래스의 필드에만 적용해, 자동으로 생성된 빌더를 사용할 수 있음.

`@SuperBuilder` : 상속 관계를 가진 클래스에서 빌더 패턴을 사용할 수 있게해주고, 모든 하위 클래스에 대한 빌더 클래스를 생성하며, 빌더 패턴의 확장을 허용함. 
상속 관계에 있는 클래스를 생성할 때, 부모 클래스의 속성과 자식 클래스의 속성을 모두 고려한 빌더를 사용할 수 있다.

`@AllArgsConstructor` 를 상속관계 매핑에서 사용하면, 특히 JPA 엔티티에서 쓸 경우, 부모 클래스의 모든 필드를 강제적으로 채워야하는 생성자가 만들어짐

- JPA는 프록시 객체 생성이나 내부 매커니즘 때문에 기본 생성자(파라미터 없는 생성자)가 반드시 필요함.
- `@AllArgsConstructo`r 이랑 `@SuperBuilde`r 같이 쓰면 충돌하거나 중복 문제 생길수 있음
- 그래서 상속구조에서는 `@AllArgsConstructor` 지양

### 다대다 관계

예로 들어, Category 와 Item은 다대다 관계이다. 하나의 Item은 여러 Category에 속할 수 있고, 하나의 Category는 여러 Item을 가질 수 있다

단순하게 다대다 관계는 JPA에서 아래와 같이 표현

```java
@ManyToMany
@JoinTable(name = "category_item")
private List<Item> items;
```

하지만, 다대다는 실무에서 지양하는 관계이다. 유연성(중간 테이블에 다른 컬럼을 넣을 수 없음)이 부족할 뿐더러, 관리가 어렵다. 나중에 관계를 더 구체적으로 관리해야하는 어려움이 발생한다. 

다대다 관계를 1대다 → 다대1로 풀면서 결론적으로 중간 엔티티를 통해 1:N + N+1관계로 쪼개진다.

*Category_item_id = 중간 엔티티

| **category_item_id** | **category_id** | **item_id** |
| --- | --- | --- |
| 1 | 10 | 100 |
| 2 | 10 | 101 |
| 3 | 11 | 100 |

### **도메인 모델 패턴 / 트랜잭션 스크립트 패턴**

| **구분** | **도메인 모델 패턴** | **트랜잭션 스크립트 패턴** |
| --- | --- | --- |
| **정의** | 비즈니스 로직을 도메인 객체(엔티티) 안에 구현 | 비즈니스 로직을 서비스 계층에 몰아서 구현 |
| **구조** | 상태 + 행동 = 같은 객체 안에 | 도메인 객체는 데이터만 보유 (getter/setter) |
| **행위 책임** | 도메인 객체가 스스로 처리 | 서비스 계층이 전부 처리 |
| **서비스 역할** | 트랜잭션 관리 및 도메인 호출만 위임 | 트랜잭션 + 로직 전부 처리 |
| **적합 상황** | 복잡한 비즈니스 로직 | 단순 CRUD 위주 |
| **장점** | - 로직 응집력↑- 도메인 주도 설계(Domain-Driven Design, DDD)에 적합 | - 단순하고 빠른 개발 |
| **단점** | - 구조가 복잡할 수 있음- 초기 진입장벽↑ | - 로직 커지면 서비스 비대화- 재사용성↓ |

```java
//도메인 모델 패턴

// 도메인 (Order)
public void cancelOrder() {
    if (delivery.getStatus() == DeliveryStatus.DELIVERY_COMPLETED) {
        throw new IllegalStateException("이미 배송완료된 상품은 취소 불가");
    }
    this.status = OrderStatus.ORDER_CANCELED;
    for (OrderItem item : orderItems) {
        item.cancel();
    }
}

// 서비스
@Transactional
public void cancelOrder(Long orderId) {
    Order order = orderRepository.findOne_Order(orderId);
    order.cancelOrder();  // 도메인 객체에 위임
}

//트랜잭션 스크립트 패턴
@Transactional
public void cancelOrder(Long orderId) {
    Order order = orderRepository.find(orderId);
    if (order.getDelivery().getStatus() == DeliveryStatus.DELIVERY_COMPLETED) {
        throw new IllegalStateException("이미 배송완료된 상품은 취소 불가");
    }
    order.setStatus(OrderStatus.ORDER_CANCELED);
    for (OrderItem item : order.getOrderItems()) {
        item.cancel();
    }
}
```

## 변경 감지와 병합(Merge)

### 준영속 앤티티

- **영속성 컨텍스트가 더 이상 관리하지 않는 엔티티**

### 병합

**병합 동작 방식**

1. merge()를 실행
2. 파라미터로 넘어온 준영속 엔티티(param)의 식별자 값으로 1차 캐시에서 영속 엔티티(findItem) 조회
    1. 만약 1차 캐시에 엔티티가 없으면 데이터 베이스에서 엔티티를 조회 후 1차 캐시에 저장
3. 조회된 영속 엔티티(findItem)에 준영속 엔티티(param)의 모든 값(필드)을 채워넣음
4. 값이 채워진 영속 상태의 객체를 반환 (merge()의 반환 값)

병합 주의할점

- merge()는 **“변경된 필드만” 업데이트**하는 게 아니라 **“모든 필드”를 복사**해서 갱신
- 만약 준영속 객체의 어떤 필드가 null이라도, 그 null 값도 같이 덮어써서 원래 있던 값이 사라질 수 있다는 
(위험성)

**merge보다 변경감지를 사용 해야하는 이유**

- `merge()`는 전체를 덮어써서 변경 지점을 추적하기 어려움
- 변경감지는 바뀐 필드만 감지하고 반영 → 변경 내역 추적 가능
- 불필요한 업데이트 쿼리 방지 → 성능 및 무결성 유

| **구분** | **병합(Merge)** | **변경 감지(Dirty Checking)** |
| --- | --- | --- |
| **작동 방식** | 새 값 전체 복사 | 변경된 필드만 추적 |
| **안전성** | 위험 (null 덮어쓰기 가능) | 안전 (바뀐 값만 반영) |
| **제어** | 전체 필드 복사 → 추적 어려움 | 원하는 필드만 변경 → 추적 쉬움 |
| **추천** | 정말 단순할 때만 | 대부분의 상황에서 권장 |