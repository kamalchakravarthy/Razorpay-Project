package com.project.RazorPay.common;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Money {
    private Integer amountUnits;
    private String currency;

    public static Money of(int moneyUnits, String currency){
        return new Money(moneyUnits, currency);
    }

    public static Money inr(int amountUnits){
        return new Money(amountUnits, "INR");
    }

    public Money add(Money other){
        if(!this.currency.equals(other.currency)){
            throw new IllegalArgumentException("Cannot Add money with different currency");
        }
        return new Money(this.amountUnits + other.amountUnits, this.currency);
    }

    public Money subtract(Money other){
        if(!this.currency.equals(other.currency)){
            throw new IllegalArgumentException("Cannot Subtract money with different currency");
        }
        return new Money(this.amountUnits - other.amountUnits, this.currency);
    }

}
