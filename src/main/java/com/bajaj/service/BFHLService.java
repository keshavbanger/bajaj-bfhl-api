package com.bajaj.service;

import com.bajaj.dto.request.BFHLRequest;
import com.bajaj.dto.response.BFHLResponse;

/**
 * Service interface for the BFHL API.
 *
 * Defines all operations for processing the data array and producing the response.
 */
public interface BFHLService {

    /**
     * Processes the input data array and returns a structured response.
     *
     * Processing rules:
     * - Numbers   : strings that parse as integers → classify as even/odd + sum them
     * - Alphabets : strings that are single letters → convert to uppercase
     * - Special   : all other strings (e.g. "$", "@")
     * - concat_string: reverse the order of alphabets found, then apply alternating caps
     *
     * @param request the input request containing the data array
     * @return a fully populated BFHLResponse
     */
    BFHLResponse processData(BFHLRequest request);

    /**
     * Determines whether a given string is a valid integer.
     *
     * @param s input string
     * @return true if parseable as integer
     */
    boolean isNumber(String s);

    /**
     * Determines whether a given string is a single alphabetic character.
     *
     * @param s input string
     * @return true if single letter
     */
    boolean isAlphabet(String s);

    /**
     * Applies alternating case to a string.
     * Index 0 → uppercase, Index 1 → lowercase, Index 2 → uppercase, ...
     *
     * @param s input string
     * @return string with alternating case applied
     */
    String applyAlternatingCaps(String s);
}
