package com.bajaj.service.impl;

import com.bajaj.dto.request.BFHLRequest;
import com.bajaj.dto.response.BFHLResponse;
import com.bajaj.service.BFHLService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service implementation that processes the BFHL data array.
 *
 * Rules:
 *  - Numbers   → classify even/odd; add to sum
 *  - Alphabets → uppercase list; reverse and alternating-cap for concat_string
 *  - Others    → special_characters list
 */
@Service
public class BFHLServiceImpl implements BFHLService {

    @Value("${app.user.first-name}")
    private String firstName;

    @Value("${app.user.last-name}")
    private String lastName;

    @Value("${app.user.dob}")
    private String dob;

    @Value("${app.user.email}")
    private String email;

    @Value("${app.user.roll-number}")
    private String rollNumber;

    @Override
    public BFHLResponse processData(BFHLRequest request) {
        List<String> data = request.getData();

        List<String> evenNumbers      = new ArrayList<>();
        List<String> oddNumbers       = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialChars     = new ArrayList<>();
        long total = 0;

        for (String item : data) {
            if (item == null || item.isEmpty()) continue;

            if (isNumber(item)) {
                long num = Long.parseLong(item);
                total += num;
                if (num % 2 == 0) {
                    evenNumbers.add(item);
                } else {
                    oddNumbers.add(item);
                }
            } else if (isAlphabet(item)) {
                alphabets.add(item.toUpperCase());
            } else {
                specialChars.add(item);
            }
        }

        // concat_string: reverse the alphabets list, then apply alternating caps
        List<String> reversed = new ArrayList<>(alphabets);
        Collections.reverse(reversed);
        String concatString = applyAlternatingCaps(String.join("", reversed));

        // user_id: firstname_lastname_ddmmyyyy (all lowercase)
        String userId = firstName.toLowerCase() + "_" + lastName.toLowerCase() + "_" + dob;

        return BFHLResponse.builder()
                .isSuccess(true)
                .userId(userId)
                .email(email)
                .rollNumber(rollNumber)
                .evenNumbers(evenNumbers)
                .oddNumbers(oddNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialChars)
                .sum(String.valueOf(total))
                .concatString(concatString)
                .build();
    }

    @Override
    public boolean isNumber(String s) {
        if (s == null || s.isEmpty()) return false;
        try {
            Long.parseLong(s.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean isAlphabet(String s) {
        if (s == null || s.isEmpty()) return false;
        return s.chars().allMatch(Character::isLetter);
    }

    @Override
    public String applyAlternatingCaps(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i % 2 == 0) {
                sb.append(Character.toUpperCase(s.charAt(i)));
            } else {
                sb.append(Character.toLowerCase(s.charAt(i)));
            }
        }
        return sb.toString();
    }
}
