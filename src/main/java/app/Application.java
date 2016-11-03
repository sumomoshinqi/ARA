package app;

import app.car.*;
import app.driver.*;
import app.passenger.*;
import app.ride.*;
import static spark.Spark.*;
import static spark.debug.DebugScreen.*;

public class Application {

	// Declare dependencies
	public static Car car;
	public static Driver driver;
	public static Passenger passenger;
	public static Ride ride;

	public static void main(String[] args) {

		// Configure Spark
		port(4567);
		staticFiles.location("/public");
		staticFiles.expireTime(600L);

		// Set up before-filters (called before each get/post)
		before((request, response) -> {
			boolean authenticated;
			// ... check if authenticated
			authenticated = true; // for now
			if (!authenticated) {
				halt(401, "You are not welcome here");
			}
		});

		new CarController(new CarService());
		new DriverController(new DriverService());
		new PassengerController(new PassengerService());
		new RideController(new RideService());
	}

}
