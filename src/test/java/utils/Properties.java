package utils;

import java.util.Date;
import java.util.Random;

public class Properties {
    public static String host = "https://todo.ly/";

    private static Random rand = new Random();
    public static String email = "testVanessa2023Test"+ Integer.toString(rand.nextInt(10000))+"@email.com";
    public static String pwd = "12345";

    public static String name = "Test";

}
