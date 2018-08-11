package android.zersey.expense_manger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SlidingDrawer;

public class Specific_Transactions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific__transactions);
        SlidingDrawer simpleSlidingDrawer = (SlidingDrawer) findViewById(R.id.simpleSlidingDrawer);
    }
}
