package ru.job4j.carsandowners;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.carsandowners.model.Car;
import ru.job4j.carsandowners.model.Driver;
import ru.job4j.carsandowners.model.Engine;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Car car = new Car("Porsche");
            car.setEngine(new Engine("V8 TwinTurbo"));
            car.addDriver(new Driver("Ivan", "Ivanov"));
            car.addDriver(new Driver("Petr", "Petrov"));
            car.addDriver(new Driver("Nikolay", "Sidorov"));

            session.persist(car);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
