package lk.peruma.schedulenotificationsample;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText textTitle, textDescription, textID;
    Button buttonSave, buttonReset;
    DatePicker datePicker;
    TimePicker timePicker;
    Date reminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textID = (EditText) findViewById(R.id.textID);

        textTitle = (EditText) findViewById(R.id.textTitle);
        textDescription = (EditText) findViewById(R.id.textDescription);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonReset = (Button) findViewById(R.id.buttonReset);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        Calendar newDateTime =  Calendar.getInstance();
        reminder = newDateTime.getTime();

        ResetFields();

        buttonReset.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ResetFields();
                    }
                }
        );


        buttonSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int id = Integer.parseInt(textID.getText().toString());
                        String title = textTitle.getText().toString();
                        String description = textDescription.getText().toString();
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        int hour = timePicker.getCurrentHour();
                        int minutes = timePicker.getCurrentMinute();

                        Calendar notificationReminder = Calendar.getInstance();
                        notificationReminder.set(year,month,day,hour,minutes);
                        reminder = notificationReminder.getTime();

                        AlarmManager alarmManager = (AlarmManager) getSystemService(getBaseContext().ALARM_SERVICE);

                        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                        notificationIntent.addCategory("android.intent.category.DEFAULT");

                        notificationIntent.putExtra("Notification", buildNotification(title,description,id));
                        notificationIntent.putExtra("NotificationDate",reminder);
                        notificationIntent.putExtra("NotificationID",id);

                        Integer x = reminder.getSeconds();
                        PendingIntent broadcast = PendingIntent.getBroadcast(getBaseContext(), x, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(System.currentTimeMillis());
                        cal.clear();
                        cal.set(Calendar.YEAR,year);
                        cal.set(Calendar.MONTH,month);
                        cal.set(Calendar.DATE,day);
                        cal.set(Calendar.HOUR_OF_DAY,hour);
                        cal.set(Calendar.MINUTE,minutes);

                        //cal.add(Calendar.SECOND, 4);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

                        Toast.makeText(MainActivity.this, "Notification has been scheduled for: "+reminder.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );


    }

    private Notification buildNotification(String Title, String Description, Integer ID){
        Intent notificationIntent = new Intent(this, NotificationActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("TODO: "+Title);
        if (Description.length()!=0) {
            builder.setContentText(Description);
            builder.setStyle(new Notification.BigTextStyle().bigText(Description));
        }
        builder.setSmallIcon(android.R.drawable.star_on);
        builder.setTicker("You have a TODO!");
        builder.setAutoCancel(true);

        Drawable largeIcon = getResources().getDrawable(android.R.drawable.star_big_on);
        builder.setLargeIcon((((BitmapDrawable)largeIcon).getBitmap()));

        builder.setContentIntent(pendingIntent);

        Intent actionIntent1 = new Intent(this,NotificationActivity.class);
        actionIntent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        actionIntent1.setAction("Email");
        PendingIntent actionPeningIntent1 = PendingIntent.getActivity(this,0,actionIntent1,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(android.R.drawable.sym_action_email,"You've got mail!",actionPeningIntent1);

        Intent actionIntent2 = new Intent(this,NotificationActivity.class);
        actionIntent2.setAction("Call");
        actionIntent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent actionPeningIntent2 = PendingIntent.getActivity(this,1,actionIntent2,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(android.R.drawable.sym_action_call,"Hello.Hello",actionPeningIntent2);

        Intent actionIntent3 = new Intent(this,NotificationActionReceiver.class);
        actionIntent3.putExtra("NotificationID",ID);

        actionIntent3.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent actionPeningIntent3 = PendingIntent.getBroadcast(this,2,actionIntent3,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(android.R.drawable.ic_menu_close_clear_cancel,"Close",actionPeningIntent3);


        return builder.build();
    }

    private void ResetFields(){
        int random = (int)(Math.random()*Integer.MAX_VALUE + 0);
        textID.setText(String.valueOf(random));
        textTitle.setText("");
        textDescription.setText("");

        Calendar reset = Calendar.getInstance();
        datePicker.updateDate(reset.get(Calendar.YEAR),reset.get(Calendar.MONTH),reset.get(Calendar.DAY_OF_MONTH));
        timePicker.setCurrentHour(reset.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(reset.get(Calendar.MINUTE));

        reminder = reset.getTime();
    }
}
