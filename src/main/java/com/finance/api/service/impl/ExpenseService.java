package com.finance.api.service.impl;

import com.finance.api.dto.request.ExpenseRequest;
import com.finance.api.entity.Expense;
import com.finance.api.entity.User;
import com.finance.api.exception.ResourceNotFoundException;
import com.finance.api.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public Page<Expense> getAll(Long userId, Pageable pageable) {
        return expenseRepository.findByUserIdOrderByTransactionDateDesc(userId, pageable);
    }

    public Expense getById(Long id, Long userId) {
        return expenseRepository.findById(id)
                .filter(e -> e.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found: " + id));
    }

    @Transactional
    public Expense create(ExpenseRequest request, User user) {
        Expense expense = Expense.builder()
                .user(user)
                .title(request.getTitle())
                .category(request.getCategory())
                .amount(request.getAmount())
                .transactionDate(request.getTransactionDate())
                .description(request.getDescription())
                .categoryEnum(request.getCategoryEnum() != null
                        ? request.getCategoryEnum() : Expense.ExpenseCategory.OTHER)
                .build();
        return expenseRepository.save(expense);
    }

    @Transactional
    public Expense update(Long id, ExpenseRequest request, Long userId) {
        Expense expense = getById(id, userId);
        expense.setTitle(request.getTitle());
        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setTransactionDate(request.getTransactionDate());
        expense.setDescription(request.getDescription());
        if (request.getCategoryEnum() != null) expense.setCategoryEnum(request.getCategoryEnum());
        return expenseRepository.save(expense);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        expenseRepository.delete(getById(id, userId));
    }
}
