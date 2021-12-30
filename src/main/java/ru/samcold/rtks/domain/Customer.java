package ru.samcold.rtks.domain;

import lombok.*;
import org.hibernate.annotations.*;
import ru.samcold.rtks.domain.proxy.CustomerProxy;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate
@Builder
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String name2;
    private String address;
    private String address2;
    private String inn;
    private String kpp;
    private String ogrn;
    private String rs;
    private String bank;
    private String bik;
    private String ks;
    private String boss;
    private String post;
    private String phone;
    private String fax;
    private String email;
    private String web;
    private String note;

    // fetch = FetchType.LAZY - не обязательно, это свойство по умолчанию для @OneToMany
    @OneToMany(targetEntity = Crane.class, mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Crane> craneList;

    @OneToMany(targetEntity = Contract.class, mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Contract> contractList;

    @OneToMany(targetEntity = Opo.class, mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Opo> opoList;

    @Override
    public String toString() {
        return name;
    }

    public Customer(CustomerProxy customerProxy) {
        id = customerProxy.idProperty().get();
        name = customerProxy.nameProperty().get();
        name2 = customerProxy.name2Property().get();
        address = customerProxy.addressProperty().get();
        address2 = customerProxy.address2Property().get();
        inn = customerProxy.innProperty().get();
        kpp = customerProxy.kppProperty().get();
        ogrn = customerProxy.ogrnProperty().get();
        rs = customerProxy.rsProperty().get();
        bank = customerProxy.bankProperty().get();
        bik = customerProxy.bikProperty().get();
        ks = customerProxy.ksProperty().get();
        boss = customerProxy.bossProperty().get();
        post = customerProxy.postProperty().get();
        phone = customerProxy.phoneProperty().get();
        fax = customerProxy.faxProperty().get();
        email = customerProxy.emailProperty().get();
        web = customerProxy.webProperty().get();
        note = customerProxy.noteProperty().get();
    }

    public String shortBossName() {

        String fullName = boss;

        String[] arr = fullName.split(" ");

        if (arr.length == 3 && !fullName.endsWith(".")) {

            String firstName = arr[1];
            String lastName = arr[0];
            String middleName = arr[2];

            String fn = firstName.charAt(0) + ".";
            String mn = middleName.charAt(0) + ".";

            return lastName + " " + fn + mn;
        }

        return fullName;
    }

    public String nameForOrder() {

        String res = "";

        res += name;
        res += !inn.isEmpty() && inn != null ? ", ИНН " + inn : "";
        res += !kpp.isEmpty() && kpp != null ? ", КПП " + kpp : "";
        res += !address.isEmpty() && address != null ? ", " + address : "";

        return res;
    }

}
