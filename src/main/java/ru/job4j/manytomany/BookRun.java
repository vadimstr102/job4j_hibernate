package ru.job4j.manytomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.manytomany.model.Author;
import ru.job4j.manytomany.model.Book;

public class BookRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Author author1 = Author.of("Лев Толстой");
            Author author2 = Author.of("Илья Ильф");
            Author author3 = Author.of("Евгений Петров");
            Author author4 = Author.of("Александр Пушкин");

            Book book1 = Book.of("Война и мир");
            Book book2 = Book.of("12 стульев");
            Book book3 = Book.of("Капитанская дочка");
            Book book4 = Book.of("Медный всадник");

            book1.addAuthor(author1);
            book2.addAuthor(author2);
            book2.addAuthor(author3);
            book3.addAuthor(author4);
            book4.addAuthor(author4);

            session.persist(book1);
            session.persist(book2);
            session.persist(book3);
            session.persist(book4);

            Book book = session.get(Book.class, 2);
            session.remove(book);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
