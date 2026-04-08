package com.finance.api.controller;

import com.finance.api.dto.request.ExpenseRequest;
import com.finance.api.dto.response.ApiResponse;
import com.finance.api.entity.Expense;
import com.finance.api.entity.User;
import com.finance.api.service.impl.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Expense>>> getAll(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(ApiResponse.success(
                expenseService.getAll(currentUser.getId(), PageRequest.of(page, size))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(
                ApiResponse.success(expenseService.getById(id, currentUser.getId())));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Expense>> create(
            @Valid @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Expense added",
                        expenseService.create(request, currentUser)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> update(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(ApiResponse.success("Expense updated",
                expenseService.update(id, request, currentUser.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        expenseService.delete(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Expense deleted", null));
    }
}
