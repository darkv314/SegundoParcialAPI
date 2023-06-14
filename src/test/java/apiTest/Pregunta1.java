package apiTest;

import factoryRequest.FactoryRequest;
import factoryRequest.RequestInfo;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Properties;

import java.util.Base64;

import static org.hamcrest.Matchers.equalTo;

public class Pregunta1 {

    RequestInfo requestInfo = new RequestInfo();
    Response response;
    JSONObject body = new JSONObject();
    String auth;

    @BeforeEach
    public void setup() {
        auth = Base64.getEncoder().encodeToString((Properties.email+":"+Properties.pwd).getBytes());
    }

    @Test
    public void verifyPregunta1() {

        //Verify Create User

        body.clear();
        body.put("Email", Properties.email);
        body.put("Password", Properties.pwd);
        body.put("FullName", Properties.name);
        requestInfo.setHost(Properties.host+"api/user.json").setBody(body.toString());
        response = FactoryRequest.make("post").send(requestInfo);
        response.then()
                .log().all()
                .statusCode(200)
                .body("Email", equalTo(body.get("Email")))
                .body("FullName", equalTo(body.get("FullName")));

        //Verify Token

        body.clear();
        requestInfo.setHost(Properties.host+"api/authentication/token.json").setHeaders("Authorization","Basic "+auth);
        response = FactoryRequest.make("get").send(requestInfo);
        response.then()
                .log().all()
                .statusCode(200)
                .body("UserEmail", equalTo(Properties.email));

        String token = response.then().extract().path("TokenString");


        //Verify Create Project
        body.put("Content", "Project1");
        body.put("Icon", 4);
        requestInfo.removeHeader("Authorization").setHost(Properties.host+"api/projects.json").setBody(body.toString()).setHeaders("Token",token);
        response = FactoryRequest.make("post").send(requestInfo);
        response.then()
                .log().all()
                .statusCode(200)
                .body("Content", equalTo(body.get("Content")))
                .body("Icon", equalTo(body.get("Icon")));


        //Verify Delete Token
        requestInfo.setHost(Properties.host+"api/authentication/token.json").setHeaders("Token",token);
        response = FactoryRequest.make("delete").send(requestInfo);
        response.then()
                .log().all()
                .statusCode(200)
                .body("TokenString", equalTo(token));


        //Verify Create Project deleted token
        body.clear();
        body.put("Content", "Project2");
        body.put("Icon", 5);
        requestInfo.setHost(Properties.host+"api/projects.json").setBody(body.toString()).setHeaders("Token",token);
        response = FactoryRequest.make("post").send(requestInfo);
        response.then()
                .log().all()
                .statusCode(200)
                .body("ErrorCode", equalTo(102));
    }

}
