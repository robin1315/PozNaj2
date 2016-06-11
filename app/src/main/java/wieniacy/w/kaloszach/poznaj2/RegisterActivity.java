package wieniacy.w.kaloszach.poznaj2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wieniacy.w.kaloszach.poznaj2.models.ConnectionClass;

public class RegisterActivity extends AppCompatActivity {

    boolean t = true; //walidajca
    private UserRegisterTask mRegisterTask = null;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void clickRegister(View view) {

        EditText email, PasswordEditText;
        EditText age, password, password2;


        EditText mImie = (EditText) findViewById(R.id.id_rej_name);
        EditText mNaz = (EditText) findViewById(R.id.id_rej_surname);
        EditText mLog = (EditText) findViewById(R.id.id_rej_login);
        email = (EditText) findViewById(R.id.id_rej_email);
        age = (EditText) findViewById(R.id.id_rej_opis);
        password = (EditText) findViewById(R.id.editText4);
        password2 = (EditText) findViewById(R.id.editText5);


        String emailString = email.getText().toString();

        if (mLog.getText().toString() == "") {
            mLog.setError("To pole jest wymagane!");
            t = false;
        }
        if (emailString == "") {
            email.setError("To pole jest wymagane!");

            t = false;
        } else if (!isValidEmail(emailString)) {
            email.setError("Błędny adres e-mail!");
            t = false;
        }


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

        } else if (passLength < 5) {
            password.setError("Podane hasło jest zbyt krótkie!");
            t = false;
        } else if (passLength2 < 5) {
            password2.setError("Podane hasło jest zbyt krótkie!");
            t = false;
        } else if (passLength != passLength2) {
            password2.setError("Podane hasła różnią się!");
            t = false;

        }

        if (t) {
            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Proszę czekać ...");
            dialog.show();
            mRegisterTask = new UserRegisterTask((mImie.getText().toString()), (mNaz.getText().toString()), (age.getText().toString()), (password.getText().toString()), (email.getText().toString()), (mLog.getText().toString()));
            mRegisterTask.execute((Void) null);

        }
        else {
            Toast.makeText(this, "Popraw błędy!", Toast.LENGTH_SHORT).show();
            t= true;
        }
    }

    //////// Asyn tast do rejestracji konieczne
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mImie;
        private final String mNaz;
        private final String mOpis;
        private final String mLog;
        private final String mEmail;
        private final String mPassword;
        Activity mactivity;
        private int Id;

        public UserRegisterTask(String imie, String nazwisko, String opis, String password, String email, String login) {
            this.mImie = imie;
            mEmail = email;
            mPassword = password;
            mNaz = nazwisko;
            mOpis = opis;
            mLog = login;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            ConnectionClass conn = new ConnectionClass();


            try {
                String que = "insert into walenmar_poznaj.USERS (FULL_NAME, DESCRIPTION, PASSWORD, EMAIL, LOGIN)" +
                        "values ('" + mImie + " " + mNaz
                        + "', '" + mOpis + "', '" +
                        mPassword + "', '" + mEmail
                        + "', '" + mLog + "')";

                conn.makeUpdate(que);

                conn.makeQuery("Select ID from walenmar_poznaj.USERS where EMAIL = '" + mEmail + "'");

                if(conn.getResult().next()){
                    Id = Integer.parseInt(conn.getResult().getString("ID"));
                }

                return true;

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

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            dialog.dismiss();
            if (success) {

                Intent myIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                myIntent.putExtra("ID", String.valueOf(Id));
                RegisterActivity.this.startActivity(myIntent);

            }
            else
            {


                Snackbar.make(getCurrentFocus(), "Rejestracja się nie powiodła", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(myIntent);

            }
        }
    }
}
