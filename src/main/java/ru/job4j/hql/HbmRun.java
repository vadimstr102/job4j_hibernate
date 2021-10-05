package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.hql.model.Candidate;

import java.util.List;
import java.util.function.Function;

public class HbmRun {
    public static void main(String[] args) {
        HbmRun hbmRun = new HbmRun();
        Candidate one = new Candidate("Oleg", 5, 500_000);
        Candidate two = new Candidate("Andrey", 3, 300_000);
        Candidate three = new Candidate("Vadim", 1, 100_000);

        hbmRun.saveCandidate(one);
        hbmRun.saveCandidate(two);
        hbmRun.saveCandidate(three);

        hbmRun.getAllCandidates().forEach(System.out::println);
        System.out.println(hbmRun.getCandidateById(one.getId()));
        System.out.println(hbmRun.getCandidateByName(three.getName()));

        two.setExperience(4);
        two.setSalary(400_000);
        hbmRun.updateCandidate(two);
        System.out.println(hbmRun.getCandidateByName(two.getName()));

        hbmRun.deleteCandidate(one.getId());
        hbmRun.getAllCandidates().forEach(System.out::println);
    }

    public void saveCandidate(Candidate candidate) {
        transaction(session -> session.save(candidate));
    }

    public List<Candidate> getAllCandidates() {
        return transaction(session ->
                session.createQuery("from Candidate", Candidate.class).list()
        );
    }

    public Candidate getCandidateById(int id) {
        return transaction(
                session -> session.createQuery("from Candidate where id = :fId", Candidate.class)
                        .setParameter("fId", id)
                        .uniqueResult()
        );
    }

    public Candidate getCandidateByName(String name) {
        return transaction(
                session -> session.createQuery("from Candidate where name = :fName", Candidate.class)
                        .setParameter("fName", name)
                        .uniqueResult()
        );
    }

    public void updateCandidate(Candidate candidate) {
        transaction(
                session -> session.createQuery("update Candidate set experience = :newExp, salary = :newSal where id = :fId")
                        .setParameter("newExp", candidate.getExperience())
                        .setParameter("newSal", candidate.getSalary())
                        .setParameter("fId", candidate.getId())
                        .executeUpdate()
        );
    }

    public void deleteCandidate(int id) {
        transaction(
                session -> session.createQuery("delete from Candidate where id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate()
        );
    }

    private <T> T transaction(Function<Session, T> command) {
        T rsl = null;
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            rsl = command.apply(session);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return rsl;
    }
}
