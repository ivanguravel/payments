package org.ivzh.payments.command.transaction;

import org.ivzh.payments.model.transaction.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * @author ivzh
 */
public final class TransactionSaveCommand {

    @Positive(message = "Source account ID must be greater than xero")
    @NotNull(message = "Transaction must have a source account")
    private long sourceId;
    private long destinationId;
    @Positive(message = "Transaction ammount must be greater than zero")
    private BigDecimal ammount;
    @NotNull(message = "Transaction must have a type")
    private TransactionType type;

    public TransactionSaveCommand(long sourceId, long destinationId, BigDecimal ammount, TransactionType type) {
        this.destinationId = destinationId;
        this.sourceId = sourceId;
        this.ammount = ammount;
        this.type = type;
    }

    public long getSourceId() {
        return sourceId;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public BigDecimal getAmmount() {
        return ammount;
    }

    public TransactionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TransactionSaveCommand{" +
                "sourceId=" + sourceId +
                ", destinationId=" + destinationId +
                ", ammount=" + ammount +
                ", type=" + type +
                '}';
    }
}
