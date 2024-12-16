package eredua.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Ride;

public class QueryRideBean {
	private List<Ride> rides = new ArrayList<Ride>();
	private List<String> dCity = new ArrayList<String>();
	private List<String> aCity = new ArrayList<String>();
	private BLFacade facadeInterface = FacadeBean.getBusinessLogic();
	private String departCity;
	private String arrivalCity;
	private Date date;
	private int places;
	private Ride ride;

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public int getPlaces() {
		return places;
	}

	public void setPlaces(int places) {
		this.places = places;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public String getDepartCity() {
		return departCity;
	}

	public void setDepartCity(String departCity) {
		this.departCity = departCity;
	}

	public QueryRideBean() {

	}

	public List<Ride> getRides() {
		System.out.println("Depart City=" + departCity);
		System.out.println("Arrival City=" + arrivalCity);
		System.out.println("Date" + date);
		if (departCity != null & arrivalCity != null & date != null) {
			rides = facadeInterface.getRides(departCity, arrivalCity, date);
			if (rides.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("There is no Ride with that parameters"));
			}
		}

		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public List<String> getdCity() {
		if (dCity == null || dCity.isEmpty() || dCity.size() == 0) {
			dCity = facadeInterface.getDepartCities();
		}
		return dCity;
	}

	public void setdCity(List<String> dCity) {
		this.dCity = dCity;
	}

	public List<String> getaCity() {
		return aCity;
	}

	public void setaCity(List<String> aCity) {
		this.aCity = aCity;
	}

	public void listenerDepart(AjaxBehaviorEvent event) {
		setaCity(facadeInterface.getDestinationCities(departCity));
		getaCity();
		System.out.println(aCity);
	}

	public void listenerArrival(AjaxBehaviorEvent event) {
		getRides();
	}

	public void listenerDate(AjaxBehaviorEvent event) {
		getRides();
	}

	public void listenerRides(AjaxBehaviorEvent event) {
		getRides();
		if (rides.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Some Information is missing or Wrong. There is no ride"));
		}
	}

	public String main() {
		return "m";
	}
	public void chooseRide(Ride r) {
		System.out.println("Choosed ride:"+r);
		ride=r;
	}

	public void book() {
		System.out.println("Booking Ride");
		if(ride==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					"Please select a Ride for booking"));
		}
		if (places > 0 && places <= ride.getnPlaces()) {
			try {
				boolean erreserbatua = facadeInterface.bookARide(ride, places);
				if (!erreserbatua) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
							"There was an error while booking, please check if you have already booked this ride"));
				}
				else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("Queried Correctly"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("You are not a User, please login or log out from the driver account"));

			}

		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Please choose a valid option for places"));
		}
		getRides();
	}

}
