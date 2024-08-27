package tobyspring.hellospring.api;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

public class ApiTemplate {
    private final ApiExcutor apiExcutor;
    private final ExRateExtractor exRateExtractor;

    public ApiTemplate() {
        this.apiExcutor = new HttpClientApiExecutor();
        this.exRateExtractor = new ErApiExRateExtractor();
    }

    public ApiTemplate(ApiExcutor apiExcutor, ExRateExtractor exRateExtractor) {
        this.apiExcutor = apiExcutor;
        this.exRateExtractor = exRateExtractor;
    }

    // url만 전달하고 싶은 경우
    public BigDecimal getForExRate(String url) {
        return this.getForExRate(url, this.apiExcutor, this.exRateExtractor);
    }

    public BigDecimal getForExRate(String url, ApiExcutor apiExcutor) {
        return this.getForExRate(url, apiExcutor, this.exRateExtractor);
    }

    public BigDecimal getForExRate(String url, ExRateExtractor exRateExtractor) {
        return this.getForExRate(url, this.apiExcutor, exRateExtractor);
    }

    // 콜백을 직접 지정하고 싶은 경우
    public BigDecimal getForExRate(String url, ApiExcutor apiExcutor, ExRateExtractor exRateExtractor) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        String response;
        try {
            response = apiExcutor.execute(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            return exRateExtractor.extract(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
