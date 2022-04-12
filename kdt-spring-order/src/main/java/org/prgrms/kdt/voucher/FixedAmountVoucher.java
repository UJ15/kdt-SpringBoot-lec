package org.prgrms.kdt.voucher;

import org.prgrms.kdt.voucher.Voucher;

import java.util.UUID;

public class FixedAmountVoucher implements Voucher {
    private static final long MAX_VOUCHER_AMOUNT = 10000;
    private final UUID voucherId;
    private final long amount;


    public FixedAmountVoucher(UUID voucherId, long amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount should be positive");
        if (amount == 0) throw new IllegalArgumentException("Amount should not be zero");
        if (amount > MAX_VOUCHER_AMOUNT) throw new IllegalArgumentException("Amount should be positive");

        this.voucherId = voucherId;
        this.amount = amount;
    }

    @Override
    public UUID getVoucherId() {
        return this.voucherId;
    }

    public long discount(long beforeDiscount){
        if (this.amount < 0) throw new IllegalArgumentException("Amount should be positive");
        return beforeDiscount - amount < 0 ? 0 : beforeDiscount - amount;
    }
}