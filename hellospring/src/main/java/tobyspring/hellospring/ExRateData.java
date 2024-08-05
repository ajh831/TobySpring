package tobyspring.hellospring;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Map;

// record : 생성자처럼 사용됨. 한번 값을 저장하면 수정할 수 없음(불변)
@JsonIgnoreProperties(ignoreUnknown = true) // JSON에서 없는 Property에 대한 설정시 에러 무시
public record ExRateData(String result, Map<String, BigDecimal> rates) {

}
