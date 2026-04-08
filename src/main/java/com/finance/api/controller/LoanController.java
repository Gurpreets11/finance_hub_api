package com.finance.api.controller;

import com.finance.api.dto.request.LoanRequest;
import com.finance.api.dto.response.ApiResponse;
import com.finance.api.entity.Loan;
import com.finance.api.entity.User;
import com.finance.api.service.impl.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Loan>>> getAll(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(ApiResponse.success(
                loanService.getAll(currentUser.getId(), PageRequest.of(page, size))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Loan>> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(
                ApiResponse.success(loanService.getById(id, currentUser.getId())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Loan>> create(
            @Valid @RequestBody LoanRequest request,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Loan added",
                        loanService.create(request, currentUser)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Loan>> update(
            @PathVariable Long id,
            @Valid @RequestBody LoanRequest request,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(ApiResponse.success("Loan updated",
                loanService.update(id, request, currentUser.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        loanService.delete(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Loan deleted", null));
    }
}
