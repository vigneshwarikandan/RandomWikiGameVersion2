package com.example.samyukthah.randomwikigame;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import Models.RandomPage;
import Models.RemovedWords;
import Models.StopWords;
import Models.StopWordsList;

public class GameActivity3 extends ActionBarActivity {

    List<RemovedWords> removedwordsList=new ArrayList<RemovedWords>();
    ListView lstwords;
    RemovedWords removedWordHashMap;
    int count=1;
    RelativeLayout layoutid;
    LinearLayout linearLayout;
    ArrayList<String> blankWords = new ArrayList<String>();
    String changedString="";
    String blankStr = "________";
    int selectedBlankStart=0;
    int selectedBlankEnd=0;
    String resultantString="";
    TextView t2;
    int lineStart=0;
    int lineEnd=0;
    StaticLayout textViewLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(0xff99ccff);

        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... params) {
                return RandomPage.jread();
            }

            @Override
            protected void onPostExecute(String result) {
                String[] splited = result.split("\\s+");
                int rowwords=5;
                CharSequence text=null;

                HashSet<String> getListOfStopWords=null;
                try{
                    StopWordsList stopwobj=new StopWordsList();
                    getListOfStopWords=stopwobj.getList();
                }catch (Exception e){
                    e.printStackTrace();
                }


                String[] splittedarr = result.split("\\s+");
                ArrayList<String> splittedlist=new ArrayList<String>();
                for(int i=0;i<splittedarr.length;i++){
                    splittedlist.add(splittedarr[i]);
                }
                prune_str(getListOfStopWords,splittedlist);
                System.out.println("the result is  " + result);
                System.out.println("the size of the list after removing is " + splittedlist.size());
                int minval=0;
                int maxval = splittedlist.size()-1;
                    String[] lines = result.split(System.getProperty("line.separator"));
                    int wordscount1=1;
                    for(int k=0;k<lines.length;k++){
                        minval=maxval-6;
                        System.out.println("the line is " + lines[k]);
                        int startindex = result.indexOf(lines[k]);
                        System.out.println("wordscount= " + wordscount1);
                        String[] words=lines[k].split("\\s+");
                        int endindex=lines[k].indexOf(".", startindex);
                        if(wordscount1!=1){
                            endindex=wordscount1+words.length;
                        }else{
                            endindex=words.length;
                        }
                        int randval=GameActivity3.randInt(wordscount1,endindex);
                        wordscount1=words.length+1;
                        int blankPos = randval;
                        String blankWord = splittedlist.get(blankPos);
                        System.out.println("the blank word is " + blankWord);
                        int blankwordindex=lines[k].indexOf(blankWord);
                        if(blankwordindex!=-1){
                        System.out.println("blankwordindex is "+blankwordindex);
                        lines[k] = lines[k].replace(blankWord, blankStr);
                        SpannableStringBuilder ss = new SpannableStringBuilder();
                        blankWords.add(blankWord);
                        removedwordsList.add(new RemovedWords("0", blankWord));
                        ss.append(lines[k]);
                        int lengthofblankstr=blankwordindex+blankStr.length();
                        ss.setSpan(new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                open(widget);
                            }
                        }, blankwordindex, lengthofblankstr, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        createContactTextView(ss);
                            maxval=minval-1;
                    }
                }
                resultantString=result;
                text=addClickablePart(result);
            }
        }.execute();

        setContentView(linearLayout);
    }


    private SpannableStringBuilder addClickablePart(String strarg){
        final String str=strarg;
        SpannableStringBuilder ssb=new SpannableStringBuilder(str);

        int idx1=Math.min(str.indexOf("@"),str.indexOf("#") );

        int idx2=0;
        while (idx1!=-1) {
            idx2=str.indexOf(" ", idx1) + 1;
            System.out.println("index 1="+idx1);
            System.out.println("index 2="+idx2);
            final String clickString=str.substring(idx1, idx2);
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    Toast.makeText(getApplicationContext(), clickString,
                            Toast.LENGTH_SHORT).show();
                    selectedBlankStart=str.indexOf(clickString);
                    selectedBlankEnd=selectedBlankStart+clickString.length();
                    System.out.println("inside clickable span");
                    open(widget);
                }
            }, idx1, idx2, 0);
            idx1=Math.min(str.indexOf("#", idx2),str.indexOf("@", idx2) );
        }
        return ssb;
    }

    private SpannableStringBuilder changeClickablePart(String spannablestr,int startindex,int endindex){
        final String str=spannablestr;
        SpannableStringBuilder ssb=new SpannableStringBuilder(resultantString);
        System.out.println("the start index  is "+startindex);
        System.out.println("the end index  is "+endindex);
        int idx1=resultantString.indexOf("@", startindex);
        System.out.println("the word is "+str);
        System.out.println("the word index is " + idx1);
        CharSequence substr=ssb.subSequence(startindex,endindex);
        System.out.println("the char sequence is "+substr);
        // StringBuilder
        int idx2=0;
        idx2=endindex;

        ssb.replace(startindex,endindex,str);
        ssb.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                Toast.makeText(getApplicationContext(), str,
                        Toast.LENGTH_SHORT).show();

                System.out.println("inside clickable span");
                open(widget);
            }
        }, startindex, endindex, 0);
        SpannableStringBuilder newSpannedString=addClickablePart(ssb.toString());
        return newSpannedString;
    }

    void prune_str(HashSet<String> stopWords, ArrayList<String> splited){

        for(int i=0;i<splited.size();i++){
            System.out.println("the splitted word is "+splited.get(i));
            if(stopWords.contains(splited.get(i))){
                System.out.println("the removed word is "+splited.get(i));
                splited.remove(splited.get(i));
            }
        }
    }

    public void createContactTextView(CharSequence strtext){
        TextView textView1 = new TextView(this);
        textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView1.setText(strtext);

        textView1.setMovementMethod(LinkMovementMethod.getInstance());

        textView1.setLinksClickable(true);
        linearLayout.addView(textView1);
    }


    public void open(View v){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final List<Integer> mSelectedItems = new ArrayList();
        final View selectedView=v;
        LayoutInflater inflater = this.getLayoutInflater();
        final View layoutalertdialogView=inflater.inflate(R.layout.alertdialoglayout, null);
        SimpleAdapter adap=new SimpleAdapter(GameActivity3.this,removedwordsList,R.layout.listviewrow,new String[]{"word"},new int[]{R.id.txtw1});
        lstwords=(ListView)layoutalertdialogView.findViewById(R.id.lstlistofwords);
        lstwords.setAdapter(adap);
        alertDialogBuilder.setView(layoutalertdialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        lstwords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //On click listener can be implemented by getting the ViewID and placing the word in the calling view ID
                //The finish and replay buttons were implemented in the previous versions. I will follow the same logic to implement those
                RemovedWords rw=(RemovedWords)parent.getItemAtPosition(position);
                String selword=rw.get("word");
                System.out.println("the selword is=" + selword);
                String txtview=getResources().getResourceEntryName(selectedView.getId());
                System.out.println(" the textview is " + txtview);
                t2.setText(changeClickablePart(selword, selectedBlankStart, selectedBlankEnd), TextView.BufferType.SPANNABLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }


}
