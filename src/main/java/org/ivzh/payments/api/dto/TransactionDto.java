package org.ivzh.payments.api.dto;

import org.ivzh.payments.model.account.Account;
import org.ivzh.payments.model.transaction.Transaction;
import org.ivzh.payments.model.transaction.TransactionState;
import org.ivzh.payments.model.transaction.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author ivzh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TransactionDto {

    private long id;
    private long sourceId;
    private Long destinationId;
    private BigDecimal ammount;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private TransactionType type;
    private TransactionState state;

    public TransactionDto() {
    }

    public TransactionDto(@NotNull Transaction transaction) {
        this.id = transaction.getId();
        this.date = transaction.getDate();
        this.type = transaction.getType();
        this.state = transaction.getState();
        this.ammount = transaction.getAmmount();
        this.sourceId = transaction.getSource().getId();

        Optional<Account> destination = transaction.getDestination();

        if (destination.isPresent()) {
            this.destinationId = destination.get().getId();
        } else {
            this.destinationId = null;
        }
    }

    public long getId() {
        return id;
    }

    public long getSourceId() {
        return sourceId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public BigDecimal getAmmount() {
        return ammount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionState getState() {
        return state;
    }
}
