package businessLogic;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import dataAccess.HibernateDataAccess;
import domain.Ride;
import domain.User;
import domain.Booking;
import domain.Driver;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */
public class BLFacadeImplementation  implements BLFacade {
	HibernateDataAccess dbManager;
	Object user;
	List<Ride> dateRides;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager=new HibernateDataAccess();
		    
		//dbManager.close();

		
	}
	
    public BLFacadeImplementation(HibernateDataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		
		
		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
     public List<String> getDepartCities(){
    	dbManager.open();	
		
		 List<String> departLocations=dbManager.getDepartCities();		

		dbManager.close();
		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	 public List<String> getDestinationCities(String from){
		dbManager.open();	
		
		 List<String> targetCities=dbManager.getArrivalCities(from);		

		dbManager.close();
		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
   
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	   
		dbManager.open();
		try {
			Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverEmail);
			dbManager.close();
			return ride;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new NullPointerException();
			
		}
		
   };
	
   /**
    * {@inheritDoc}
    */
	 
	public List<Ride> getRides(String from, String to, Date date){
		dbManager.open();
		List<Ride>  rides=dbManager.getRides(from, to, date);
		dbManager.close();
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */
	 
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		dbManager.open();
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		dbManager.close();
		return dates;
	}
	
	
	public void close() {
		HibernateDataAccess dB4oManager=new HibernateDataAccess();

		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */
    
	 public void initializeBD(){
    	dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}
	 public void createUser(String email, String name, String password) {
		 dbManager.open();
		 dbManager.createUser(email,name,password);
		 dbManager.close();
	 }

		public void createDriver(String email, String name, String password) {
			dbManager.open();
			 dbManager.createDriver(email,name,password);
			 dbManager.close();
		}
	public void login(String email,String password) {
		dbManager.open();
		Object l=dbManager.login(email,password);
		if(l!=null) user=l;
		dbManager.close();
	}
	public boolean isLoged() {
		System.out.println(user);
		return user!=null;
	}
	public boolean isDriver() {
		return user instanceof Driver;
	}
	public String driverEmail() {
		String email=((Driver)user).getEmail();
		return email;
	}
	public boolean logout() {
		if (user instanceof Driver || user instanceof User) {
			user=null;
			return true;
		}
		else return false;
	}
	public void getRidesDate(Date date) {
		dbManager.open();
		dateRides=dbManager.getRidesDate(date);
		dbManager.close();
		System.out.println(dateRides);
	}
	public List<Ride> getDateRides(){
		return dateRides;
		
	}
	public boolean bookARide(Ride r,int places) throws Exception {
		dbManager.open();
		boolean eran=dbManager.bookARide(r,places,(User)user);
		dbManager.close();
		return eran ;
	}
	public List<Booking> getBooking(){
		return ((User)user).getBookedRides();
	}

	public boolean isUser() {
		return user instanceof User;
	}
}

