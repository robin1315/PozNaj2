package wieniacy.w.kaloszach.poznaj2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import mysql-connector-java-5.1.38-bin


public class GalleryActivity extends AppCompatActivity {
    ProgressDialog dialog = null;
    StringBuffer buffer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        buffer = new StringBuffer();

        TextView tv = (TextView) findViewById(R.id.galery_tv);
        String str = "https://shielded-atoll-79606.herokuapp.com/allusers.txt";

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        dialog.show();

        new PobieranieDanych(this).execute(str);
        tv.setText(buffer.toString());
    }

    public class PobieranieDanych extends AsyncTask<String,Void,String>{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView tv = (TextView) findViewById(R.id.galery_tv);
            tv.setText(s);
            dialog.dismiss();
        }

        Activity wywolujacyActivity;

        public PobieranieDanych(Activity wywolujacy) {
            this.wywolujacyActivity = wywolujacy;
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection =(HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                String out = buffer.toString();
                return buffer.toString();

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (connection != null){
                    connection.disconnect();
                }
                try{
                    if(reader != null){
                        reader.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            return null;
        }
    }


}
