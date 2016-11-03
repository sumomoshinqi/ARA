package persistance;

public abstract class Repositories {

    public static void initialise(Repositories instance) {
        Repositories.instance = instance;
    }

    public static CarRepository cars() {
        return instance.carsRepository();
    }

    public static DriverRepository drivers() {
    	return instance.driversRepository();
    }

    public static PassengerRepository passengers() {
    	return instance.passengersRepository();
    }

    public static RideRepository rides() {
    	return instance.ridesRepository();
    }

    protected abstract CarRepository carsRepository();
    protected abstract DriverRepository driversRepository();
    protected abstract PassengerRepository passengersRepository():
    protected abstract RideRepository ridesRepository():

    private static Repositories instance;
}
