package cz.cvut.fit.tjv.bi.semwork.semwork.rest.dto;

import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Objects;

public class StorageDto extends ResourceSupport implements Serializable {
    private String name;

    private String location;

    public StorageDto() {}

    public StorageDto(String name, String location) {
        this.name = name;
        this.location = location;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StorageDto that = (StorageDto) o;
        return name.equals(that.name) &&
                location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, location);
    }
}
