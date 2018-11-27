package fall2018.csc2017.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import fall2018.csc2017.R;

public class MainActivity extends Activity {

    MainActivity main;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameEngine.is_over = false;
        main = this;
        setContentView(R.layout.activity_main2);
        GameEngine.setIsLost(false);
        Log.e("MainActivityTwo","onCreate");
        GameEngine.getInstance().createGrid(this);
        TextView txt = findViewById(R.id.bombNum);
        String str = "#BOMBS: " + Integer.toString(GameEngine.getBombNumber());
        txt.setText(str);
        addHardButtonListener();
        CountDownTimer timer = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long l) {
                GameEngine.getInstance().time += 1;
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

    private void addHardButtonListener() {

        final Button hardButton = findViewById(R.id.newGame);
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tmp = new Intent(main, MainActivity.class);
                startActivity(tmp);

                finish();
            }
        });
    }

}
