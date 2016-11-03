package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import app.domain.Car;
import app.domain.Repositories;
import app.persistence.MongoRepositories;
import org.mongolink.MongoSession;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args)  {
        String versionURI = "/v1";

        port(8080); // use port 8080

        get(versionURI +"/", (req, res) -> "It worked!"); // test server

        /* return all cars */
        get(versionURI +"/cars", (req, res) -> {
            MongoSession session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));

            List<Car> cars = Repositories.cars().all();

            session.stop();

            res.status(200);
            if (cars.size() == 0) {
                return "No cars";
            } else {
                StringBuilder responsepage  = new StringBuilder();
                for (Car car: cars) {
                    responsepage.append(dataToJson(car));
                    responsepage.append('\n');
                }
                res.type("application/json");
                return responsepage.toString();
            }
        });

        /* return car with :id */
        get(versionURI +"/cars/:id", (req, res) -> {
            MongoSession session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));

            UUID uid = UUID.fromString(req.params(":id"));
            Car car = Repositories.cars().get(uid);

            session.stop();

            if (car == null) {
                res.status(404);
                return "Car: " + req.params(":id") +" not found";
            } else {
                res.status(200);
                res.type("application/json");
                return dataToJson(car);
            }
        });

        /* create new car */
        post(versionURI + "/cars", (req, res) -> {
            MongoSession session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));

            try {

                Car car = new Car(req.queryParams("make"),
                                  req.queryParams("model"),
                                  req.queryParams("license"),
                                  req.queryParams("carType"),
                                  Integer.parseInt(req.queryParams("maxPassengers")),
                                  req.queryParams("validRideTypes"));

                if (!car.isValid()) {
                    res.status(400);
                    return "invalid car";
                }

                Repositories.cars().add(car);

                session.stop();

                res.status(200);
                res.type("application/json");
                return dataToJson(car);
            }  catch (Exception e){
                session.stop();
                res.status(400);
                return e.getMessage();
            }
         });

        /* delete car with :id */
        delete(versionURI +"/cars/:id", (req, res) -> {
            MongoSession session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));

            UUID uid = UUID.fromString(req.params(":id"));
            Car car = Repositories.cars().get(uid);

            if (car == null) {
                res.status(404); 
                session.stop();
                return "Car: " + req.params(":id") +" not found";
            } else {
                Repositories.cars().delete(car);
                session.stop();
                res.status(200);
                return "Car: " + req.params(":id") +" deleted";
            }
        });

        /* initialize db */ 
        get(versionURI + "/cars/setup", (req, res) -> {
            MongoSession session = MongoConfiguration.createSession();
            session.start();
            Repositories.initialise(new MongoRepositories(session));

            try {

                Car car = new Car("Tesla", "S", "1234567", "sedan", 4, "LUXURY");

                Repositories.cars().add(car);

                session.stop();

                res.status(200);
                res.type("application/json");
                return "Added sample cars";
            }  catch (Exception e){
                session.stop();
                res.status(400);
                return "";
            }

        });

    }

    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e){
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }
}
