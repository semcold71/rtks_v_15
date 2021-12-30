package ru.samcold.rtks.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import ru.samcold.rtks.domain.proxy.StatementProxy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "statements")
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@SelectBeforeUpdate
@Builder
public class Statement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Crane crane;

    @Column(name = "statement_date")
    private Date statementDate;

    @Column(name = "statement_number")
    private String statementNumber;

    @Column(name = "crane_purpose")
    private String cranePurpose;

    @Column(name = "report_conclusion")
    private String reportConclusion;

    @Column(name = "report_sign_date")
    private Date reportSignDate;

    @Column(name = "report_number")
    private String reportNumber;

    @Column(name = "next_date")
    private Date nextDate;

    @Column(name = "opo_name")
    private String opoName;

    @Column(name = "opo_address")
    private String opoAddress;

    @Column(name = "opo_danger_class")
    private String opoDangerClass;

    @Column(name = "opo_reg_number")
    private String opoRegNumber;

    public StringProperty customerNameProperty() {
        return new SimpleStringProperty(crane.getCustomer().getName());
    }

    public StringProperty craneNameProperty() {
        String result = "";
        result += crane.getName();
        result += !Objects.equals(crane.getZav(), "") && crane.getZav() != null ? " зав.№ " + crane.getZav() : "";
        result += !Objects.equals(crane.getReg(), "") && crane.getReg() != null ? " рег.№ " + crane.getReg() : "";
        return new SimpleStringProperty(result);
    }

    public Statement() {
        this.cranePurpose = "предназначен для погрузочно-разгрузочных работ";
        this.reportConclusion = "Соответствует требованиям промышленной безопасности.";
        this.opoDangerClass = "IV";
        this.opoRegNumber = "А-29-";
    }

    public Statement(StatementProxy proxy) {
        this.id = proxy.idProperty().get();
        this.statementDate = Date.valueOf(proxy.statementDateProperty().get());
        this.statementNumber = proxy.statementNumberProperty().get();
        this.cranePurpose = proxy.cranePurposeProperty().get();
        this.reportNumber = proxy.reportNumberProperty().get();
        this.reportSignDate = Date.valueOf(proxy.reportSignDateProperty().get());
        this.reportConclusion = proxy.reportConclusionProperty().get();
        this.opoName = proxy.opoNameProperty().get();
        this.opoAddress = proxy.opoAddressProperty().get();
        this.opoDangerClass = proxy.opoDangerClassProperty().get();
        this.opoRegNumber = proxy.opoRegNumberProperty().get();
        this.nextDate = Date.valueOf(proxy.nextDateProperty().get());
        this.crane = proxy.craneProperty().get();
    }
}
