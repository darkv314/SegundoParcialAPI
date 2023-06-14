package apiTest;

import factoryRequest.FactoryRequest;
import factoryRequest.RequestInfo;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Properties;
import utils.PropertiesP2;

import java.util.Base64;

import static org.hamcrest.Matchers.equalTo;

public class Pregunta2 {

    RequestInfo requestInfo = new RequestInfo();
    Response response;

    Response allItems;
    JSONObject body = new JSONObject();
    String auth;

    @BeforeEach
    public void setup() {
        auth = Base64.getEncoder().encodeToString((PropertiesP2.email+":"+PropertiesP2.pwd).getBytes());
    }

    @Test
    public void verifyPregunta2() {
        //Verify Create Item

        for(int i=0;i<4;i++){
            body.clear();
            body.put("Content", "New Item"+Integer.toString(i));
            requestInfo.setHost(Properties.host+"api/items.json").setBody(body.toString()).setHeaders("Authorization", "Basic " + auth);
            response = FactoryRequest.make("post").send(requestInfo);
            response.then()
                    .log().all()
                    .statusCode(200)
                    .body("Content", equalTo(body.get("Content")));
        }

        //Get all items

        requestInfo.setHost(Properties.host+"api/items.json").setHeaders("Authentication","Basic "+auth);
        allItems = FactoryRequest.make("get").send(requestInfo);
        allItems.then()
                .log().all()
                .statusCode(200);


        //Delete all items
        for(int i=0;i<4;i++){
            requestInfo.setHost(Properties.host+"api/items/"+allItems.then().extract().path("Id["+i+"]")+".json").setHeaders("Authentication","Basic "+auth);
            response = FactoryRequest.make("delete").send(requestInfo);
            response.then()
                    .log().all()
                    .statusCode(200)
                    .body("Id", equalTo(allItems.then().extract().path("Id["+i+"]")));
        }


    }
}
