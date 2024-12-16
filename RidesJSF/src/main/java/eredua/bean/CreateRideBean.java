package eredua.bean;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.BLFacade;
import domain.Ride;

public class CreateRideBean {
	private String from;
	private String to;
	private int seats;
	private int price;
	private Date date;
	private BLFacade facadeInterface=FacadeBean.getBusinessLogic();
	
	public CreateRideBean() {
		
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String createRide() {
		try {
			
			Ride r=facadeInterface.createRide(from, to,date, seats, price, facadeInterface.driverEmail());
			System.out.println(r);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Ride created correctly"));
			return null;
		}
		catch(Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Ocurred an error while creating the Ride"));
			return null;
		}
	}
	public String main() {
		return "m";
	}
	


}
