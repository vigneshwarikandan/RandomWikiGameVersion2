package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SAMYUKTHAH on 16-08-2015.
 */



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

/**
 * Words which have no place in this term-concept mapping are those which
 * describe no concepts. The particle words of grammar, the, of, and ..., known
 * in IR as stopwords, fall into this category. Stopwords can be useful for
 * retrieval but only in searching for phrases (see
 * http://snowball.tartarus.org/texts/introduction.html)
 *
 * Sources:
 * <ul>
 * <li>http://www.ranks.nl/resources/stopwords.html
 * <li>http://www.lextek.com/manuals/onix/stopwords1.html
 * </ul>
 *
 * @author Samyuktha.H
 *
 */
public final class StopWords {
    private HashSet<String> list = new HashSet<String>();

    public StopWords(File src) throws Exception {
        init(src);
    }

    public void init(File swf) throws Exception {
        if (swf == null)
            return;

        list.clear();
       System.out.println("the path is "+swf.getPath());
        if (!swf.exists()) {
            throw new RuntimeException("File not found: " + swf.getAbsolutePath());
        }
        BufferedReader rdr = null;
        try {
            rdr = new BufferedReader(new FileReader(swf));
            String raw = rdr.readLine();
            while (raw != null) {
                if (!raw.startsWith("#"))
                    list.add(raw.trim().toLowerCase());
                raw = rdr.readLine();
            }
        } finally {
            rdr.close();
        }
    }
}

