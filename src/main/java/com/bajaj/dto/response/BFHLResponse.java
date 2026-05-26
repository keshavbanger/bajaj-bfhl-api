package com.bajaj.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for POST /bfhl
 *
 * Example Output for input ["a", "1", "334", "4", "R", "$"]:
 * {
 *   "is_success": true,
 *   "user_id": "keshav_banger_01052006",
 *   "email": "Keshavbanger230815@acropolis.in",
 *   "roll_number": "0827cs231129",
 *   "even_numbers": ["334", "4"],
 *   "odd_numbers": ["1"],
 *   "alphabets": ["A", "R"],
 *   "special_characters": ["$"],
 *   "sum": "339",
 *   "concat_string": "Ra"
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BFHLResponse {

    @JsonProperty("is_success")
    private boolean isSuccess;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("roll_number")
    private String rollNumber;

    @JsonProperty("even_numbers")
    private List<String> evenNumbers;

    @JsonProperty("odd_numbers")
    private List<String> oddNumbers;

    @JsonProperty("alphabets")
    private List<String> alphabets;

    @JsonProperty("special_characters")
    private List<String> specialCharacters;

    @JsonProperty("sum")
    private String sum;

    @JsonProperty("concat_string")
    private String concatString;
}
