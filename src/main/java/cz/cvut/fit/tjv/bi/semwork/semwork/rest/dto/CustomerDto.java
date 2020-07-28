package cz.cvut.fit.tjv.bi.semwork.semwork.rest.dto;

import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Objects;

public class CustomerDto extends ResourceSupport implements Serializable {
    private String login;

    private String name;

    private String surname;

    public CustomerDto() {}

    public CustomerDto(String login, String name, String surname) {
        this.login = login;
        this.name = name;
        this.surname = surname;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CustomerDto that = (CustomerDto) o;
        return login.equals(that.login) &&
                name.equals(that.name) &&
                surname.equals(that.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, name, surname);
    }
}
