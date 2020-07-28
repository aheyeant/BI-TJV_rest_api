package cz.cvut.fit.tjv.bi.semwork.semwork.data.model;


import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity(name = "storage")
//@NamedQuery(name = "findAllBooksByStorageId", query = "SELECT p.storedBooks FROM StorageController p WHERE p.id = :id")
public class StorageEntity {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "storage_book",
            joinColumns = @JoinColumn(name = "storage_name"),
            inverseJoinColumns = @JoinColumn(name = "book_name"))
    private List<BookEntity> storedBooks;

    public StorageEntity() {}

    public StorageEntity(String name, String location) {
        this.name = name;
        this.location = location;
        this.storedBooks = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<BookEntity> getStoredBooks() {
        return storedBooks;
    }

    public void setStoredBooks(List<BookEntity> storedBooks) {
        this.storedBooks = storedBooks;
    }
}
