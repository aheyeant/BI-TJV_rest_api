package cz.cvut.fit.tjv.bi.semwork.semwork.rest.dto;

import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Objects;

public class BookDto extends ResourceSupport implements Serializable {
    private String name;

    private double price;

    public BookDto() {}

    public BookDto(String name, double price) {
        this.name = name;
        this.price = price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookDto bookDto = (BookDto) o;
        return Double.compare(bookDto.price, price) == 0 &&
                name.equals(bookDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, price);
    }
}
