package fall2018.csc2017.Scoreboard;
/*
Taken from https://www.youtube.com/watch?v=cKweRL0rHBc. The video demonstrate how to create a
swipe view using fragment. Class involved are FragmentPage, FragmentPageLocal, SwipeAdapter,
SwipeAdapterLocal, scoreboard.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fall2018.csc2017.R;
import fall2018.csc2017.slidingtiles.Game_choose;
import fall2018.csc2017.slidingtiles.Score;
import fall2018.csc2017.slidingtiles.User;
import fall2018.csc2017.slidingtiles.UserManager;

import java.util.List;

public class FragmentPageLocal extends Fragment {

    UserManager current_manager = UserManager.get_instance();
    static String text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        User current_user = current_manager.getLoginUser();
        current_user.sort_score();
        List<Score> user_score = current_user.getTop_score();
        Bundle bundle = getArguments();
        int pageNumber = bundle.getInt("pageNumber");
        view = inflater.inflate(R.layout.page_fragment_layout, container, false);
        TextView scoreBoard = (TextView) view.findViewById(R.id.BoardName);
        TextView email = (TextView) view.findViewById(R.id.UserEmail);
        TextView score = (TextView) view.findViewById(R.id.Score);
        String rank;
        if (pageNumber == 1){
            rank = "1st";
        } else if (pageNumber == 2){
            rank = "2nd";
        } else if (pageNumber == 3){
            rank = "3rd";
        } else {
            rank = Integer.toString(pageNumber) + "th";
        }
        if (Game_choose.getCurrent_game().equals("sliding tiles")){
            scoreBoard.setText("Sliding Tiles" + "\n" + "Local Ranking List");
        } else if (Game_choose.getCurrent_game().equals("minesweeper")){
            scoreBoard.setText("Minesweeper" + "\n" + "Local Ranking List");
        } else {
            scoreBoard.setText("2048" + "\n" + "Local Ranking List");
        }
        if (user_score.size() < pageNumber) {
            email.setText("HOI! You need to play once to let me know your score!");
            score.setText("");
        } else {
            email.setText(rank + "   Username: " + "\n" + current_user.getUser_email());
            score.setText("Score: " + Integer.toString(current_user.getTop_score().get(pageNumber - 1).final_score));
        }

        return view;

    }

}
