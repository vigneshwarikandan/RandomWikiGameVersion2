package ServerCall;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by SAMYUKTHAH on 07-08-2015.
 */
public class ServerCallClass {
    public static String getRandomWikiPage(){
        String replacedString="";
        JSONObject result=JSONParser.getJSONFromUrl("https://en.wikipedia.org/w/api.php?action=query&generator=random&grnnamespace=0&prop=extracts&exchars=1500&format=json");
        try{
            JSONObject queryString=result.getJSONObject("query");
            JSONObject pages=queryString.getJSONObject("pages");
            Iterator keyiterator=pages.keys();
            ArrayList<String> keyArray=new ArrayList<String>();

                String key=keyiterator.next().toString();
                System.out.println("the key is " + key);
                keyArray.add(key);
            JSONObject extractJSONObject=pages.getJSONObject(key);

            String extractpage=extractJSONObject.getString("extract");
            System.out.println(" the extract JSON string)"+extractpage);
             replacedString=extractpage.replaceAll("<[^>]+>", "");
            System.out.println(" the replaced JSON string)"+replacedString);



        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("result="+replacedString);
        return replacedString;
    }
}
