package hello;

import static spark.Spark.*;
import static 

public class HelloWorld {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}