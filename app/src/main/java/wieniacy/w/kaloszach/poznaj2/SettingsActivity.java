package wieniacy.w.kaloszach.poznaj2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wieniacy.w.kaloszach.poznaj2.models.ConnectionClass;

/**
 * Created by Rainier on 2016-05-24.
 */
public class SettingsActivity extends AppCompatActivity
{
    EditText radiusEditText;
    Button setRadiusBtn;

    EditText current_password, current_password2;
    EditText email;
    EditText age, password, password2;
    EditText mImie;
    EditText mNaz;
    EditText mLog;

    public int USERID;
    public String USERNAME;
    public String USERSURNAME;
    public String USERLOGIN;
    public String USEREMAIL;
    public String USERDESC;
    public String USERPASS;
    private boolean t = true;
    private ProgressDialog dialog;
    private UpdateUser uu;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRadiusBtn = (Button) findViewById(R.id.setRadiusButton);
        radiusEditText = (EditText) findViewById(R.id.radius);
        setRadiusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(Double.parseDouble(radiusEditText.getText().toString()) == 0)
                    LoginActivity.radius = 1;
                else
                    LoginActivity.radius = Double.parseDouble(radiusEditText.getText().toString());
                Toast.makeText(getApplicationContext(), "Pomyślnie zmieniono!", Toast.LENGTH_SHORT).show();
            }
        });


        mImie = (EditText) findViewById(R.id.id_setting_name);
        mNaz = (EditText) findViewById(R.id.id_setting_surname);
        mLog = (EditText) findViewById(R.id.id_setting_login);
        email = (EditText) findViewById(R.id.id_setting_email);
        age = (EditText) findViewById(R.id.id_setting_opis);
        password = (EditText) findViewById(R.id.id_setting_password);
        password2 = (EditText) findViewById(R.id.id_setting_password2);
        current_password = (EditText) findViewById(R.id.id_setting_current_password);
        current_password2 = (EditText) findViewById(R.id.id_setting_current_password2);

        USERID =((MyAplicationGlobal) this.getApplication()).getGlobalVarValue();
        USERNAME = ((MyAplicationGlobal) this.getApplication()).getUSERNAME();
        USERSURNAME = ((MyAplicationGlobal) this.getApplication()).getUSERSURNAME();
        USERLOGIN = ((MyAplicationGlobal) this.getApplication()).getUSERLOGIN();
        USEREMAIL = ((MyAplicationGlobal) this.getApplication()).getUSEREMAIL();
        USERDESC = ((MyAplicationGlobal) this.getApplication()).getUSERDESC();
        USERPASS = ((MyAplicationGlobal) this.getApplication()).getUSERPASS();

        mImie.setText(USERNAME);
        mNaz.setText(USERSURNAME);
        mLog.setText(USERLOGIN);
        email.setText(USEREMAIL);
        age.setText(USERDESC);

    }

    public void onClickSetRadiusBtn(View v)
    {
        LoginActivity.radius = Integer.parseInt(radiusEditText.toString());
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void clickChange2(View view) {
        String tekst = password.getText().toString();
        int passLength = password.getText().length();
        int passLength2 = password2.getText().length();
        String tekst2 = password2.getText().toString();

        if ((password.getText().toString().trim().equals(""))) {
            password.setError("Podaj hasło!");
            t = false;

        } else if ((password2.getText().toString().trim().equals(""))) {
            password2.setError("Powtórz hasło!");
            t = false;
        }
        else if (passLength < 5) {
            password.setError("Podane hasło jest zbyt krótkie!");
            t = false;
        } else if (passLength2 < 5) {
            password2.setError("Podane hasło jest zbyt krótkie!");
            t = false;
        }else if(!current_password2.getText().toString().equals(USERPASS)){
            current_password2.setError("Nieprawidłowe hasło!");
            t=false;
        }

        if (t) {
            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Proszę czekać ...");
            dialog.show();

            String q="UPDATE USERS " +
                    "SET " +//FULL_NAME='"+ mImie.getText().toString() +" " + mNaz.getText().toString() +"'"+
                    //",DESCRIPTION = '"+ age.getText().toString()+ "'"+
                    " PASSWORD = '" + tekst + "'" +
                    //",EMAIL = '"+ email.getText().toString() + "'" +
                    //",LOGIN = '" + mLog.getText().toString() + "'"+
                    "where ID=" + USERID;

            uu = new UpdateUser(q);
            uu.execute((Void) null);

        }
        else {
            Toast.makeText(this, "Popraw błędy!", Toast.LENGTH_SHORT).show();
            t= true;
        }


    }
    public void clickChange(View view){


        String emailString = email.getText().toString();

        if (mLog.getText().toString() == "") {
            mLog.setError("To pole jest wymagane!");
            t = false;
        }
        else if (emailString == "") {
            email.setError("To pole jest wymagane!");
            t = false;
        } else if (!isValidEmail(emailString)) {
            email.setError("Błędny adres e-mail!");
            t = false;
        }


        else if(!current_password.getText().toString().equals(USERPASS)){
            current_password.setError("Nieprawidłowe hasło!");
            t=false;
        }
        if (t) {
            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Proszę czekać ...");
            dialog.show();

            String q="UPDATE USERS " +
                    "SET FULL_NAME='"+ mImie.getText().toString() +" " + mNaz.getText().toString() +"'"+
                    ",DESCRIPTION = '"+ age.getText().toString()+ "'"+
                    //",PASSWORD = '" + tekst + "'" +
                    ",EMAIL = '"+ email.getText().toString() + "'" +
                    ",LOGIN = '" + mLog.getText().toString() + "'"+
                    "where ID=" + USERID;

            uu = new UpdateUser(q);
            uu.execute((Void) null);

        }
        else {
            Toast.makeText(this, "Popraw błędy!", Toast.LENGTH_SHORT).show();
            t= true;
        }


    }

    public class UpdateUser extends AsyncTask<Void, Void, Boolean> {

        String query;
        public UpdateUser( String q){

            this.query = q;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.hide();
            Toast.makeText(getApplicationContext() , "Pomyślnie dokonano zmiany!", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ConnectionClass conn = new ConnectionClass();

            try {
                conn.makeUpdate(query);



            } catch (SQLException e) {
                e.getMessage();
                e.printStackTrace();
            } finally {
                try {
                    conn.conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
// zmiana : true -> flase

            //return false;
           return true;
        }

    }
}