# 토비의 스프링 6 - 이해와 원리
토비의 스프링 6 - 이해와 원리 강의를 들으며 공부하는 내용을 업로드 합니다.

<br/>
<br/>
<hr>

# 강의자료 목차
- [섹션1-스프링 개발 시작하기](#섹션1-스프링-개발-시작하기)
    - [PaymentService](#PaymentService)
- [섹션2-오브젝트와 의존관계](#섹션2-오브젝트와-의존관계)
    - [오브젝트와 의존관계](#오브젝트와-의존관계)
    - [관심사의 분리](#관심사의-분리)
    - [상속을 통한 확장](#상속을-통한-확장)
<br/>
<br/>
<hr>

# 섹션1-스프링 개발 시작하기
# PaymentService
## 요구사항

- 해외직구를 위한 원화 결제 준비 기능 개발
- 주문번호, 외국 통화 종류, 외국 통화 기준 결제 금액을 전달 받아서 다음의 정보를 더해 Payment를 생성한다
    - 적용 환율
    - 원화 환산 금액
    - 원화 환산 금액 유효시간
- PaymentService.prepare() 메소드로 개발
    - Payment 오브젝트 리턴
    
<br/>

## 개발방법

- 빠르게 완성해서 가장 간단한 방법을 찾는다
- 작성한 코드가 동작하는지 확인하는 방법을 찾는다
- 조금씩 기능을 추가하고 다시 검증한다
- 코드를 한눈에 이해하기 힘들다면 코멘트로 설명을 달아준다

<br/>

### 환율 가져오기

- https://open.er-api.com/v6/lastest/{기준통화} 이용
    - 이 서비스가 더이상 유지되지 않는 경우 사용할 다른 서비스 URL을 강의자료에서 확인
    - JSON 포맷으로 리턴되는 값을 분석해서 원화(KRW) 환율 값을 가져온다
    - JSON 자바 오브젝트로 변환
        - Jackson 프로젝트의 ObjectMapper 사용

<br/>
<br/>
<hr>

# 섹션2-오브젝트와 의존관계
# 오브젝트와 의존관계
## 오브젝트

- OOP, 객체, 클래스?

<br/>

## 클래스와 오브젝트

- **오브젝트**? 프로그램을 실행하면 만들어져서 동작하는 것.
- **클래스**? 오브젝트를 만들어내기 위해서 필요한 것. 우리가 작성하는 코드(청사진, 설계도)

<br/>

## 클래스의 인스턴스 = 오브젝트

### Class Instance

- **인스턴스**? 추상적인 것에 대한 실체
    - 클래스를 가지고 실체화한 것
- 자바에서는 배열(Array)도 오브젝트

<br/>

## 의존관계

### Dependency

- A → B : A가 B에 의존한다.

<br/>

### 의존관계 2가지 관점

1. **Class 사이의 의존관계(Class 레벨의 의존관계, Code 레벨의 의존관계)**
    
    ![image](https://github.com/user-attachments/assets/e276b855-03ec-45c7-92f5-b1457afc0b84)

    - Client의 기능이 제대로 동작하려면 Supplier가 필요
    - Client가 Supplier를 사용, 호출, 생성, 인스턴스화, 전송
    - Supplier가 변경되면 Client 코드가 영향을 받음
        - 코드레벨에서 바로 알 수 있음
        의존하고 있는 코드가 변경 되면 나의 코드도 바뀔 가능성이 높음

<br/>
        
2. **오브젝트 사이의 의존관계**
    - 프로그램을 실행하는 런타임 환경에서 의존관계가 만들어짐

<br/>

**⭐️  클래스 레벨의 의존관계와 런타임 레벨의 의존관계가 다를 수 있음‼️ ⭐️**

<br/>
<br/>

# 관심사의 분리
## 코드 개선 방법

1. 기능을 낫게 만드는 방법
    - 기능 추가 또는 삭제

<br/>

2. 기능은 건들지 않고 내부 코드 구조 개션하는 방법(<span>$\color{#DD6565}\text{리팩토링}$</span>)

<br/>

## 주석을 삭제

주석이 나쁘다는 것이 아니지만 주석을 보지 않더라도 코드를 읽으면 이해할 수 있게 만들라는 뜻

<br/>

**⇒ 코드를 작성할 때 주석이 필요할 때 달아두었다가 더이상 주석이 없더라도 코드를 이해하는 데 필요가 없다면 제거를 해주는 게 좋음**

```java
// 금액 계산
BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);

// 유효 시간 계산
LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30); // 30분 더하기
```

- 금액 계산
    - `foreginCurrencyAmount` : 외환 금액
    - `.multiply(exRate)` : 환율 곱하기
    - `convertedAmount` : 전환된 금액
- 유효 시간 계산
    - `LocalDateTime.now()` : 현재 시간
    - `.plusMinutes(30)` : 30분 더하기
    - `validUntil` : 계산된 유효 시간
    

⇒ 위의 2가지 내용은 코드만 보더라도 어떤 내용인지 파악이 가능하기 때문에 주석을 제거함

```java
BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);
```

<br/>
<br/>

## 관심사의 분리

### Separation of Concerns(SoC)

```java
// 환율 가져오기
// https://open.er-api.com/v6/latest/USD
URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
HttpURLConnection connection = (HttpURLConnection) url.openConnection();
BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
String response = br.lines().collect(Collectors.joining()); // lines() : BufferedReader에서 들어오는걸 Stream 타입으로 계속 가져오게 할 수 있는 것(java8 이후)
br.close();

// ObjectMapper : JSON 컨텐츠를 Java 객체로 역직렬롸 하거나 Java 객체를 JSON으로 직렬화 할 때 사용
ObjectMapper mapper = new ObjectMapper(); // ObjectMapper : Jackson 라이브러리의 클래스
ExRateData data = mapper.readValue(response, ExRateData.class);
BigDecimal exRate = data.rates().get("KRW");
System.out.println(exRate);

BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);
```

- 기존에 작성한 PayemtService에서는 큰 관심사가 두가지 들어음
    1. 환율을 가져오는 것
    2. 가져온 환율을 계산하여 유효시간을 적용하여 Payment를 반환
    
    ⇒ 코드를 읽고 이해하는 데 기술적인 내용과 비즈니스(업무 내용)이 혼재가 될 수 있음

<br/>
  
- 관심사는 변경이라는 관점으로 설명할 수 있음
    1. 환율을 가져오는 것
        - 기술적인 이유 또는 환율을 어떻게 가져올 수 있는 가에 대한 메커니즘이 달라지면 변경될 것
    2. 가져온 환율을 계산하여 유효시간을 적용하여 Payment를 반환
        - 서비스 로직과 관련된 부분이 바뀌면 변경될 것
    
    ⇒ 변경의 이유와 시점이 다른 코드를 같이 두면 좋지 않음 → 분리!!!!!
    

<br/>

### 분리하는 가장 쉬운 방법?

1. 메서드 분리(메서드 추출)
    
    > 인텔리제이 맥북 단축키 : opt + cmd + m
    > 
    
    **분리 전**
    
    ```java
    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
      // 환율 가져오기
      // https://open.er-api.com/v6/latest/USD
      URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String response = br.lines().collect(Collectors.joining()); // lines() : BufferedReader에서 들어오는걸 Stream 타입으로 계속 가져오게 할 수 있는 것(java8 이후)
      br.close();
    
      // ObjectMapper : JSON 컨텐츠를 Java 객체로 역직렬롸 하거나 Java 객체를 JSON으로 직렬화 할 때 사용
      ObjectMapper mapper = new ObjectMapper(); // ObjectMapper : Jackson 라이브러리의 클래스
      ExRateData data = mapper.readValue(response, ExRateData.class);
      BigDecimal exRate = data.rates().get("KRW");
      System.out.println(exRate);
    
      BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
      LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);
    
      return new Payment(orderId, currency, foreginCurrencyAmount, exRate, convertedAmount, validUntil);
    }
    ```

   <br/>
    
    **분리 후**
    
    ```java
    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
        BigDecimal exRate = getExRate(currency);
        BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);
    
        return new Payment(orderId, currency, foreginCurrencyAmount, exRate, convertedAmount, validUntil);
    }
    
    // 메서드로 분리
    private static BigDecimal getExRate(String currency) throws IOException {
        URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = br.lines().collect(Collectors.joining()); // lines() : BufferedReader에서 들어오는걸 Stream 타입으로 계속 가져오게 할 수 있는 것(java8 이후)
        br.close();
    
        // ObjectMapper : JSON 컨텐츠를 Java 객체로 역직렬롸 하거나 Java 객체를 JSON으로 직렬화 할 때 사용
        ObjectMapper mapper = new ObjectMapper(); // ObjectMapper : Jackson 라이브러리의 클래스
        ExRateData data = mapper.readValue(response, ExRateData.class);
        BigDecimal exRate = data.rates().get("KRW");
        System.out.println(exRate);
        return exRate;
    }
    ```
    
<br/>
<br/>

# 상속을 통한 확장
## 메서드 분리 작업을 한 코드

```java
public class PaymentService {
    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
        BigDecimal exRate = getExRate(currency);
        BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

        return new Payment(orderId, currency, foreginCurrencyAmount, exRate, convertedAmount, validUntil);
    }

    private static BigDecimal getExRate(String currency) throws IOException {
        URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = br.lines().collect(Collectors.joining()); // lines() : BufferedReader에서 들어오는걸 Stream 타입으로 계속 가져오게 할 수 있는 것(java8 이후)
        br.close();

        // ObjectMapper : JSON 컨텐츠를 Java 객체로 역직렬롸 하거나 Java 객체를 JSON으로 직렬화 할 때 사용
        ObjectMapper mapper = new ObjectMapper(); // ObjectMapper : Jackson 라이브러리의 클래스
        ExRateData data = mapper.readValue(response, ExRateData.class);
        BigDecimal exRate = data.rates().get("KRW");
        System.out.println(exRate);
        return exRate;
    }
    
    public static void main(String[] args) throws IOException {
        PaymentService paymentService = new PaymentService();
        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}
```

그러나 클래스의 관점으로 바라봤을 때 아직 두개의 다른 관심사를 가지고 있음

⇒ 변경이 되어야하는 시점과 이유가 다르기 때문에 클래스 밖으로 분리해야 될 필요가 있음

<br/>

**클래스 밖으로 분리해야 되는 이유?**

1. **재사용 관점**
- 한 번 만들어진 자기 기능에 충실한 코드가 있다면 소스코드를 건드리지 않더라도 그대로 사용할 수 있어야 됨
- `PaymentService`는 환율을 가져오는 방식, 정책 등 매커니즘이 변경되면 계속해서 고쳐야 되기 때문에 클래스 밖으로 분리해야 되는 것

<br/>

1. **확장성 관점**
- 사용하는 측에서 환율 정보를 가져오는 방법이 각각 다른경우 `prepare`메서드를 각각 고쳐서 사용하는 것이 아닌 환율정보를 가져오는 코드를 밖으로 분리해내고 `PaymetService` 클래스는 변경이 되지 않아도 `getExRate()`가 바뀌어도 상관이 없도록 변경해야 됨
- **상속**을 이용

<br/>

## 상속

기존의 코드를 건들지 않아도 기능을 확장해서 사용할 수 있도록 해줌

> 상속을 통해 유연한 확장을 하는 대표적인 디자인 패턴 :
TempleteMethod Pattern, FactoryMethod Pattern
> 

**변경 전**

```java
private static BigDecimal getExRate(String currency) throws IOException {
    URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String response = br.lines().collect(Collectors.joining()); // lines() : BufferedReader에서 들어오는걸 Stream 타입으로 계속 가져오게 할 수 있는 것(java8 이후)
    br.close();

    // ObjectMapper : JSON 컨텐츠를 Java 객체로 역직렬롸 하거나 Java 객체를 JSON으로 직렬화 할 때 사용
    ObjectMapper mapper = new ObjectMapper(); // ObjectMapper : Jackson 라이브러리의 클래스
    ExRateData data = mapper.readValue(response, ExRateData.class);
    BigDecimal exRate = data.rates().get("KRW");
    System.out.println(exRate);
    return exRate;
}
```

<br/>

**변경 후**

```java
abstract BigDecimal getExRate(String currency) throws IOException;
```

`getExRate`메서드의 내용을 `WebApiExRatePaymentService`로 분리

```java
public class WebApiExRatePaymentService extends PaymentService {

    @Override
    BigDecimal getExRate(String currency) throws IOException {
        URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = br.lines().collect(Collectors.joining());
        br.close();

        ObjectMapper mapper = new ObjectMapper();
        ExRateData data = mapper.readValue(response, ExRateData.class);
        BigDecimal exRate = data.rates().get("KRW");
        System.out.println(exRate);
        return exRate;
    }
}
```

`PaymentService`

```java
abstract public class PaymentService {
    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
        BigDecimal exRate = getExRate(currency);
        BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

        return new Payment(orderId, currency, foreginCurrencyAmount, exRate, convertedAmount, validUntil);
    }

    abstract BigDecimal getExRate(String currency) throws IOException;

    public static void main(String[] args) throws IOException {
        PaymentService paymentService = new WebApiExRatePaymentService();
        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}
```
<br/>

### 환율을 가져오는 시스템을 변경하고 싶다면?

`PaymentService`를 상속받아서 구현하도록 하면 됨

- 환율 정책이 변경된 경우 테스트
    
    `SimpleExRatePaymentService`
    
    ```java
    public class SimpleExRatePaymentService extends PaymentService {
        @Override
        BigDecimal getExRate(String currency) throws IOException {
            if (currency.equals("USD")) return BigDecimal.valueOf(1000);
    
            throw new IllegalArgumentException("지원되지 않는 통화입니다.");
        }
    }
    
    ```
    
    `Client` ← Client 측만 변경해 주면 됨
    
    ```java
    public class Client {
        public static void main(String[] args) throws IOException {
            PaymentService paymentService = new SimpleExRatePaymentService();
            Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
            System.out.println(payment);
        }
    }
    ```
    
    ![image](https://github.com/user-attachments/assets/3d011faa-cbb9-4b80-b7fd-2c326d6b63f4)

    
<br/>

## PaymetService의 문제점

`absrtract` 메서드가 추가가 된다면?

- 상속하는 클래스들이 2개의 메서드를 구현해야 됨 ⇒ 수많은 조합들이 만들어져야 됨
- 구현에 따라서 클래스명이 너무 길어지게 됨

<br/>

**⇒ 장기적인 관점으로 바라봤을 때 상속을 통한 확장은 `PaymentService`에 적합하지 않음**
