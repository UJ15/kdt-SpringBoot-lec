package org.prgrms.kdt.voucher;

import org.prgrms.kdt.voucher.Voucher;

import java.util.UUID;

public class FixedAmountVoucher implements Voucher {
    private final UUID voucherId;
    private final long amount;


    public FixedAmountVoucher(UUID voucherId, long amount) {
        this.voucherId = voucherId;
        this.amount = amount;
    }

    @Override
    public UUID getVoucherId() {
        return this.voucherId;
    }

    public long discount(long beforeDiscount){
        return beforeDiscount - amount;
    }
}