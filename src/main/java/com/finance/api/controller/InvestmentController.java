package com.finance.api.controller;

import com.finance.api.dto.request.InvestmentRequest;
import com.finance.api.dto.response.ApiResponse;
import com.finance.api.entity.Investment;
import com.finance.api.entity.User;
import com.finance.api.service.impl.InvestmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/investments")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Investment>>> getAll(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(ApiResponse.success(
                investmentService.getAll(currentUser.getId(), PageRequest.of(page, size))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Investment>> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(
                ApiResponse.success(investmentService.getById(id, currentUser.getId())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Investment>> create(
            @Valid @RequestBody InvestmentRequest request,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Investment added",
                        investmentService.create(request, currentUser)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Investment>> update(
            @PathVariable Long id,
            @Valid @RequestBody InvestmentRequest request,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(ApiResponse.success("Investment updated",
                investmentService.update(id, request, currentUser.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        investmentService.delete(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Investment deleted", null));
    }
}
