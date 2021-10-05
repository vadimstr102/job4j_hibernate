package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.hql.model.Candidate;
import ru.job4j.hql.model.Vacancy;
import ru.job4j.hql.model.VacancyBase;

import java.util.List;
import java.util.function.Function;

public class HbmRun {
    public static void main(String[] args) {
        Vacancy vacancy1 = new Vacancy("Sber: Junior Java developer");
        Vacancy vacancy2 = new Vacancy("Tinkoff: Junior Java developer");
        Vacancy vacancy3 = new Vacancy("Yandex: Junior Java developer");
        VacancyBase vacancyBase = new VacancyBase();
        vacancyBase.addVacancy(vacancy1);
        vacancyBase.addVacancy(vacancy2);
        vacancyBase.addVacancy(vacancy3);
        Candidate candidate = new Candidate("Vadim", 1, 100_000);
        candidate.setVacancyBase(vacancyBase);

        HbmRun hbmRun = new HbmRun();
        hbmRun.saveCandidate(candidate);
        Candidate candidateFromBase = hbmRun.getCandidateByIdWithVacancies(candidate.getId());

        System.out.println(candidateFromBase);
        System.out.println(candidateFromBase.getVacancyBase());
        candidateFromBase.getVacancyBase().getVacancies().forEach(System.out::println);
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

    public Candidate getCandidateByIdWithVacancies(int id) {
        return transaction(
                session -> session.createQuery(
                                "select distinct c from Candidate c "
                                        + "join fetch c.vacancyBase vb "
                                        + "join fetch vb.vacancies v "
                                        + "where c.id = :fId", Candidate.class
                        )
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
