package lk.peruma.schedulenotificationsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        text = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();
        String intentText = intent.getAction();
        text.setText("You selected: "+ intentText);

    }
}
