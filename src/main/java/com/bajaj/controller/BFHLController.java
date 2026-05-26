package com.bajaj.controller;

import com.bajaj.dto.request.BFHLRequest;
import com.bajaj.dto.response.BFHLResponse;
import com.bajaj.service.BFHLService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller exposing the two required BFHL endpoints:
 *
 *   POST /bfhl  → processes data array
 *   GET  /health → health check
 */
@RestController
@RequestMapping("/bfhl")
@CrossOrigin(origins = "*")
public class BFHLController {

    private final BFHLService bfhlService;

    public BFHLController(BFHLService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * Processes the input data array and returns categorized results.
     *
     * @param request JSON body with "data" array
     * @return 200 OK with BFHLResponse
     */
    @PostMapping
    public ResponseEntity<BFHLResponse> handlePost(@RequestBody BFHLRequest request) {
        BFHLResponse response = bfhlService.processData(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint.
     *
     * @return {"status": "UP"}
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
