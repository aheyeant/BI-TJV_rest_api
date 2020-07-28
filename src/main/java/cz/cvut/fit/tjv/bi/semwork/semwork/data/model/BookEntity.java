package cz.cvut.fit.tjv.bi.semwork.semwork.data.model;


import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "book")
public class BookEntity {
    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private double price;

    @ManyToMany(mappedBy = "storedBooks")
    private List<StorageEntity> storages;

    @ManyToMany(mappedBy = "purchasedBooks")
    private List<CustomerEntity> customers;

    public BookEntity() {}

    public BookEntity(String name) {
        this.name = name;
        this.price = 0;
        this.storages = new LinkedList<>();
        this.customers = new LinkedList<>();
    }

    public BookEntity(String name, double price) {
        this.name = name;
        this.price = price;
        this.storages = new LinkedList<>();
        this.customers = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<StorageEntity> getStorages() {
        return storages;
    }

    public void setStorages(List<StorageEntity> storages) {
        this.storages = storages;
    }

    public List<CustomerEntity> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerEntity> customers) {
        this.customers = customers;
    }
}
