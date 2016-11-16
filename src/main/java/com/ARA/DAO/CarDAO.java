package com.ARA.DAO;

import com.ARA.module.Car;

import com.ARA.util.Error;
import com.ARA.util.dataToJson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import spark.Response;


/**
 * implementation of Car's Data Access Object
 * @author Edam
 *
 */
public class CarDAO extends BasicDAO<Car, String> {

    public CarDAO(Class<Car> entityClass, Datastore ds) {
        super(entityClass, ds);
    }

    public String getAllCars(Request req, Response res) throws IOException {
        try {
            List<Car> allCar = getDs().find(Car.class).asList();
            res.status(200);
            return dataToJson.d2j(allCar);
        } catch (Exception e) {
            res.status(500);
            return dataToJson.d2j(new Error(500, 5000, e.getMessage()));
        }
    }

    public String getCar(Request req, Response res) throws IOException {
        try {
            String id = req.params(":id");
            Car car = getDs().find(Car.class).field("id").equal(id).get();
            if (car == null) {
                res.status(400);
                return dataToJson.d2j(new Error(400, 1002, "Given car does not exist"));
            }
            res.status(200);
            return dataToJson.d2j(car);
        } catch (Exception e) {
            res.status(500);
            return dataToJson.d2j(new Error(500, 5000, e.getMessage()));
        }
    }

    public String createCar(Request req, Response res) throws IOException {
        try {
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(req.body());

            String make = jsonObject.get("make").toString().replaceAll("\"", "");

            String model = jsonObject.get("model").toString().replaceAll("\"", "");

            String license = jsonObject.get("license").toString().replaceAll("\"", "");

            String carType = jsonObject.get("carType").toString().replaceAll("\"", "");

            Integer maxPassengers = Integer.valueOf(jsonObject.get("maxPassengers").toString());
            String color = jsonObject.get("color").toString().replaceAll("\"", "");

            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> validRideTypes = new Gson().fromJson(jsonObject.get("validRideTypes").getAsJsonArray(), listType);

            Car newCar = new Car(make, model, license, carType, maxPassengers, color, validRideTypes);

            if (!newCar.isValidCar()) {
                res.status(400);
                return dataToJson.d2j(new Error(400, 2000, "Invalid data type"));
            }

            getDs().save(newCar);
            res.status(200);
            return dataToJson.d2j(newCar);
        } catch (Exception e) {
            res.status(500);
            return dataToJson.d2j(new Error(500, 5000, e.getMessage()));
        }
    }

    public String updateCar(Request req, Response res) throws IOException {
        try {
            String id = req.params(":id");
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(req.body());

            Car car = getDs().find(Car.class).field("id").equal(id).get();

            if (jsonObject.has("make")) {
                String make = jsonObject.get("make").toString().replaceAll("\"", "");

                car.setMake(make);
            }

            if (jsonObject.has("model")) {
                String model = jsonObject.get("model").toString().replaceAll("\"", "");

                car.setModel(model);
            }

            if (jsonObject.has("license")) {
                String license = jsonObject.get("license").toString().replaceAll("\"", "");

                car.setLicense(license);
            }

            if (jsonObject.has("carType")) {
                String carType = jsonObject.get("carType").toString().replaceAll("\"", "");

                car.setCarType(carType);
            }

            if (jsonObject.has("maxPassengers")) {
                Integer maxPassengers = Integer.valueOf(jsonObject.get("maxPassengers").toString());

                car.setMaxPassengers(maxPassengers);
            }

            if (jsonObject.has("color")) {
                String color = jsonObject.get("color").toString().replaceAll("\"", "");

                car.setColor(color);
            }

            if (jsonObject.has("validRideTypes")) {
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                List<String> validRideTypes = new Gson().fromJson(jsonObject.get("validRideTypes").getAsJsonArray(), listType);

                car.setValidRideTypes(validRideTypes);
            }
            if (!car.isValidCar()) {
                res.status(400);
                return dataToJson.d2j(new Error(400, 2000, "Invalid data type"));
            }

            getDs().save(car);
            res.status(200);
            return dataToJson.d2j(car);
        } catch (Exception e) {
            res.status(500);
            return dataToJson.d2j(new Error(500, 5000, e.getMessage()));
        }
    }

    public String deleteCar(Request req, Response res) throws IOException {
        try {
            String id = req.params(":id");
            Car car = getDs().find(Car.class).field("id").equal(id).get();
            if (car == null) {
                res.status(400);
                return dataToJson.d2j(new Error(400, 1002, "Given car does not exist"));
            }
            getDs().delete(car);
            res.status(200);
            return dataToJson.d2j(car);
        } catch (Exception e) {
            res.status(500);
            return dataToJson.d2j(new Error(500, 5000, e.getMessage()));
        }
    }
}
