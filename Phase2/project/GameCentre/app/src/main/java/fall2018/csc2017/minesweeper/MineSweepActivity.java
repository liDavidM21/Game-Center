package fall2018.csc2017.minesweeper;
/**
 * https://evgenii.com/blog/spring-button-animation-on-android/
 * https://stackoverflow.com/questions/36894384/android-move-background-continuously-with-animation
 * https://stackoverflow.com/questions/9107900/how-to-upload-image-from-gallery-in-android
 */


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import fall2018.csc2017.R;
import fall2018.csc2017.slidingtiles.LoginActivity;
import fall2018.csc2017.slidingtiles.scoreboard;
import fall2018.csc2017.slidingtiles.Usermanager;
import fall2018.csc2017.slidingtiles.GameChoosing;
/**
 * The initial activity for the sliding puzzle tile game.
 */
public class MineSweepActivity extends AppCompatActivity {

    /**
     * The main save file.
     */
    public static final String SAVE_FILENAME = "save_file.ser";
    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "save_file_tmp.ser";
    /**
     * A temporary save file.
     */
    public static final String AUTO_SAVE_FILENAME = "save_file_auto.ser";
    /**
     * The board manager.
     */
    private static int showDefault = 1;
    private GameEngine gameengine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serializeUserManager();
        super.onCreate(savedInstanceState);
        gameengine = GameEngine.getInstance();
        if (showDefault == 1) {
            makeToastModeText();
            showDefault++;
        }
        saveToFile(TEMP_SAVE_FILENAME);
        saveToFile(AUTO_SAVE_FILENAME);
        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
        addSettingButtonListener();
        addScoreboardButtonListener();
        addSignoutButtonListener();
        addExitButtonListener();
    }

    /**
     * Start a new game.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameengine = GameEngine.getInstance();
                switchToGame("start");
            }
        });
    }

    /**
     * Button for entering scoreboard interface.
     */
    private void addScoreboardButtonListener() {
        Button startButton = findViewById(R.id.scoreboardButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToScoreboard();
            }
        });
    }

    /**
     * Button listener for game setting.
     * Enter activity_setting interface.
     */
    private void addSettingButtonListener() {
        Button startButton = findViewById(R.id.SETTING);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSetting();
            }
        });
    }

    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastLoadedText("Loading game");
                switchToGame("load");
            }
        });
    }

    /**
     * Attempt to sign out
     */
    private void addSignoutButtonListener() {
        Button loadButton = findViewById(R.id.Signout);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
            }
        });
    }

    private void signout() {
        Intent tmp = new Intent(this, LoginActivity.class);
        startActivity(tmp);
    }

    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        final Button saveButton = findViewById(R.id.SaveButton);
        final MineSweepActivity tmp1 = this;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastSavedText();
                final Animation myAnim = AnimationUtils.loadAnimation(tmp1, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                saveButton.startAnimation(myAnim);
            }
        });
    }

    /**
     * Display that a game was saved successfully.
     */
    private void makeToastSavedText() {
        Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(TEMP_SAVE_FILENAME);
    }

    /**
     * Switch to the GameActivity view to play the game.
     */
    private void switchToGame(String s) {
        if (s == "start") {
            Button button = (Button) findViewById(R.id.StartButton);
            startAnimation(button);
        } else {
            Button button = (Button) findViewById(R.id.LoadButton);
            startAnimation(button);
        }
    }

    /**
     * Animation added for start button.
     *
     * @param button
     */
    private void startAnimation(Button button) {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 20);
        myAnim.setInterpolator(interpolator);
        button.startAnimation(myAnim);
        final MineSweepActivity tmp1 = this;
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent tmp = new Intent(tmp1,MainActivity.class);
                saveToFile(MineSweepActivity.TEMP_SAVE_FILENAME);
                startActivity(tmp);
            }
        });
    }

    /**
     * Switch to activity_setting interface.
     */
    private void switchToSetting() {
        final MineSweepActivity tmp1 = this;
        Intent tmp = new Intent(tmp1, GameSettingMinesweeper.class);
        saveToFile(MineSweepActivity.TEMP_SAVE_FILENAME);
        startActivity(tmp);
    }
    /**
     * Switch to Scoreboard interface.
     */
    private void switchToScoreboard() {
        Intent tmp = new Intent(this, scoreboard.class);
        startActivity(tmp);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                gameengine = (GameEngine) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Display the default mode -- medium.
     */
    private void makeToastModeText() {
        Toast.makeText(this, "DEFAULT MODE: MEDIUM", Toast.LENGTH_SHORT).show();
    }


    /**
     * Display undo setting configuration: maximum undo steps selected by user.
     */

    /**
     * Button for go back to game choosing selection layout.
     */
    private void addExitButtonListener() {
        Button exitButton = findViewById(R.id.backTwo);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSelecting();
            }
        });
    }

    /**
     * Switch to game choosing interface.
     * Used by ExitButtonListener.
     */
    private void switchToSelecting() {
        Intent tmp = new Intent(this, GameChoosing.class);
        startActivity(tmp);
    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(gameengine);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void serializeUserManager() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput("UserManager.ser", MODE_PRIVATE));
            outputStream.writeObject(Usermanager.get_instance());
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}