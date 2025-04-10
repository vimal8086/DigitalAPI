package com.one.digitalapi.service;

import com.one.digitalapi.entity.Discount;
import java.util.List;

public interface DiscountService {

    public Discount createDiscount(Discount discount);

    public Discount updateDiscount(Discount discount);

    public void deleteDiscount(Discount discount);

    public List<Discount> getAllDiscounts();
}
