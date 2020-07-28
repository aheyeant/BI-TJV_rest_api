package cz.cvut.fit.tjv.bi.semwork.semwork.data.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "customer")
//@NamedQuery(name = "findAllBooksByCustomerId", query = "SELECT p.purchasedBooks FROM CustomerEntity p WHERE p.id = :id")
public class CustomerEntity {
    @Id
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "customer_book",
            joinColumns = @JoinColumn(name = "customer_login"),
            inverseJoinColumns = @JoinColumn(name = "book_name"))
    private List<BookEntity> purchasedBooks;

    public CustomerEntity() {}

    public CustomerEntity(String login, String name, String surname) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.purchasedBooks = new LinkedList<>();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<BookEntity> getPurchasedBooks() {
        return purchasedBooks;
    }

    public void setPurchasedBooks(List<BookEntity> purchasedBooks) {
        this.purchasedBooks = purchasedBooks;

    }
}
