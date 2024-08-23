# 토비의 스프링 6 - 이해와 원리
[토비의 스프링 6 - 이해와 원리](https://www.inflearn.com/course/%ED%86%A0%EB%B9%84%EC%9D%98-%EC%8A%A4%ED%94%84%EB%A7%816-%EC%9D%B4%ED%95%B4%EC%99%80-%EC%9B%90%EB%A6%AC?srsltid=AfmBOoomBCbPyYP1tay2c-4Z4yXHeQ_rf__MdFshlxqu0reG7y0GPCDN) 강의를 들으며 공부하는 내용을 업로드 합니다.
<br/>
<br/>
<hr>

# 강의자료 목차
- [섹션2-스프링 개발 시작하기](#섹션2-스프링-개발-시작하기)
    -[PaymentService](#PaymentService)
- [섹션3-오브젝트와 의존관계](#섹션3-오브젝트와-의존관계)
    - [오브젝트와 의존관계](#오브젝트와-의존관계)
    - [관심사의 분리](#관심사의-분리)
    - [상속을 통한 확장](#상속을-통한-확장)
    - [클래스의 분리](#클래스의-분리)
    - [인터페이스 도입](#인터페이스-도입)
    - [관계설정 책임의 분리](#관계설정-책임의-분리)
    - [오브젝트 팩토리](#오브젝트-팩토리)
    - [원칙과 패턴](#원칙과-패턴)


<br/>
<br/>
<hr>

# 섹션2-스프링 개발 시작하기
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

# 섹션3-오브젝트와 의존관계
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

<br/>

# 관심사 분리 과정
## 1. 메서드 추출

![image](https://github.com/user-attachments/assets/a890afb2-5bbc-42e7-833f-94d677408315)


<br/>

## 2. 상속을 통한 확장

![image](https://github.com/user-attachments/assets/23ee486c-a4af-4d06-8bce-2b0b31b26d20)

<br/>

⭐️ **PaymentService는 재사용**하면서 **내부**적인 **기능**은 **확장**하고 **변경**하도록 하는 방법? **클래스를 분리**한다. ⭐️

<br/>

# 클래스의 분리

![image](https://github.com/user-attachments/assets/092d945a-3e99-4ae2-86c8-28aad00585fe)


상속을 하는 것이 아니기 때문에 **의존관계**가 만들어지게 됨

<br/>

`PaymentService`

```java
public class PaymentService {
    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
        WebApiExRateProvider exRateProvider = new WebApiExRateProvider();
        BigDecimal exRate = exRateProvider.getWebExRate(currency);
        BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

        return new Payment(orderId, currency, foreginCurrencyAmount, exRate, convertedAmount, validUntil);
    }
}
```

환율이 필요하면 환율을 제공해주는 다른 클래스의 도움을 받아서 사용하도록 변경 됨

<br/>

추가적으로 수정을 해준다면 `prepare` 메서드가 호출될 때마다 객체가 만들어지게 코드가 작성되어 있으므로 한 번만 만들고 재사용하도록 인스턴스 변수로 변경

```java
public class PaymentService {
    private final WebApiExRateProvider exRateProvider;

    public PaymentService() {
        this.exRateProvider = new WebApiExRateProvider();
    }

    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
        BigDecimal exRate = exRateProvider.getWebExRate(currency);
        BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

        return new Payment(orderId, currency, foreginCurrencyAmount, exRate, convertedAmount, validUntil);
    }
}
```

<br/>

시간이 지나면서 변경, 확장을 하게되면 어떤일이 일어날까?

상속을 통해서 확장을 했을 때 기대한 점은 환율 정보를 가져오는 분리된 관심사와 책임이 변경되더라도 `PaymentService`가 변경이 일어나지 않도록 하는 것이였음

하지만 지금의 코드에서 `WebApiExRateProvider` 를 `SimpleExRateProvider`로 변경하려면

```java
    private final WebApiExRateProvider exRateProvider;vider;
```

```java
    public PaymentService() {
        this.exRateProvider = new WebApiExRateProvider();
    }
```

```java
    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
        BigDecimal exRate = exRateProvider.getWebExRate(currency);
				
				...
				
    }
```

생성자 내부의 객체 객체 생성 부분, 인스턴스 변수의 타입, 사용하는 쪽의 코드를 변경해 줘야됨

<br/>

```java
public class PaymentService {
    private final SimpleExRateProvider exRateProvider;

    public PaymentService() {
        this.exRateProvider = new SimpleExRateProvider();
    }

    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
        BigDecimal exRate = exRateProvider.getExRate(currency);
        BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

        return new Payment(orderId, currency, foreginCurrencyAmount, exRate, convertedAmount, validUntil);
    }
}
```

매번 변경을 해줘야 되기 때문에 상속보다 더 좋지 않음!!!

이러한 문제를 해결하기 위해서는 인터페이스를 도입하는 방법이 있음

<br/>

# 인터페이스 도입

## 1. 클래스 분리

![image](https://github.com/user-attachments/assets/a726452c-1b73-4e31-9989-2efee41c0a34)


클래스를 변경해야 할 때 `PaymentService` 에 관련된 기능들을 모두 건드려야되는 단점이 존재

해결하기위해서는 독립적인 인터페이스를 도입하면 됨

<br/>

# 인터페이스 도입

![image](https://github.com/user-attachments/assets/ad91c3fb-d54d-43d9-bb18-2817c4bb72a5)


인터페이스 타입으로 `PaymentService` 쪽에서 사용하게 만들면

메서드 이름이 달라진다고 `PaymentService`를 고쳐야되는 상황은 피할 수 있게 됨

`ExRateProvider`

```java
public interface ExRateProvicer {
    BigDecimal getWebExRate(String currency) throws IOException;
}
```

> 참고로 인터페이스는 기본적으로 모든 메서드가 public
> 

<br/>

`SimpleExRateProvider`

```java
public class SimpleExRateProvider implements ExRateProvider{
    @Override
    public BigDecimal getExRate(String currency) throws IOException {
        if (currency.equals("USD")) return BigDecimal.valueOf(1000);

        throw new IllegalArgumentException("지원되지 않는 통화입니다.");
    }
}
```

<br/>

`WebApiExRateProvicer`

```java
public class WebApiExRateProvider implements ExRateProvider{
    @Override
    public BigDecimal getExRate(String currency) throws IOException {
        URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = br.lines().collect(Collectors.joining());
        br.close();

        ObjectMapper mapper = new ObjectMapper();
        ExRateData data = mapper.readValue(response, ExRateData.class);
        return data.rates().get("KRW");
    }
}
```

<br/>

`PaymentService`

```java
public class PaymentService {
    private final ExRateProvider exRateProvider;

    public PaymentService() {
		    // this.exRateProvider = new SimpleExRateProvider();
        this.exRateProvider = new WebApiExRateProvider();
    }

    public Payment prepare(Long orderId, String currency, BigDecimal foreginCurrencyAmount) throws IOException {
        BigDecimal exRate = exRateProvider.getExRate(currency);
        BigDecimal convertedAmount = foreginCurrencyAmount.multiply(exRate);
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);

        return new Payment(orderId, currency, foreginCurrencyAmount, exRate, convertedAmount, validUntil);
    }
}
```

<br/>

인터페이스를 적용하게 되면

새로운 환율을 가져오는 코드를 추가하더라도 최소한의 부분만 수정하더라도 잘 작동하는 것을 위의 `PaymentService`를 통해 확인할 수 있었음

하나의 클래스에서 의존하는(사용하는) 다른 클래스들이 있을 때 그 안에 인터페이스를 정해두고 인터페이스 타입으로 사용하게 만들면

그 인터페이스를 구현한 다른 클래스를 추가할 때 적용해야되는 코드를 최소화 할 수 있음

**⇒ 다형성 적용**

<br/>

그러나 위의 코드는 상속을 통해서 구현한 것보다 못한 상황

`PaymentService`의 생성자에서 **환율을 가져오는 클래스에 강하게 의존**하고 있기 때문에 어쨋든 수정해야되는 상황임(강한 결합도)

<br/>

# 관계설정 책임의 분리

## 1. 인터페이스 도입

![image](https://github.com/user-attachments/assets/0603a18c-469e-4f9a-b5e3-d06d6b38ffb9)


디자인 패턴의 대부분이 인터페이스 구조로 이루어져 있음(70%)

<br/>

## 인터페이스를 사용했을 때 장점

1. PaymentService가 ExRateProvider에만 의존
    - ExRateProvider가 변경되면 PaymentService가 변경되고
    ExRateProvider가 변경되지않으면 PaymentService가 변경되지 않음

그러나

생성자 내부에서 구체적인 클래스로 명시해서 객체 생성 하는 경우

```java
    public PaymentService() {
		    // this.exRateProvider = new SimpleExRateProvider();
        this.exRateProvider = new WebApiExRateProvider();
    }
```

이와 같이 사용하게 되면 인터페이스가 아닌 **구체적인 클래스에 의존**하게 되어있음

![image](https://github.com/user-attachments/assets/74ee5d05-6988-4148-b46b-6fc578c00a34)


> 코드레벨의 의존관계

<br/>


`PaymentService`가 어떤 클래스의 Object를 사용하게 할 것인가가 “관계 설정”을 뜻 함

(의존하는 코드는 극히 적지만 클래스 레벨에 의존하고 있는 상황)

**런타임**에서는 `Object`가 `Object`를 의존하는 관계가 됨

![image](https://github.com/user-attachments/assets/4a5b0d57-bac5-494f-befd-0dd4ba4a2768)


> 오브젝트 다이어그램(런타임시 동작하는 구조를 보여줌)

<br/>

## 책임을 가지고 있는게 무엇인가?

![image](https://github.com/user-attachments/assets/9ff06c68-f2f3-4a42-a833-35e2911b4728)


`PaymentService`가 `WebApiExRateProvider`의 오브젝트를 사용하겠다고 결정하고 있기 때문에

책음을 가진 코드가 사용할 클래스가 변경되면 `PaymentService`가 변경되는 것은 당연함

<br/>

## 클래스가 변경되더라도 PaymentService를 수정하지않고 재사용하는 방법?

**⭐️ 의존관계를 설정하는 코드를 분리시킨다 ⭐️**

![image](https://github.com/user-attachments/assets/97c9d0b6-f0bb-4ab2-831f-6f4161fe0bb6)


> 책임을 앞단으로 보냄!

<br/>


`PaymentService`의 생성자

```java
public PaymentService(ExRateProvider exRateProvider) {
		// this.exRateProvider = new WebApiExRateProvider(); // 의존관계 설정하는 책임
    this.exRateProvider = exRateProvider;
}
```

<br/>

`Client`

```java
public class Client {
    public static void main(String[] args) throws IOException {
//        PaymentService paymentService = new PaymentService(new WebApiExRateProvider()); // 관계설정의 책임을 Client한테 넘김
        PaymentService paymentService = new PaymentService(new SimpleExRateProvider());
        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}
```

<br/>

## 관계설정 책임 분리 결론

![image](https://github.com/user-attachments/assets/f4e9be84-b9eb-4dc1-93f7-2b435f4d657f)


1. `Client`가 `PaymentService`에서 사용할 클래스를 결정
2. `Client`가 `PaymentService` 오브젝트를 만듦
3. 동시에 생성자를 통해서 앞서 만든 `ExRateProvicer` 인터페이스를 구현한 클래스의 오브젝트를 `PaymentService`에 전달
4. `Client`가 `PaymentService`의 `prepare()`를 호출
5. `WebApiExRateProvider`의 `getExRate()`를 사용하면서 기능 작동

<br/>

# 오브젝트 팩토리

![image](https://github.com/user-attachments/assets/8f00b827-148d-42f1-ad09-a571d2e810c6)


## Client로 관심사를 분리하는 경우 문제점

`Client`가 관심사를 2개 가지고 있음

1. `PaymentService`가 다른 클래스 오브젝트와 어떻게 관계를 맺어야 할지의 책임
2. `PaymentService`를 이용해서 업무를 수행하는 책임

```java
public class Client {
    public static void main(String[] args) throws IOException {
        // 책임 1. 관계 설정
        PaymentService paymentService = new PaymentService(new SimpleExRateProvider());
        // 책임 2. 업무 수행
        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}
```

<br/>

## 여러 관심사를 가지고 있는 경우 해결방법?

관심사를 다른 클래스로 넘기면 됨

![image](https://github.com/user-attachments/assets/55bb5f49-3f4e-491b-881b-14e9cb9ef354)


- `Client`는 `PaymentService`를 이용해서 작업을 하는데 충실한 코드로 구성
- `PaymentService`를 사용하기 위해서 준비를 시키려면 `ObjectFactory`를 사용
    - `ObjectFactory`를 사용해서 `PaymentService`가 사용할 `PaymentService`를 얻어옴

<br/>

<p>$\color{#DD6565}\text{⇒ 런타임 오브젝트 사이에 의존관계를 설정하는 책임을 ObjectFactory에서 넘기자}$</p>

<br/>

`ObjectFacroty`

```java
public class ObjectFactory {
    public PaymentService paymentService() {
        return new PaymentService(new SimpleExRateProvider());
    }
}
```

<br/>

`Client`

```java
public class Client {
    public static void main(String[] args) throws IOException {
        ObjectFactory objectFactory = new ObjectFactory();
        PaymentService paymentService = objectFactory.paymentService();

        // 책임 2. 업무 수행
        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}

```

<br/>

## 추가 개선

`ObjectFactory`는 현재 메서드 하나 안에 `PaymentService` 오브젝트도 만들고, `ExRateProvider` 인터페이스를 구현한 오브젝트도 만드는 2가지 관심사가 합쳐져 있음

<br/>

**변경 전**

```java
return new PaymentService(new SimpleExRateProvider());
```

<br/>

**변경 후**

```java
public class ObjectFactory {
    public PaymentService paymentService() {
        return new PaymentService(exRateProvider());
    }

    public ExRateProvider exRateProvider() {
        return new WebApiExRateProvider();
    }
}

```

이와 같이 변경하게 되면 `exRateProvider`의 구현 클래스를 변경하고 싶은 경우 빠르게 찾아갈 수 있음

**메서드의 이름이 분명하기 때문**

<br/>

각각의 오브젝트를 만드는 2개의 메서드로 분리 후

필요한 경우 메서드를 호출하여 오브젝트를 가져와 생성자에 주입하는 방식을 사용

<br/>

⇒ ObjectFactory에서 오브젝트를 만들때 하나의 오브젝트가 의존하는 다른 오브젝트가 있다면 정확하게 찾아와서 연결시켜주는 작업까지 수행해야 됨

```java
public class Client {
    public static void main(String[] args) throws IOException {
        ObjectFactory objectFactory = new ObjectFactory();
        PaymentService paymentService = objectFactory.paymentService();

        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}

```

<br/>

# 원칙과 패턴

- 원칙 : 객체지향 설계 원칙
    - 관심사의 분리
- 패천 : 객체지향 디자인 패턴

<br/>

# 객체지향 설계 원칙

1. 개방 폐쇄 원칙
2. 높은 응집도와 낮은 결합도
3. 전략 패턴
4. 제어의 역전

<br/>

## 개방 폐쇄 원칙     Open-Closed Principle(OCP)

- 클래스나 모듈은 확장에는 열려 있어야 하고 변경에는 닫혀 있어야 함
- 클래스가 기능을 확장할 때 클래스의 코드는 변경되지 않음
    - 환율 정보를 가져오는 방법을 확장할 때
    `PaymentService` 코드가 변경되지 않음
    (**전략 패턴**에도 개방 폐쇄 원칙이 잘 적용되어 있음)

<br/>

## 높은 응집도와 낮은 결합도     High Coherence and low coupling

- 응집도가 높다는 것은 하나의 모듈이 하나의 책임 또는 관심사에 집중되어있다는 뜻
    - 특징 : 변화가 일어날 때 해당 모듈에서 변하는 부분이 커짐
    - 장점 : 변화가 일어날 때 비용이 적게 듦
        - 응집도가 낮아서 하나의 모듈에서 변화가 일어날 때 일부분만 바뀌면
        바뀌지 않은 나머지 영역의 코드에 어떠한 영향을 주는지 검증이 필요 ⇒ 개발 비용 증가
- 책임과 관심사가 다른 모듈과는 낮은 결합도. 즉, 느슨하게 연결된 형태를 유지하는 것이 바람직
    - 코드레벨에서 결합도가 높다면 한가지 수정하는 경우 많은 부분이 따라서 고쳐져야 됨

<br/>

### 결합도가 낮은 코드

![image](https://github.com/user-attachments/assets/dd4ae5fb-67c7-4644-9157-4afd985c3650)


<br/>

### 결합도가 높은 코드

![image](https://github.com/user-attachments/assets/73ded978-ee5d-448f-a103-fe22bbbcb045)


<br/>


## 전략 패턴     Strategy Pattern

- 자신의 기능 맥락(Context)에서, 필요에 따라서 변경이 필요한 알고리즘을 인터페이스를 통해 통째로 외부로 분리시키고, 이를 구현한 구체적인 알고리즘 클래스를 필요에 따라 바꿔서 사용할 수 있게 하는 디자인 패턴
    
    ![image](https://github.com/user-attachments/assets/568d69fb-31ed-4f95-a2bf-c94f7ac25fdc)

    
    - 알고리즘을 대표하는 인터페이스를 정의
    - 인터페이스를 구현하는 클래스 작성
    - 어떤 클래스의 오브젝트를 사용할지는 자신이 결정하는 대신 `Clinet`가 주입해주는 방식
    
- context : `PaymentService`
- 전략 인터페이스 : `ExRateProvider`

<br/>

### 자바의 대표적인 전략 패턴 : Collections의 Sort

**숫자 ← 오름차순 정렬**

```java
public class Sort {
    public static void main(String[] args) {
        List<Integer> scores = Arrays.asList(5,7,1,9,2,8);
        Collections.sort(scores);

        scores.forEach(System.out::println);
    }
}
```

> 출력결과]
> 
> 
> 1
> 
> 2
> 
> 5
> 
> 7
> 
> 8
> 
> 9
> 

<br/>

**문자 ← 알파벳 순 정렬(기본)**

```java
public class Sort {
    public static void main(String[] args) {
        List<String> scores = Arrays.asList("z", "x", "spring", "java");
        Collections.sort(scores);

        scores.forEach(System.out::println);
    }
}
```

> 출력결과]
> 
> 
> java
> spring
> x
> z
> 

<br/>

**문자 ← 문자 길이 순서대로 변경하려는 경우(전략 변경)**

```java
public class Sort {
    public static void main(String[] args) {
        List<String> scores = Arrays.asList("z", "x", "spring", "java");
        Collections.sort(scores, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });

        scores.forEach(System.out::println);
    }
}
```

```java
public class Sort {
    public static void main(String[] args) {
        List<String> scores = Arrays.asList("z", "x", "spring", "java");
        Collections.sort(scores, (o1, o2) -> o1.length() - o2.length());

        scores.forEach(System.out::println);
    }
}
```

> 출력결과]
> 
> 
> z
> x
> java
> spring
> 

위와 같이 **전략 패턴**은 **프로그램을 실행하는 도중 전략을 변경하면서 사용할 수 있음**

<br/>

## 제어의 역전     Inversion of Control

- 제어권 이전을 통한 제어관계 역전 - 프레임워크의 기본 동작 원리
- 제어권이 누구한테 있었는가? 제어권이 어디로 이전했는가? 를 파악하면 됨

 
<br/>


### 제어권 보유 이전 과정을 살펴보자

1. **제어권 보유 : PaymentService**

![image](https://github.com/user-attachments/assets/1e2692ea-235c-4d93-82f1-408f3e871eda)


`PaymentService`가 어떤 종류의 환율 정보를 이용할 것인지 결정하는 권한을 `new 클래스()`로 가지고 있었음

<br/>

2. **제어권 보유 : Client**

![image](https://github.com/user-attachments/assets/dc9b808e-5eec-4a2d-a9fd-8d09ceddd50c)


<br/>

3. **제어권 보유 : ObjectFactory**

![image](https://github.com/user-attachments/assets/4a2cdcb8-6b2b-4589-b501-5f0137dad33d)


<br/>

**⚠️ 주의 : FrameWork에서 말하는 제어의 역전과 다름**

**내가 권한을 가지고 작업을 하던 과정이 다른 쪽으로 넘어갔다고 이해하면 됨**
