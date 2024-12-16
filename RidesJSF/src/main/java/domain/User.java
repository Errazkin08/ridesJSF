package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String email;
	private String name;
	private String password;
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Booking> bookedRides = new LinkedList<>();

	public List<Booking> getBookedRides() {
		return bookedRides;
	}

	public void setBookedRides(List<Booking> bookedRides) {
		this.bookedRides = bookedRides;
	}
	public boolean bookARide(Booking book) {
		boolean badago=false;
		for (Booking b: bookedRides){
			if(b.getRide().getRideNumber()==book.getRide().getRideNumber()) {
				badago=true;
				return false;
			}
		}
		bookedRides.add(book);
		return true;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User() {
		super();
	}

	public User(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password= password;
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

	public String toString() {
		return email + ";" + name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email != other.email)
			return false;
		return true;
	}

}
