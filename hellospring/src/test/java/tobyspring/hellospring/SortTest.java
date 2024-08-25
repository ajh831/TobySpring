package tobyspring.hellospring;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SortTest {
    Sort sort;

    @BeforeEach // 각 테스트를 실행할 때 beforeEach가 테서트 메서드 마다 수행될 것
    void beaforeEach() {
        sort = new Sort(); // 인스턴스 생성
        System.out.println(this);
    }

    @Test
    void sort() {
        // 1. 테스트를 실행하기 위한 준비 (given)
//        Sort sort = new Sort(); // 인스턴스 생성

        // 2. 기능 실행 (when)
        List<String> list = sort.sortByLength(Arrays.asList("aa", "b"));

        // 3. 검증(실행결과 확인) (then)
        Assertions.assertThat(list).isEqualTo(Arrays.asList("b", "aa"));
    }

    @Test
    void sort3Itemns() {
//        Sort sort = new Sort(); // 인스턴스 생성

        List<String> list = sort.sortByLength(Arrays.asList("aa", "ccc", "b"));

        Assertions.assertThat(list).isEqualTo(Arrays.asList("b", "aa", "ccc"));
    }

    @Test
    void sortAlreadySorted() {
//        Sort sort = new Sort(); // 인스턴스 생성

        List<String> list = sort.sortByLength(Arrays.asList("b", "aa", "ccc"));

        Assertions.assertThat(list).isEqualTo(Arrays.asList("b", "aa", "ccc"));
    }
}