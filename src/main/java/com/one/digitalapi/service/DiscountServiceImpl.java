package com.one.digitalapi.service;

import com.one.digitalapi.entity.Discount;
import com.one.digitalapi.exception.DiscountException;
import com.one.digitalapi.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public Discount createDiscount(Discount discount) throws DiscountException {
        return discountRepository.save(discount);
    }

    @Override
    public Discount updateDiscount(Discount discount) throws DiscountException {
        return discountRepository.save(discount);
    }

    @Override
    public void deleteDiscount(Discount discount) throws DiscountException {
        discountRepository.delete(discount);
    }

    @Override
    public List<Discount> getAllDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findByEndDateAfter(now);
    }
}