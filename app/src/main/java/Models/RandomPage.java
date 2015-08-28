package Models;

import ServerCall.ServerCallClass;

/**
 * Created by SAMYUKTHAH on 07-08-2015.
 */
public class RandomPage {
public static String jread(){
    String result=ServerCallClass.getRandomWikiPage();
    return result;
}
}
