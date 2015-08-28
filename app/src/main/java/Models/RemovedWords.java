package Models;

import java.util.HashMap;

/**
 * Created by SAMYUKTHAH on 10-08-2015.
 */
public class RemovedWords extends HashMap<String,String>{
    public RemovedWords(String wordindex,String word){
        put("wordindex",wordindex);
        put("word",word);
    }
}
