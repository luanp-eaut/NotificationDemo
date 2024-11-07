package eaut.it.mobileappdev.notificationdemo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class TapActionView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tap_action_view);

        TextView textView = findViewById(R.id.textView);
        String message=getIntent().getStringExtra("message");
        textView.setText(message);
    }
}