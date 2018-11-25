package fall2018.csc2017.game2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import fall2018.csc2017.R;

public class MainActivityTwo extends Activity {

    public static MainActivityTwo mainActivity = null;
    private TextView Score;
    public static int score = 0;//当前得分
    private TextView maxScore;
    private ImageView share;
    private Button restart;
    private Button pause;
    private GameView gameView;
    /**
     * The maximum step the player can use undo.(default 3)
     */
    private static int undoStep = 3;

    /**
     * Set the maximum step to i
     *
     * @param i the maximum step
     */

    public static void setUndoStep(int i) {
        undoStep = i;
    }

    /**
     * Get the maximum step of undo.
     *
     * @return the maximum step of undo.
     */

    public static int getUndoStep() {
        return undoStep;
    }

    public MainActivityTwo() {
        mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Score = (TextView) findViewById(R.id.Score);
        maxScore = (TextView) findViewById(R.id.maxScore);
        maxScore.setText(getSharedPreferences("pMaxScore", MODE_PRIVATE).getInt("maxScore", 0) + "");
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String s = "我在“2048”游戏中的得分为" + maxScore.getText() + "，你敢来挑战吗？点击进入>>http://www.amazon.cn/dp/B01KV5AK2Q";
                shareIntent.putExtra(Intent.EXTRA_TEXT, s);
                startActivity(Intent.createChooser(shareIntent, "炫耀一下"));
            }
        });
        gameView = (GameView)findViewById(R.id.gameView);
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameView.startGame();
            }
        });

        addUndoButtonListener();
        pause = (Button)findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });

    }

    public static MainActivityTwo getMainActivity() {
        return mainActivity;
    }

    //分数清零
    public void clearScore() {
        score = 0;
        showScore();
    }

    //分数增加
    public void addScore(int i) {

        score += i;
        showScore();
        SharedPreferences pref = getSharedPreferences("pMaxScore", MODE_PRIVATE);

        //若当前得分超出最高记录，则更新之
        if (score > pref.getInt("maxScore", 0)) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("maxScore", score);
            editor.commit();
            maxScore.setText(pref.getInt("maxScore", 0) + "");
        }

    }
    /**
     * Add the Undo bottom.
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.back);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameView.getStateList().size() == 0) {
                    makeToastUndo();
                }
                if (gameView.hasTouched && gameView.getStateList().size() >= 1) {
                    score = gameView.getScoreList().get(gameView.getScoreList().size() - 1);
                    gameView.getScoreList().remove(gameView.getScoreList().size() - 1);
                    showScore();
                    int[][] newState =
                            gameView.getStateList().get(gameView.getStateList().size() - 1);
                    gameView.getStateList().remove(gameView.getStateList().size() - 1);
                    for (int y = 0; y < 4; ++y) {
                        for (int x = 0; x < 4; ++x) {
                            gameView.cards[y][x].setNum(newState[y][x]);
                        }
                    }
                }
            }
        });
    }

    /**
     * Display that the player can't undo anymore.
     */
    private void makeToastUndo() {
        Toast.makeText(this, "Can't undo any more!", Toast.LENGTH_SHORT).show();
    }

    //显示当前得分
    public void showScore() {
        Score.setText(score + "");
    }

    @Override
    public void onBackPressed() {
        createExitTipDialog();
    }

    private void createExitTipDialog() {
        new AlertDialog.Builder(MainActivityTwo.this)
                .setMessage("确认退出吗？")
                .setTitle("提示")
                .setIcon(R.drawable.tip)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

}
