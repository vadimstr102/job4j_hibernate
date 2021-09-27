package ru.job4j.lazy.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "brands")
public class BrandBi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<ModelBi> models = new ArrayList<>();

    public BrandBi() {
    }

    public BrandBi(String name) {
        this.name = name;
    }

    public void addModel(ModelBi model) {
        models.add(model);
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

    public List<ModelBi> getModels() {
        return models;
    }

    public void setModels(List<ModelBi> models) {
        this.models = models;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BrandBi brandBi = (BrandBi) o;
        return id == brandBi.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BrandBi{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
