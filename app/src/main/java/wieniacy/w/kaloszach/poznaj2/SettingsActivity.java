package wieniacy.w.kaloszach.poznaj2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Rainier on 2016-05-24.
 */
public class SettingsActivity extends AppCompatActivity
{
    EditText radiusEditText;
    Button setRadiusBtn;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setRadiusBtn = (Button) findViewById(R.id.setRadiusButton);
        radiusEditText = (EditText) findViewById(R.id.radius);
        setRadiusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginActivity.radius = Double.parseDouble(radiusEditText.getText().toString());
            }
        });
    }

    public void onClickSetRadiusBtn(View v)
    {
        LoginActivity.radius = Integer.parseInt(radiusEditText.toString());
    }
}
