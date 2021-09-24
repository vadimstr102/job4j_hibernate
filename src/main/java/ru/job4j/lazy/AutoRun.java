package ru.job4j.lazy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.lazy.model.BrandBi;
import ru.job4j.lazy.model.ModelBi;

import java.util.ArrayList;
import java.util.List;

public class AutoRun {
    public static void main(String[] args) {
        List<BrandBi> brands = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            BrandBi brand1 = new BrandBi("BMW");
            ModelBi model1 = new ModelBi("X5", brand1);
            ModelBi model2 = new ModelBi("M3", brand1);
            brand1.addModel(model1);
            brand1.addModel(model2);

            BrandBi brand2 = new BrandBi("LADA");
            ModelBi model3 = new ModelBi("Granta", brand2);
            ModelBi model4 = new ModelBi("Vesta", brand2);
            brand2.addModel(model3);
            brand2.addModel(model4);

            session.save(brand1);
            session.save(brand2);

            brands = session.createQuery("select distinct b from BrandBi b join fetch b.models").list();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        for (BrandBi brand : brands) {
            for (ModelBi model : brand.getModels()) {
                System.out.println(model);
            }
        }
    }
}
