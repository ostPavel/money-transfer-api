package com.revolut.transfer.vo;

import com.revolut.transfer.enums.Currency;
import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNTS")
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    long accountNumber; // using db IDs as account numbers for simplicity sake

    @Column(name = "BALANCE")
    BigDecimal balance;

    @Column(name = "CURRENCY")
    @Enumerated(value = EnumType.STRING)
    Currency currency;

}
