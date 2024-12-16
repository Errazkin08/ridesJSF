package eredua.bean;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.BLFacade;

public class DataRideQueryBean {
	private Date date;
	private BLFacade facadeInterface=FacadeBean.getBusinessLogic();

	
	public DataRideQueryBean() {
		
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	public String search() {
		if(date==null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Choose a date"));
			return null;
		}
		else {
			facadeInterface.getRidesDate(date);
			return "d";
		}
	}
	

}
