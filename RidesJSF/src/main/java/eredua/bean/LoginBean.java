package eredua.bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.BLFacade;

public class LoginBean {
	private String username;
	private String password;
	private BLFacade facadeInterface = FacadeBean.getBusinessLogic();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String main() {
		return "m";
	}

	public String login() {
		if (username != null & password != null) {
			facadeInterface.login(username, password);
			if (!facadeInterface.isLoged()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("The email or the password are wrong. Or you are not registered."));
				
			}
			else {
				return "m";
			}
		}
		return null;
	}
}
