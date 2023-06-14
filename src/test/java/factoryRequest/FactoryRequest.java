package factoryRequest;

import java.util.HashMap;
import java.util.Map;

public class FactoryRequest {
    private static Map<String, IRequest> iRequestMap = new HashMap<>();
    public static IRequest make (String type) {
        iRequestMap.put("GET", new RequestGET());
        iRequestMap.put("PUT", new RequestPUT());
        iRequestMap.put("POST", new RequestPOST());
        iRequestMap.put("DELETE", new RequestDELETE());
        return iRequestMap.containsKey(type.toUpperCase()) ? iRequestMap.get(type.toUpperCase()) : iRequestMap.get("GET");
    }
}
