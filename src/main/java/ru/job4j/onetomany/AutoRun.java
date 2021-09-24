package ru.job4j.onetomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.onetomany.model.Brand;
import ru.job4j.onetomany.model.Model;

public class AutoRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Brand brand = Brand.of("BMW");
            Model model1 = Model.of("X6");
            Model model2 = Model.of("M3");
            Model model3 = Model.of("M5");
            Model model4 = Model.of("M8");
            Model model5 = Model.of("Z4");

            brand.addModel(model1);
            brand.addModel(model2);
            brand.addModel(model3);
            brand.addModel(model4);
            brand.addModel(model5);

            session.save(brand);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
