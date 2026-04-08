package com.finance.api.service.impl;

import com.finance.api.dto.request.InvestmentRequest;
import com.finance.api.entity.Investment;
import com.finance.api.entity.User;
import com.finance.api.exception.ResourceNotFoundException;
import com.finance.api.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final InvestmentRepository investmentRepository;

    public Page<Investment> getAll(Long userId, Pageable pageable) {
        return investmentRepository.findByUserIdOrderByCurrentValueDesc(userId, pageable);
    }

    public Investment getById(Long id, Long userId) {
        return investmentRepository.findById(id)
                .filter(i -> i.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Investment not found: " + id));
    }

    @Transactional
    public Investment create(InvestmentRequest request, User user) {
        Investment investment = Investment.builder()
                .user(user)
                .name(request.getName())
                .type(request.getType())
                .investedAmount(request.getInvestedAmount())
                .currentValue(request.getCurrentValue())
                .returnPercentage(calcReturn(request.getInvestedAmount(), request.getCurrentValue()))
                .startDate(request.getStartDate())
                .maturityDate(request.getMaturityDate())
                .notes(request.getNotes())
                .build();
        return investmentRepository.save(investment);
    }

    @Transactional
    public Investment update(Long id, InvestmentRequest request, Long userId) {
        Investment inv = getById(id, userId);
        inv.setName(request.getName());
        inv.setType(request.getType());
        inv.setInvestedAmount(request.getInvestedAmount());
        inv.setCurrentValue(request.getCurrentValue());
        inv.setReturnPercentage(calcReturn(request.getInvestedAmount(), request.getCurrentValue()));
        inv.setStartDate(request.getStartDate());
        inv.setMaturityDate(request.getMaturityDate());
        inv.setNotes(request.getNotes());
        return investmentRepository.save(inv);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        investmentRepository.delete(getById(id, userId));
    }

    private BigDecimal calcReturn(BigDecimal invested, BigDecimal current) {
        if (invested == null || invested.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return current.subtract(invested)
                .divide(invested, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
