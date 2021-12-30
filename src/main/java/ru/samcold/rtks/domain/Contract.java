package ru.samcold.rtks.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import ru.samcold.rtks.domain.proxy.ContractProxy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate
@Builder
public class Contract implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String number;
    private Date date = Date.valueOf(LocalDate.now());
    private double price;
    private String start = getRussianMonth(0);
    private String finish = getRussianMonth(3);
    private String note;
    private int prepay = 100;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(targetEntity = Work.class, mappedBy = "contract", fetch = FetchType.LAZY)
    private List<Work> workList;

    public Contract(ContractProxy proxy) {
        this.id = proxy.idProperty().get();
        this.customer = proxy.customerProperty().get();
        this.number = proxy.numberProperty().get();
        this.date = Date.valueOf(proxy.dateProperty().get());
        this.price = proxy.priceProperty().get();
        this.start = proxy.startProperty().get();
        this.finish = proxy.finishProperty().get();
        this.note = proxy.noteProperty().get();
        this.prepay = proxy.prepayProperty().get();
    }

    public void filler(ContractProxy proxy) {
        this.id = proxy.idProperty().get();
        this.customer = proxy.customerProperty().get();
        this.number = proxy.numberProperty().get();
        this.date = Date.valueOf(proxy.dateProperty().get());
        this.price = proxy.priceProperty().get();
        this.start = proxy.startProperty().get();
        this.finish = proxy.finishProperty().get();
        this.note = proxy.noteProperty().get();
        this.prepay = proxy.prepayProperty().get();
    }

    public void clear() {
        setId(0);
        setNumber("");
        setDate(Date.valueOf(LocalDate.now()));
        setPrice(0.0);
        setStart("");
        setFinish("");
        setNote("");
        setPrepay(0);
    }

    private String getRussianMonth(int offset) {

        Map<Integer, String> months = new HashMap<>();
        months.put(1, "Январь");
        months.put(2, "Февраль");
        months.put(3, "Март");
        months.put(4, "Апрель");
        months.put(5, "Май");
        months.put(6, "Июнь");
        months.put(7, "Июль");
        months.put(8, "Август");
        months.put(9, "Сентябрь");
        months.put(10, "Октябрь");
        months.put(11, "Ноябрь");
        months.put(12, "Декабрь");

        int month = LocalDate.now().plusMonths(offset).getMonthValue();
        int year = LocalDate.now().plusMonths(offset).getYear();

        return months.get(month) + " " + year;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", date=" + date +
                '}';
    }
}
