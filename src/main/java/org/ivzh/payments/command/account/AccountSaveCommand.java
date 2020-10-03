package org.ivzh.payments.command.account;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

/**
 * @author ivzh
 */
public class AccountSaveCommand {

    @NotBlank(message = "Account number is missing")
    private String number;

    AccountSaveCommand() {
        super();
    }

    public AccountSaveCommand(@NotBlank(message = "Account number is missing") String number) {
        this.number = number;
    }

    @Schema(description = "The number of the account to be created")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "AccountSaveCommand{" +
                "number='" + number + '\'' +
                '}';
    }
}
