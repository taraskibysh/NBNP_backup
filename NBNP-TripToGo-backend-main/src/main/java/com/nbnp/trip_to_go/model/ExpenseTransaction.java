package com.nbnp.trip_to_go.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expense_transaction")
public class ExpenseTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_amount", precision = 10, scale = 2)
    private BigDecimal userAmount;

    @Column(name = "done")
    private Boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;

    public Integer getId() {
        return id;
    }

    public BigDecimal getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(BigDecimal userAmount) {
        this.userAmount = userAmount;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public AppUser getUser() {
        return user;
    }

    public Expense getExpense() {
        return expense;
    }
}
