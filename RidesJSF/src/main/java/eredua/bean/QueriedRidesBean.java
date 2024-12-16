package eredua.bean;

import java.util.List;

import businessLogic.BLFacade;
import domain.Booking;

public class QueriedRidesBean {
	private BLFacade facadeInterface = FacadeBean.getBusinessLogic();
	private List<Booking> booking;
	
	public QueriedRidesBean() {
		booking=facadeInterface.getBooking();
	}
	
	public String main() {
		return "m";
	}

	public List<Booking> getBooking() {
		return booking;
	}

	public void setBooking(List<Booking> booking) {
		this.booking = booking;
	}
	

}
