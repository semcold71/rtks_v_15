package ru.samcold.rtks.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import ru.samcold.rtks.domain.proxy.OpoProxy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "opo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate
@Builder
public class Opo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "opo_name")
    private String name;

    @Column(name = "opo_address")
    private String address;

    @Column(name = "danger_class")
    private String dangerClass;

    @Column(name = "reg_number")
    private String regNumber;

    @Column(name = "note")
    private String note;

    /**
     * Скан Свидетельства о регистрации ОПО и/или Карты объекта
     */
    @Lob
    @Column(name = "opo_doc")
    private byte[] opoDoc;

    /**
     * Скан страхового полиса
     */
    @Lob
    @Column(name = "policy_doc")
    private byte[] policyDoc;

    public Opo(OpoProxy proxy) {
        this.id = proxy.idProperty().get();
        this.customer = proxy.customerProperty().get();
        this.name = proxy.nameProperty().get();
        this.address = proxy.addressProperty().get();
        this.dangerClass = proxy.dangerClassProperty().get();
        this.regNumber = proxy.regNumberProperty().get();
        this.note = proxy.noteProperty().get();

        this.opoDoc = proxy.opoDocProperty().get();
        this.policyDoc = proxy.policyDocProperty().get();
    }
}
