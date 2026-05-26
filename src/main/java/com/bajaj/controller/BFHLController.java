package com.bajaj.controller;

import com.bajaj.dto.request.BFHLRequest;
import com.bajaj.dto.response.BFHLResponse;
import com.bajaj.service.BFHLService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller exposing the required BFHL endpoints:
 *
 *   POST /bfhl          → processes data array
 *   GET  /bfhl/health   → health check (nested)
 *   GET  /health        → health check (root - required by Bajaj form)
 */
@RestController
@CrossOrigin(origins = "*")
public class BFHLController {

    private final BFHLService bfhlService;

    public BFHLController(BFHLService bfhlService) {
        this.bfhlService = bfhlService;
    }

    /**
     * POST /bfhl — processes the input data array.
     */
    @PostMapping("/bfhl")
    public ResponseEntity<BFHLResponse> handlePost(@RequestBody BFHLRequest request) {
        BFHLResponse response = bfhlService.processData(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /health — root-level health check (required by Bajaj form).
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthRoot() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    /**
     * GET /bfhl/health — nested health check (also kept for compatibility).
     */
    @GetMapping("/bfhl/health")
    public ResponseEntity<Map<String, String>> healthNested() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
