package com.finance.api.service.impl;

import com.finance.api.dto.request.IncomeRequest;
import com.finance.api.entity.Income;
import com.finance.api.entity.User;
import com.finance.api.exception.ResourceNotFoundException;
import com.finance.api.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public Page<Income> getAll(Long userId, Pageable pageable) {
        return incomeRepository.findByUserIdOrderByTransactionDateDesc(userId, pageable);
    }

    public Income getById(Long id, Long userId) {
        return incomeRepository.findById(id)
                .filter(i -> i.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Income not found: " + id));
    }

    @Transactional
    public Income create(IncomeRequest request, User user) {
        Income income = Income.builder()
                .user(user)
                .title(request.getTitle())
                .category(request.getCategory())
                .amount(request.getAmount())
                .transactionDate(request.getTransactionDate())
                .description(request.getDescription())
                .isRecurring(request.getIsRecurring())
                .categoryEnum(request.getCategoryEnum() != null
                        ? request.getCategoryEnum() : Income.IncomeCategory.OTHER)
                .build();
        return incomeRepository.save(income);
    }

    @Transactional
    public Income update(Long id, IncomeRequest request, Long userId) {
        Income income = getById(id, userId);
        income.setTitle(request.getTitle());
        income.setCategory(request.getCategory());
        income.setAmount(request.getAmount());
        income.setTransactionDate(request.getTransactionDate());
        income.setDescription(request.getDescription());
        income.setIsRecurring(request.getIsRecurring());
        if (request.getCategoryEnum() != null) income.setCategoryEnum(request.getCategoryEnum());
        return incomeRepository.save(income);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        Income income = getById(id, userId);
        incomeRepository.delete(income);
    }
}
