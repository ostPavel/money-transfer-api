package com.revolut.transfer.vo;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.UUID;
import com.revolut.transfer.enums.Currency;

@Entity
@Table(name = "TRANSACTIONS")
@Data
public class Transaction {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    UUID id;

    @Column(name = "T_FROM")
    long fromAccNumber; // account number to withdraw money from

    @Column(name = "T_TO")
    long toAccNumber; // account number to transfer money to

    @Column(name = "T_AMOUNT")
    @NotNull
    BigDecimal amount;

    @Column(name = "T_CURRENCY")
    @Enumerated(value = EnumType.STRING)
    Currency currency; // transaction currency is determined by "from" account

    @Column(name = "T_STATUS")
    boolean success;

}
