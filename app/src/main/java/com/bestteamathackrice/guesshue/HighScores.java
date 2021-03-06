package com.bestteamathackrice.guesshue;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Activity for high scores page.
 */
public class HighScores extends GlobalSettingsActivity {
    static MediaPlayer scoresMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        scoresMusic = MediaPlayer.create(HighScores.this, R.raw.scores_music);
        scoresMusic.start();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_scores, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    public void dispatchMenu(View view){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView scoreView = (TextView)findViewById(R.id.high_scores_value_list);
        TextView nameView = (TextView)findViewById(R.id.high_scores_name_list);
        scoreView.setTypeface(type);
        displayHighScores(nameView, scoreView);
    }

    public void displayHighScores(TextView nameView, TextView scoreView) {
        // Player scores are stored as strings in the format "jfk 1234"
        SharedPreferences scorePrefs = getSharedPreferences(MainActivity.GAME_PREFS, 0);
        Set<String> highScoresSet = scorePrefs.getStringSet("highScores", null);
        ArrayList<String> highScoresArray = new ArrayList<>();
        if (highScoresSet != null) {
            highScoresArray = new ArrayList<>(highScoresSet);
        }
        // The first element of the array is the names of the high scorers in descending order of
        // score, and the second is the scores
        String[] highScoresStrings = sortHighScores(highScoresArray);

        nameView.setText(highScoresStrings[0]);
        scoreView.setText(highScoresStrings[1]);

    }

    private String[] sortHighScores(ArrayList<String> highScoresArray) {
        ArrayList<PlayerScore> playerScores = new ArrayList<>();
        for (String playerScore : highScoresArray) {
            String[] tuple = playerScore.split(" ");
            playerScores.add(new PlayerScore(tuple[0], Integer.parseInt(tuple[1])));
        }
        Collections.sort(playerScores);

        String scoresString = "";
        String namesString = "";
        for (PlayerScore playerScore : playerScores) {
            scoresString += playerScore.getScore() + "\n";
            namesString += playerScore.getName() + "\n";
        }

        String[] result = {namesString, scoresString};
        return result;
    }


    @Override
    protected void onStop() {
        super.onStop();
        scoresMusic.stop();
    }
}


