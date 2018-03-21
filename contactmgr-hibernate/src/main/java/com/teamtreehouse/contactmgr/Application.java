package com.teamtreehouse.contactmgr;



import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.teamtreehouse.contactmgr.model.Contact;
import com.teamtreehouse.contactmgr.model.Contact.ContactBuilder;

public class Application {
	//Hold a reusable reference to a SessionFactory(since we need only one)
	private static final SessionFactory session_factory=buildSessionFactory();
	
	private static SessionFactory buildSessionFactory() {
		//Create a StandartServiceRegistry
		Configuration configuration=new Configuration().configure();
		configuration.addAnnotatedClass(Contact.class);
		StandardServiceRegistryBuilder builder=new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		SessionFactory sessionFactory=configuration.buildSessionFactory(builder.build());
		
		return sessionFactory;
		
	}
	
	//h2dialect bulunamadı hatası verdi.
	
	public static void main(String[] args) {
		Contact contact=new ContactBuilder("Chris","Ramacciotti")
				.withEmail("rama@teamtreehouse.com")
				.withPhone(7735556666L)
				.build();
		
		int id = save(contact);
		//Display a list of contacts before the update
		System.out.printf("%n%nBefore update%n%n");
		for (Contact c : fetchAllContacts()) {
			System.out.println(c);
		}
		//java 8 stream ile ekrana bas
		fetchAllContacts().stream().forEach(System.out::println);
		
		//Get the persisted contact
		Contact c=findContactById(id);
		
		//Update the contact
		c.setFirstName("Beatrix");
		
		//Persist the changes
		System.out.printf("%n%nUpdating...%n%n");
		update(c);
		System.out.printf("%n%nUpdate complete!%n%n");
		//Display a list of contacts after the update
		System.out.printf("%n%nAfter update%n%n");
		fetchAllContacts().stream().forEach(System.out::println);
		System.out.printf("%n%nAfter delete%n%n");
		delete(c);
		fetchAllContacts().stream().forEach(System.out::println);
		
		
	}
	
	private static Contact findContactById(int id) {
		//Open a session
		Session session=session_factory.openSession();
		
		//Retrieve the persistent object (or null if not found)
		Contact contact=session.get(Contact.class, id);
		
		//Close the session
		session.close();
		
		//Return the object
		return contact;
	}
	
	private static void update(Contact contact) {
		//Open a session
		Session session=session_factory.openSession();
		
		//Begin a transaction
		session.beginTransaction();
		
		//Use the session to update the contact
		session.update(contact);
		
		//Commit the transaction
		session.getTransaction().commit();
		
		//Close the session
		session.close();
	}
	
			private static void delete(Contact contact) {
				//Open a session
				Session session=session_factory.openSession();
				
				//Begin a transaction
				session.beginTransaction();
				
				//Use the session to delete the contact
				session.delete(contact);
				
				//Commit the transaction
				session.getTransaction().commit();
				
				//Close the session
				session.close();
	}
	
	
	
	@SuppressWarnings("deprecation")
	private static List<Contact> fetchAllContacts(){
		//Open a session
		Session session=session_factory.openSession();
		
		//Create Criteria
		//criteria deprecated, createquery kullan.
		Criteria criteria=session.createCriteria(Contact.class);
		
		//Get a list of Contact objects according to the Criteria object
		List<Contact> contacts=criteria.list();
		
		//Close the session
		session.close();
		
		return contacts;
	}

			private static int save(Contact contact) {
				//Open a session
				Session session=session_factory.openSession();
				
				//Begin a transaction
				//javax.transaction.jta pom.xml'e eklenmeli yazıyor.
				session.beginTransaction();
				
				//Use the session to save the contact
				int id = (int) session.save(contact);
				//Commit the transaction
				session.getTransaction().commit();
				//Close the session
				session.close();
				
				return id;
	}
	

}
