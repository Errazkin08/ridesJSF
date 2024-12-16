package domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

@Entity

public class Booking implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ride_id", nullable = false) // ride_id references Ride table
    private Ride ride;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // user_id references email in User table
    private User user;
	private int seats;


	public Booking(Ride ride, User traveler, int seats) {
		this.ride = ride;
		this.user = traveler;
		this.seats = seats;
		
		System.out.println("Book created"+" Ride:"+ride+" | User: "+user+" |seats:"+seats);
	}
	public Booking() {
		
	}

	public int getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(int bookNumber) {
		this.bookingNumber = bookNumber;
	}

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public User getTraveler() {
		return user;
	}

	public void setTraveler(User traveler) {
		this.user = traveler;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	

	public double prezioaKalkulatu() {
		return (this.ride.getPrice())*this.seats;
	}

}
