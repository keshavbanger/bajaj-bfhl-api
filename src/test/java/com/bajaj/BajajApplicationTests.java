package com.bajaj;

import com.bajaj.controller.BFHLController;
import com.bajaj.dto.request.BFHLRequest;
import com.bajaj.dto.response.BFHLResponse;
import com.bajaj.service.BFHLService;
import com.bajaj.service.impl.BFHLServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BajajApplicationTests {

    @Autowired
    private BFHLController controller;

    @Autowired
    private BFHLService service;

    // ─── Helper ───────────────────────────────────────────────────────────────

    private BFHLRequest req(String... items) {
        return new BFHLRequest(Arrays.asList(items));
    }

    // ─── Context Load ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Spring context loads successfully")
    void contextLoads() {
        assertThat(controller).isNotNull();
        assertThat(service).isNotNull();
    }

    // ─── isNumber ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("isNumber: valid integer strings return true")
    void isNumber_validIntegers() {
        assertThat(service.isNumber("1")).isTrue();
        assertThat(service.isNumber("334")).isTrue();
        assertThat(service.isNumber("0")).isTrue();
        assertThat(service.isNumber("-5")).isTrue();
    }

    @Test
    @DisplayName("isNumber: non-numeric strings return false")
    void isNumber_nonNumeric() {
        assertThat(service.isNumber("a")).isFalse();
        assertThat(service.isNumber("$")).isFalse();
        assertThat(service.isNumber("")).isFalse();
        assertThat(service.isNumber(null)).isFalse();
    }

    // ─── isAlphabet ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("isAlphabet: single letters return true")
    void isAlphabet_singleLetters() {
        assertThat(service.isAlphabet("a")).isTrue();
        assertThat(service.isAlphabet("R")).isTrue();
        assertThat(service.isAlphabet("Z")).isTrue();
    }

    @Test
    @DisplayName("isAlphabet: non-letters or multi-char strings return false")
    void isAlphabet_nonLetters() {
        assertThat(service.isAlphabet("1")).isFalse();
        assertThat(service.isAlphabet("$")).isFalse();
        assertThat(service.isAlphabet("ab")).isFalse();
        assertThat(service.isAlphabet("")).isFalse();
        assertThat(service.isAlphabet(null)).isFalse();
    }

    // ─── applyAlternatingCaps ─────────────────────────────────────────────────

    @Test
    @DisplayName("applyAlternatingCaps: alternates upper/lower from index 0")
    void applyAlternatingCaps_basic() {
        assertThat(service.applyAlternatingCaps("ra")).isEqualTo("Ra");
        assertThat(service.applyAlternatingCaps("abc")).isEqualTo("AbC");
    }

    @Test
    @DisplayName("applyAlternatingCaps: single char is uppercased")
    void applyAlternatingCaps_singleChar() {
        assertThat(service.applyAlternatingCaps("a")).isEqualTo("A");
    }

    @Test
    @DisplayName("applyAlternatingCaps: empty string returns empty")
    void applyAlternatingCaps_empty() {
        assertThat(service.applyAlternatingCaps("")).isEmpty();
    }

    // ─── processData: canonical example ───────────────────────────────────────

    @Test
    @DisplayName("processData: canonical input [a, 1, 334, 4, R, $]")
    void processData_canonical() {
        BFHLRequest request = req("a", "1", "334", "4", "R", "$");
        BFHLResponse resp = service.processData(request);

        assertThat(resp.isSuccess()).isTrue();
        assertThat(resp.getEvenNumbers()).containsExactly("334", "4");
        assertThat(resp.getOddNumbers()).containsExactly("1");
        assertThat(resp.getAlphabets()).containsExactlyInAnyOrder("A", "R");
        assertThat(resp.getSpecialCharacters()).containsExactly("$");
        assertThat(resp.getSum()).isEqualTo("339");
        // alphabets found: A, R → reversed: R, A → joined "RA" → alternating: "Ra"
        assertThat(resp.getConcatString()).isEqualTo("Ra");
    }

    // ─── processData: only numbers ────────────────────────────────────────────

    @Test
    @DisplayName("processData: only numbers")
    void processData_onlyNumbers() {
        BFHLResponse resp = service.processData(req("2", "3", "10"));

        assertThat(resp.getEvenNumbers()).containsExactly("2", "10");
        assertThat(resp.getOddNumbers()).containsExactly("3");
        assertThat(resp.getAlphabets()).isEmpty();
        assertThat(resp.getSpecialCharacters()).isEmpty();
        assertThat(resp.getSum()).isEqualTo("15");
        assertThat(resp.getConcatString()).isEmpty();
    }

    // ─── processData: only alphabets ──────────────────────────────────────────

    @Test
    @DisplayName("processData: only alphabets")
    void processData_onlyAlphabets() {
        BFHLResponse resp = service.processData(req("b", "A", "z"));

        assertThat(resp.getAlphabets()).containsExactly("B", "A", "Z");
        assertThat(resp.getEvenNumbers()).isEmpty();
        assertThat(resp.getOddNumbers()).isEmpty();
        assertThat(resp.getSum()).isEqualTo("0");
        // alphabets: B, A, Z → reversed: Z, A, B → "ZAB" → alternating: "ZaB"
        assertThat(resp.getConcatString()).isEqualTo("ZaB");
    }

    // ─── processData: empty list ───────────────────────────────────────────────

    @Test
    @DisplayName("processData: empty data list returns zeroed response")
    void processData_emptyList() {
        BFHLResponse resp = service.processData(new BFHLRequest(Collections.emptyList()));

        assertThat(resp.isSuccess()).isTrue();
        assertThat(resp.getEvenNumbers()).isEmpty();
        assertThat(resp.getOddNumbers()).isEmpty();
        assertThat(resp.getAlphabets()).isEmpty();
        assertThat(resp.getSpecialCharacters()).isEmpty();
        assertThat(resp.getSum()).isEqualTo("0");
        assertThat(resp.getConcatString()).isEmpty();
    }

    // ─── User ID format ───────────────────────────────────────────────────────

    @Test
    @DisplayName("processData: user_id is in correct format firstname_lastname_ddmmyyyy")
    void processData_userIdFormat() {
        BFHLResponse resp = service.processData(req("1"));
        // format: lowercase_firstname_lowercase_lastname_dob
        assertThat(resp.getUserId()).matches("[a-z]+_[a-z]+_\\d{8}");
    }
}
