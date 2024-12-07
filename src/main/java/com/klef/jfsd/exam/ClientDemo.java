package com.klef.jfsd.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ClientDemo {

    public static void main(String[] args) {
        // Step 1: Set up the Hibernate session factory
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Customer.class)
                .buildSessionFactory();

        // Step 2: Create a session
        Session session = factory.getCurrentSession();

        try {
            // Task I: Insert a new customer record
            session.beginTransaction();
            Customer customer1 = new Customer("John Doe", "john@example.com", 30, "New York");
            session.save(customer1);
            session.getTransaction().commit();

            // Task II: Query customers using Hibernate Criteria (JPA Criteria API)

            // Start a new session for Criteria queries
            session = factory.getCurrentSession();
            session.beginTransaction();

            // Create a CriteriaBuilder and CriteriaQuery
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);

            // Define the root of the query (the "from" part)
            Root<Customer> root = criteriaQuery.from(Customer.class);

            // Apply a restriction (e.g., Customers with age > 25)
            criteriaQuery.select(root).where(criteriaBuilder.gt(root.get("age"), 25));

            // Execute the query
            List<Customer> customers = session.createQuery(criteriaQuery).getResultList();
            customers.forEach(System.out::println);

            session.getTransaction().commit();

        } finally {
            factory.close();
        }
    }
}
