package com.finance.api.controller;

import com.finance.api.dto.request.IncomeRequest;
import com.finance.api.dto.response.ApiResponse;
import com.finance.api.entity.Income;
import com.finance.api.entity.User;
import com.finance.api.service.impl.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    /** GET /api/v1/incomes?page=0&size=20 */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Income>>> getAll(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                ApiResponse.success(incomeService.getAll(currentUser.getId(), pageable)));
    }

    /** GET /api/v1/incomes/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Income>> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(
                ApiResponse.success(incomeService.getById(id, currentUser.getId())));
    }

    /** POST /api/v1/incomes */
    @PostMapping
    public ResponseEntity<ApiResponse<Income>> create(
            @Valid @RequestBody IncomeRequest request,
            @AuthenticationPrincipal User currentUser) {

        Income created = incomeService.create(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Income added", created));
    }

    /** PUT /api/v1/incomes/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Income>> update(
            @PathVariable Long id,
            @Valid @RequestBody IncomeRequest request,
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(
                ApiResponse.success("Income updated",
                        incomeService.update(id, request, currentUser.getId())));
    }

    /** DELETE /api/v1/incomes/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        incomeService.delete(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Income deleted", null));
    }
}
