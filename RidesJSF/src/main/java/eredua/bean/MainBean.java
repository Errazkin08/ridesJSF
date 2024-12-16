package eredua.bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.BLFacade;

public class MainBean {
	private BLFacade facadeInterface=FacadeBean.getBusinessLogic();
	public MainBean() {
		
	}
	public String query() {
		return "q";
	}
	public String create() {
		if(facadeInterface.isDriver()) 	return "c";
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("You cant create any Ride you are not a Driver"));
		return null;
	}
	public String login() {
		return "l";
	}
	public String register() {
		return "r";
	}
	public void logout() {
		boolean l=facadeInterface.logout();
		if(l)
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Logged out"));
		else
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("You can`t log out because you are not logged"));
	}
	public String ridesDate() {
		
		return "rd";
		
	}
	public String showQuery() {
		if(facadeInterface.isUser())
		return "sq";
		else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("You can`t view your queried Rides because you are not an User"));
			return null;
		}
	}
}
