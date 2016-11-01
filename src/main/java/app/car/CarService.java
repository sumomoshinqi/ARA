package app.car;

import org.mongolink.domain.mapper.ContextBuilder;
import org.mongolink.*;

public class CarService {

    public CarService () {
        ContextBuilder builder = new ContextBuilder("app.car.mapping");
        Settings settings = Settings.defaultInstance()
                    .withDefaultUpdateStrategy(UpdateStrategies.DIFF)
                    .withDbName("db")
                    .withAddresses(Lists.newArrayList(new ServerAddress("host", 7689)))
                    .withAuthentication("user", "passwd");
        MongoSessionManager mongoSessionManager = MongoSessionManager.create(builder, settings);
    }

	public static List<Car> getAllCars = (Request request, Response response) -> {
        MongoSession session = mongoSessionManager.createSession();
        session.start();
        //....
        return session.get(persistentType());
        session.stop();
    };

    public static Car getCar = (Request request, Response response) -> {
        MongoSession session = mongoSessionManager.createSession();
        session.start();
        //....
        return session.get(request.id, persistentType());
        session.stop();
    };

    public static Car createCar = (Request request, Response response) -> {
    	Car car = new Car(request); // loop over request.body for parameters
    	return car;
    };

    public static Car updateCar = (Request request, Response response) -> {
    };
}