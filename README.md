# java-convenience-store-precourse
구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 편의전 결제 시스템 구현

<details> 
  <summary>과제 및 프로그래밍 요구사항</summary>
  
  ## 과제 요구사항
  * Git 커밋 단위는 기능 목록 단위로 추가
  * Angular JS Git Commit Message Conventions

  ## 프로그래밍 요구사항
  * JDK 21버전
  * Application main()으로 프로그래밍 시작
  * build.gradle 파일 변경 불가
  * 외부 라이브러리 사용 불가
    * camp.nextstep.edu.missionutils에서 제공하는 DateTimes 및 Console API를 사용하여 구현해야 한다.
       * 현재 날짜와 시간을 가져오려면 camp.nextstep.edu.missionutils.DateTimes의 now()를 활용한다.
       * 사용자가 입력하는 값은 camp.nextstep.edu.missionutils.Console의 readLine()을 활용한다.
  * 자바 코드 컨벤션 준수
  * 기존 파일, 패키지명 등 이동 및 이름 변경 등 불가
  
  ## 프로그래밍 요구사항 2
  * 기능 분석한 내용이 정상 작동하는지 JUnit, AssertJ를 이용해 테스트 코드로 확인
  * indent(인덴트, 들여쓰기) depth를 3이 넘지 않도록 구현한다. 2까지만 허용한다.
  * 3항 연산자를 쓰지 않는다.
  * 함수(또는 메서드)가 한 가지 일만 하도록 최대한 작게 작성
    * 함수(또는 메서드)의 길이가 15라인을 넘어가지 않도록 구현한다.
    * 함수(또는 메서드)가 한 가지 일만 잘 하도록 구현한다.
  * JUnit 5와 AssertJ를 이용하여 정리한 기능 목록이 정상적으로 작동하는지 테스트 코드로 확인
  
  ## 프로그래밍 요구사항 3
  * else 예약어를 쓰지 않는다.
  * Java Enum을 적용하여 프로그램을 구현한다.
  * 구현한 기능에 대한 단위 테스트를 작성한다. 단, UI(System.out, System.in, Scanner) 로직은 제외한다.

  ## 프로그래밍 요구사항 4
  * 함수 길이가 10을 넘지 않도록 구현, 한 가지 일만 잘 하도록 구현
  * 입출력 담당 클래스를 별도로 구현(InputView, OutputView)
  * 클래스 이름, 메소드 반환 유형, 시그니처 등은 자유롭게 수정 가능
  
</details>

## 기능 목록
- 인사말과 안내 멘트 출력
  - `안녕하세요. W편의점입니다.`
  - `현재 보유하고 있는 상품입니다.`
 - 상품별 프로모션 적용 확인
    - 오늘 날짜가 프로모션 기간 내에 포함된 경우 프로모션 진행
      - 프로모션 재고가 있을 때만 가능, 결재시 프로모션 재고 우선 차감
    - 지정 상품을 지정 갯수만큼 구매하는 경우에만 증정(1회 적용 가능)
    - 프로모션 3개 종류는 아래와 같다. (모두 N개 구매 시 1개 무료 증정의 형태)
    ```text
      name,buy,get,start_date,end_date
      탄산2+1,2,1,2024-01-01,2024-12-31
      MD추천상품,1,1,2024-01-01,2024-12-31
      반짝할인,1,1,2024-11-01,2024-11-30
    ```
- 현재 보유중인 상품명, 가격, 재고, 프로모션 이름 공백 구분 출력
  - 재고가 0개이면 `재고 없음` 출력
- 안내 멘트 출력
  - `구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])`
- 사용자 입력값에 대한 처리 진행
  - 프로모션 있는 상품의 경우
    - 프로모션 혜택 안내 메세지 출력 & 사용자 입력 처리
      - `현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)`
      - Y : 프로모션 혜택 충족하는 조건으로 선택 상태 변경
        ```text
        - 프로모션 미적용 금액의 30% 할인
        - 최대 한도 8,000원      
        ```
      - N : 계속 진행
    - 프로모션 불가 안내 메세지 출력 & 사용자 입력 처리
      - `현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)`
      - Y : 해당 상품 수량에 대해 정가로 결제 진행
      - N : 해당 상품 수량 제외하도록 선택 상태 변경
  - 멤버십 할인 적용 여부 안내 메세지 출력 & 사용자 입력 처리
    - `멤버십 할인을 받으시겠습니까? (Y/N)`
    - Y : 멤버십 할인 적용
      ```text
        - 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
        - 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
        - 멤버십 할인의 최대 한도는 8,000원이다.
      ``` 
    - N : 멤버십 할인 미적용
- 영수증 출력
  - 구매 상품 내역 (상품명 수량 금액)
  - 증정 상품 내역 (상품명 수량)
  - 금액 정보 (총구매액, 행사할인, 멤버십할인, 내실돈)
  ```text
    ===========W 편의점=============
    상품명		수량	금액
    콜라		3 	3,000
    에너지바 		5 	10,000
    ===========증	정=============
    콜라		1
    ==============================
    총구매액		8	13,000
    행사할인			-1,000
    멤버십할인			-3,000
    내실돈			 9,000
  ``` 
- 추가 구매 여부 확인 안내 메세지 출력 & 사용자 입력 처리
  - `감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)`
  - Y : 구매 후 상품 재고 반영하여 프로그램 재실행
  - N : 프로그램 종료
   
## 설계
```text
  com.store
  ├── domain
  │   ├── Promotion.java        // 프로모션을 관리하는 도메인 객체
  │   ├── Product.java           // 일반 상품을 관리하는 도메인 객체
  │   └── PromotionProduct.java   // 프로모션 상품을 정의하는 도메인 객체
  ├── service
  │   ├── ReceiptService.java      // 영수증 로직을 처리하는 서비스 클래스
  │   ├── InventoryService.java  // 상품 재고 처리 로직을 처리하는 서비스 클래스
  │   ├── MemberDiscountService.java  // 멤버십 할인 로직을 처리하는 서비스 클래스
  │   └── PurchaseService.java   // 구매 로직을 처리하는 서비스 클래스
  ├── controller
  │   └── StoreController.java   // 사용자 입력을 처리하고 결과를 반환
  └── View
      ├── InputView.java    // 사용자 입력 관련 처리
      └── OutputView.java  // 콘솔 출력 관련 처리
```
