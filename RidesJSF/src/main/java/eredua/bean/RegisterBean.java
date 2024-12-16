package eredua.bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.BLFacade;
import domain.Driver;
import domain.User;

public class RegisterBean {
	private String email;
	private String name;
	private String password;
	private String userType;
	private BLFacade facadeInterface=FacadeBean.getBusinessLogic();

	public String register() {
		try {
		
		System.out.println(email + " | "+name +" | "+password + " | "+userType);
		if (email != null & name != null & password != null & (userType.equals("Driver") || userType.equals("User"))) {
			if (userType.equals("Driver")) {
				System.out.println("new Driver");
				facadeInterface.createDriver(email,name,password);
				System.out.println("Done");

			} else if (userType.equals("User")) {
				System.out.println("new User");

				facadeInterface.createUser(email,name,password);
				System.out.println("Done");

	
			}
			
			
		}
		}
		catch(Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Something went wrong while registrating"));
		}
		return "l";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String main() {
		return "m";
	}
}
