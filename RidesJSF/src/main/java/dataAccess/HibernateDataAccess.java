package dataAccess;

import java.io.File;
import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

import configuration.UtilDate;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.User;
import eredua.HibernateUtil;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

/**
 * It implements the data access to the objectDb database
 */
public class HibernateDataAccess {
	private Session session;
	public HibernateDataAccess() {
		open();
		//initializeDB();
		close();
		
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	
	
	public void initializeDB() {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		try {

			Calendar today = Calendar.getInstance();

			int month = today.get(Calendar.MONTH);
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 1;
				year += 1;
			}

			// Create drivers
			Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez","123");
			Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga","123");
			Driver driver3 = new Driver("driver3@gmail.com", "Test driver","123");

			// Create rides
			Ride ride1= new Ride("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7,driver1);
			
			
			driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
			driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6), 4, 8);
			driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4);

			driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8);

			driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3);
			driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5);
			driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5);

			driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3);

			session.persist(driver1);
			session.persist(driver2);
			session.persist(driver3);
			session.persist(ride1);

			session.getTransaction().commit();
			System.out.println("Db initialized");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */
	public List<String> getDepartCities() {
	    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	    try {
	    	session.beginTransaction();
	        List<String> cities = session.createQuery("SELECT DISTINCT r.nondik FROM Ride r").list();
	        session.getTransaction().commit();
	        return cities;
	    } finally {
	        
	    }
	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */
	public List<String> getArrivalCities(String from) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query query = session.createQuery("SELECT DISTINCT r.nora FROM Ride r WHERE r.nondik=:nondik");
		query.setParameter("nondik", from);
		List<String> arrivingCities=query.list();
		session.getTransaction().commit();
		return arrivingCities;

	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException,Exception {
		System.out.println(">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverEmail
				+ " date " + date);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		
			
			
			if (new Date().compareTo(date) > 0) {
				throw new RideMustBeLaterThanTodayException(
						"Ride has to be later than today");
			}
			
			session.beginTransaction();
			Query q = session.createQuery("select d from Driver d where d.email=:driverEmail ");  // Select only needed fields with "Driver" entity
			q.setParameter("driverEmail", driverEmail);
			System.out.println(q);
			Driver driver = (Driver) q.uniqueResult();
			System.out.println(driver);

			if (driver.doesRideExists(from, to, date)) {
				session.getTransaction().commit();
				throw new RideAlreadyExistException(
						"This ride already exists");
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			// next instruction can be obviated
			session.persist(driver);
			session.persist(ride);
			session.getTransaction().commit();

			return ride;
		

	}

	/**
	 * This method retrieves the rides from two locations on a given date
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	public List<Ride> getRides(String from, String to, Date date) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		System.out.println(">> DataAccess: getRides=> from= " + from + " to= " + to + " date " + date);

		List<Ride> res = new ArrayList<>();
		Query query = session.createQuery("SELECT DISTINCT r FROM Ride r WHERE r.nondik=:nondik AND r.nora=:nora AND r.date=:date");
		query.setParameter("nondik", from);
		query.setParameter("nora", to);
		query.setParameter("date", date);
		
		List<Ride> rides = query.list();
		for (Ride ride : rides) {
			res.add(ride);
		}
		return res;
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<>();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		Query query = session.createQuery(
				"SELECT DISTINCT r.date FROM Ride r WHERE r.nondik=?1 AND r.nora=?2 AND r.date BETWEEN ?3 and ?4");

		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		
		List<Date> dates = query.list();
		for (Date d : dates) {
			res.add(d);
		}
		return res;
	}

	public void open() {
		if(session!=null) {
			if(!session.isOpen()) {
				session = HibernateUtil.getSessionFactory().openSession();
			}
		}
		
	 

	}

	public void close() {
		if( session!=null && session.isOpen()) {
			session.close();
		}
		
		System.out.println("DataAcess closed");
	}

	public void createUser(String email, String name, String password) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		User d=new User(email,name,password);
		session.persist(d);
		session.getTransaction().commit();
		
		
	}
	public void createDriver(String email, String name, String password) {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Driver u=new Driver(email,name,password);
		session.persist(u);
		session.getTransaction().commit();
		
	}
	public Object login(String email, String password) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Object eran;
		Query query = session.createQuery("SELECT d FROM Driver d WHERE d.email=:email AND password=:pass");
		query.setParameter("email", email);
		query.setParameter("pass", password);
		eran=query.uniqueResult();
		if(eran==null) {
			query = session.createQuery("SELECT d FROM User d WHERE d.email=:email AND password=:pass");
			query.setParameter("email", email);
			query.setParameter("pass", password);
			eran=query.uniqueResult();
		}
		return eran;
		
		
		
	}

	public List<Ride> getRidesDate(Date date) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List<Ride> res = new ArrayList<>();
		Query query = session.createQuery("SELECT r FROM Ride r WHERE r.date=:date");
		query.setParameter("date", date);
		
		List<Ride> rides = query.list();
		if(rides.isEmpty()|| rides==null) {
			return new ArrayList<Ride>();
		}
		for (Ride ride : rides) {
			res.add(ride);
		}
		return res;
		
	}
	public boolean bookARide(Ride r, int places, User u) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		boolean res=false;
		Booking b= new Booking(r,u,places);
		if(u.bookARide(b)) {
			r.setBetMinimum(r.getnPlaces()-places);
			session.merge(r);
			session.persist(b);
			res=true;
		}
		session.getTransaction().commit();
		return res;
		
	}
	
	

}
