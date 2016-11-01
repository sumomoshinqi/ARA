package app.car;

import java.util.UUID;

public class Car {
	UUID id;
	private String make;
	private String model;
	private String license;
	private String carType;
	private int maxPassengers;
	private String color;
	private String validRideTypes; // Values are ECONOMY, PREMIUM, EXECUTIVE
	private Driver driver;

	protected Car() {};

	public Car(String make, String email, String ....) { // Do we need a constructor here?
		////////////
		this.id = UUID.randomUUID();
		this.make = make;
		
	}
}
