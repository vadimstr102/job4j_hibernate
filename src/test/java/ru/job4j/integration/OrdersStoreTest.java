package ru.job4j.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrdersStoreTest {
    private final BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void deleteTable() throws SQLException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_002.sql")))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));

        List<Order> all = (List<Order>) store.findAll();

        assertEquals(all.size(), 1);
        assertEquals(all.get(0).getId(), 1);
        assertEquals(all.get(0).getName(), "name1");
        assertEquals(all.get(0).getDescription(), "description1");
    }

    @Test
    public void whenSaveOrdersAndFindById() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name2", "description2"));
        store.save(Order.of("name3", "description3"));

        Order order = store.findById(3);

        assertEquals(order.getId(), 3);
        assertEquals(order.getName(), "name3");
        assertEquals(order.getDescription(), "description3");
    }

    @Test
    public void whenSaveOrdersAndFindByNameOneRow() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name2", "description2"));
        store.save(Order.of("name3", "description3"));

        List<Order> all = (List<Order>) store.findByName("name2");

        assertEquals(all.size(), 1);
        assertEquals(all.get(0).getId(), 2);
        assertEquals(all.get(0).getName(), "name2");
        assertEquals(all.get(0).getDescription(), "description2");
    }

    @Test
    public void whenSaveOrdersAndFindByNameTwoRow() {
        OrdersStore store = new OrdersStore(pool);

        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name1", "description2"));
        store.save(Order.of("name3", "description3"));

        List<Order> all = (List<Order>) store.findByName("name1");

        assertEquals(all.size(), 2);
        assertEquals(all.get(0).getId(), 1);
        assertEquals(all.get(0).getName(), "name1");
        assertEquals(all.get(0).getDescription(), "description1");
        assertEquals(all.get(1).getId(), 2);
        assertEquals(all.get(1).getName(), "name1");
        assertEquals(all.get(1).getDescription(), "description2");
    }

    @Test
    public void whenSaveOrderAndUpdate() {
        OrdersStore store = new OrdersStore(pool);
        Order oldOrder = Order.of("name", "description");

        store.save(oldOrder);
        Order newOrder = new Order(oldOrder.getId(), "newName", "newDescription", oldOrder.getCreated());
        store.update(newOrder);

        Order order = store.findById(oldOrder.getId());

        assertEquals(order.getId(), oldOrder.getId());
        assertEquals(order.getName(), "newName");
        assertEquals(order.getDescription(), "newDescription");
    }
}
