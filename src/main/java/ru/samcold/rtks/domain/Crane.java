package ru.samcold.rtks.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import ru.samcold.rtks.domain.proxy.CraneProxy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cranes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate
@Builder
public class Crane implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String zav;
    private String reg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(targetEntity = Statement.class, mappedBy = "crane")
    @OrderBy(Statement_.STATEMENT_DATE)
    private List<Statement> statementList;

    @Override
    public String toString() {
        String result = "";
        result += name;
        result += !Objects.equals(zav, "") && zav != null ? " зав.№ " + zav : "";
        result += !Objects.equals(reg, "") && reg != null ? " рег.№ " + reg : "";
        return result;
    }

    public Crane(CraneProxy proxy) {
        this.id = proxy.idProperty().get();
        this.customer = proxy.customerProperty().get();
        this.name = proxy.nameProperty().get();
        this.zav = proxy.zavProperty().get();
        this.reg = proxy.regProperty().get();
    }

    public String zavAndReg() {
        String z =  zav != null ? "зав.№ " + zav : "";
        String r = reg != null ? "рег.№ " + reg : "";
        return z + " " + r;
    }
}
