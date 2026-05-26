package com.bajaj.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for POST /bfhl
 *
 * Example:
 * {
 *   "data": ["a", "1", "334", "4", "R", "$"]
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BFHLRequest {

    @JsonProperty("data")
    private List<String> data;
}
