package com.finance.api.service.impl;

import com.finance.api.dto.request.LoanRequest;
import com.finance.api.entity.Loan;
import com.finance.api.entity.User;
import com.finance.api.exception.ResourceNotFoundException;
import com.finance.api.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;

    public Page<Loan> getAll(Long userId, Pageable pageable) {
        return loanRepository.findByUserIdOrderByEmiDueDateAsc(userId, pageable);
    }

    public Loan getById(Long id, Long userId) {
        return loanRepository.findById(id)
                .filter(l -> l.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found: " + id));
    }

    @Transactional
    public Loan create(LoanRequest request, User user) {
        Loan loan = Loan.builder()
                .user(user)
                .name(request.getName())
                .type(request.getType())
                .totalAmount(request.getTotalAmount())
                .outstandingAmount(request.getOutstandingAmount())
                .emiAmount(request.getEmiAmount())
                .interestRate(request.getInterestRate())
                .emiDueDate(request.getEmiDueDate())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .lenderName(request.getLenderName())
                .build();
        return loanRepository.save(loan);
    }

    @Transactional
    public Loan update(Long id, LoanRequest request, Long userId) {
        Loan loan = getById(id, userId);
        loan.setName(request.getName());
        loan.setType(request.getType());
        loan.setTotalAmount(request.getTotalAmount());
        loan.setOutstandingAmount(request.getOutstandingAmount());
        loan.setEmiAmount(request.getEmiAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setEmiDueDate(request.getEmiDueDate());
        loan.setStartDate(request.getStartDate());
        loan.setEndDate(request.getEndDate());
        loan.setLenderName(request.getLenderName());
        return loanRepository.save(loan);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        loanRepository.delete(getById(id, userId));
    }
}
