package ru.samcold.rtks.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import ru.samcold.rtks.domain.proxy.WorkProxy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "works")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@SelectBeforeUpdate
@Builder
public class Work implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int num;
    private String description;
    private int count;
    private String unit;
    private double price;
    private double total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Override
    public String toString() {
        return "Work{" +
                "contract=" + contract.getId() +
                ", id=" + id +
                ", num=" + num +
                ", description='" + description + '\'' +
                ", count=" + count +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                ", total=" + total +
                '}';
    }

    public Work(Contract contract) {
        this.contract = contract;
    }

    public Work(WorkProxy workProxy) {
        this.id = workProxy.idProperty().get();
        this.contract = workProxy.contractProperty().get();
        this.num = workProxy.numProperty().get();
        this.description = workProxy.descriptionProperty().get();
        this.count = workProxy.countProperty().get();
        this.unit = workProxy.unitProperty().get();
        this.price = workProxy.priceProperty().get();
        this.total = workProxy.totalProperty().get();
    }
}
