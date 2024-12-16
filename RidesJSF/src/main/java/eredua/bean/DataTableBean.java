package eredua.bean;

import java.util.ArrayList;
import java.util.List;

import businessLogic.BLFacade;
import domain.Ride;

public class DataTableBean {
	private BLFacade facadeInterface=FacadeBean.getBusinessLogic();
	private List<Ride> rides;
	
	
	public DataTableBean() {
		System.out.println(rides);
		rides=facadeInterface.getDateRides();
	}

	public List<Ride> getRides() {
		rides=facadeInterface.getDateRides();
		return rides;
	}

	public void setRides(List<Ride> rides) {
		
		this.rides = rides;
		rides=facadeInterface.getDateRides();
	}
	public String main() {
		return "m";
	}
	
	

}
