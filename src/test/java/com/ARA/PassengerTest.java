package com.ARA;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import spark.Spark;

import java.io.IOException;
import java.util.Map;

/**
 * implementation of Passenger Test - CRUD
 * @author Edam & Ruby
 * @version 4.0.0
 * @Note: Action 1 or action 2 is needed for test
 *        1. Mark out email duplicate validation in PassengerDAO 118-122
 *        2. Change required every test run -
 *        - "emailAddress" in requestBody
 */
public class PassengerTest {

    String testPassengerID;
    String requestBody = "{" +
            "'firstName':'Zoe'," +
            "'lastName':'Moore'," +
            "'emailAddress':'zoe05@att.com'," +
            "'password':'1234567890'," +
            "'addressLine1':'1999 Castro Street'," +
            "'addressLine2':''," +
            "'city':'MV'," +
            "'state':'CA'," +
            "'zip':'72033'," +
            "'phoneNumber':'546-777-8989'" +
            "}";

    /** This test is used to test Passenger.
     * 1. create passenger
     * 2. get passenger
     * 3. patch passenger with update info
     * 4. delete passenger
     * 5. should not get deleted passenger
     * 6. should not create a passenger with missing email address
     * 7. should not create a passenger with with long password
     * */
    @Test
    public void Passenger() throws IOException {

        //createPassenger
        TestResponse resPost = TestResponse.request("POST", "/v1/passengers", requestBody);
        Map<String, String> jsonPost = resPost.json();
        assertEquals(200, resPost.status);
        assertEquals("Zoe", jsonPost.get("firstName"));
        assertEquals("Moore", jsonPost.get("lastName"));
        assertEquals("1999 Castro Street", jsonPost.get("addressLine1"));
        assertEquals("", jsonPost.get("addressLine2"));
        assertEquals("MV", jsonPost.get("city"));
        assertEquals("CA", jsonPost.get("state"));
        assertEquals("72033", jsonPost.get("zip"));
        assertEquals("546-777-8989", jsonPost.get("phoneNumber"));
        assertNotNull(jsonPost.get("id"));

        testPassengerID = jsonPost.get("id");

        //getPassenger just created
        TestResponse resGet = TestResponse.request("GET", "/v1/passengers/"+testPassengerID+"");
        Map<String, String> jsonGet = resGet.json();
        assertEquals(200, resGet.status);
        assertEquals("Zoe", jsonGet.get("firstName"));
        assertEquals("Moore", jsonGet.get("lastName"));
        assertEquals("1999 Castro Street", jsonGet.get("addressLine1"));
        assertEquals("", jsonGet.get("addressLine2"));
        assertEquals("MV", jsonGet.get("city"));
        assertEquals("CA", jsonGet.get("state"));
        assertEquals("72033", jsonGet.get("zip"));
        assertEquals("546-777-8989", jsonGet.get("phoneNumber"));
        assertNotNull(jsonGet.get("id"));

        //patchPassenger
        String requestBodyPatch = "{" +
                "'firstName':'Paul'" +
                "}";
        TestResponse resPatch = TestResponse.request("PATCH", "/v1/passengers/"+testPassengerID+"",requestBodyPatch);
        Map<String, String> jsonPatch = resPatch.json();
        assertEquals(200, resPatch.status);
        assertEquals("Paul", jsonPatch.get("firstName"));
        assertEquals("Moore", jsonPatch.get("lastName"));
        assertEquals("1999 Castro Street", jsonPatch.get("addressLine1"));
        assertEquals("", jsonPatch.get("addressLine2"));
        assertEquals("MV", jsonPatch.get("city"));
        assertEquals("CA", jsonPatch.get("state"));
        assertEquals("72033", jsonPatch.get("zip"));
        assertEquals("546-777-8989", jsonPatch.get("phoneNumber"));
        assertNotNull(jsonPatch.get("id"));

        //deletePassenger
        TestResponse resDelete = TestResponse.request("DELETE", "/v1/passengers/"+testPassengerID+"");
        Map<String, String> jsonDelete  = resDelete .json();
        assertEquals(200, resDelete.status);
        assertEquals("Paul", jsonDelete.get("firstName"));
        assertEquals("Moore", jsonDelete.get("lastName"));
        assertEquals("1999 Castro Street", jsonDelete.get("addressLine1"));
        assertEquals("", jsonDelete.get("addressLine2"));
        assertEquals("MV", jsonDelete.get("city"));
        assertEquals("CA", jsonDelete.get("state"));
        assertEquals("72033", jsonDelete.get("zip"));
        assertEquals("546-777-8989", jsonDelete.get("phoneNumber"));
        assertNotNull(jsonPatch.get("id"));

        //should not get deleted passenger
        TestResponse resGetNG = TestResponse.request("GET", "/v1/passengers/"+testPassengerID+"");
        assertEquals(400, resGetNG.status);

        //should not create a passenger with missing email address
        String requestBodyCreateMissingEmailAddress = "{" +
                "'firstName':'Zoe'," +
                "'lastName':'Moore'," +
                "'password':'1234567890'," +
                "'addressLine1':'1999 Castro Street'," +
                "'addressLine2':''," +
                "'city':'MV'," +
                "'state':'CA'," +
                "'zip':'72033'," +
                "'phoneNumber':'546-777-8989'" +
                "}";
        TestResponse resCreateMissingMake = TestResponse.request("POST", "/v1/passengers", requestBodyCreateMissingEmailAddress);
        assertEquals(500, resCreateMissingMake.status);

        //should not create passenger with long password
        String requestBodyCreateLongPassword = "{" +
                "'firstName':'Zoe'," +
                "'lastName':'Moore'," +
                "'emailAddress':'zoe04@att.com'," +
                "'password':'1234567890zoezoezoezoezoezoezoezoezoezoezoezoezoe'," +
                "'addressLine1':'1999 Castro Street'," +
                "'addressLine2':''," +
                "'city':'MV'," +
                "'state':'CA'," +
                "'zip':'72033'," +
                "'phoneNumber':'546-777-8989'" +
                "}";
        TestResponse resCreateLongMake = TestResponse.request("POST", "/v1/passengers", requestBodyCreateLongPassword);
        assertEquals(400, resCreateLongMake.status);

    }
}