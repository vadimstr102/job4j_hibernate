package ru.job4j.lazy.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "models")
public class ModelBi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandBi brand;

    public ModelBi() {
    }

    public ModelBi(String name, BrandBi brand) {
        this.name = name;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BrandBi getBrand() {
        return brand;
    }

    public void setBrand(BrandBi brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModelBi modelBi = (ModelBi) o;
        return id == modelBi.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ModelBi{" + "id=" + id + ", name='" + name + '\'' + ", brand=" + brand + '}';
    }
}
