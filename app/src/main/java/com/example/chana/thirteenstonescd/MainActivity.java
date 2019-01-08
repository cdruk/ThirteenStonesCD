package com.example.chana.thirteenstonescd;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import static com.example.chana.thirteenstonescd.ThirteenStones.getGameFromJSON;
import static com.example.chana.thirteenstonescd.ThirteenStones.getJSONof;
import static com.example.chana.thirteenstonescd.Utils.showInfoDialog;

public class MainActivity extends AppCompatActivity {

    ThirteenStones mCurrentGame;
    TextView status_bar;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupFAB();

        status_bar = findViewById(R.id.tv_status_bar);
        view = findViewById(R.id.activity_main);

        startFirstGame();
    }

    private void startFirstGame() {
        mCurrentGame = new ThirteenStones();
        updateStatusBar();
    }

    private void startNextNewGame() {
        mCurrentGame.startGame();
        updateStatusBar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog(MainActivity.this, getString(R.string.info_title), mCurrentGame.getRules());
            }
        });
    }

    private void updateStatusBar() {
        status_bar.setText(mCurrentGame.getStatusBarText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_new_game) {
            startNextNewGame();
        }
        if (id == R.id.action_about){
            showInfoDialog(MainActivity.this,"About", "Chana Drukier");
        }
        return super.onOptionsItemSelected(item);
    }

    public void pick123(View view) {
        Button currentButton = (Button) view;
        try{
            mCurrentGame.takeTurn(Integer.parseInt(currentButton.getText().toString()));
            updateStatusBar();
            if(mCurrentGame.isGameOver()){
                showInfoDialog(MainActivity.this, "Game Over",
                        "The winner is player " + mCurrentGame.getCurrentOrWinningPlayerNumberOneOrTwo());
            }
        }
        catch (Exception e){
            Snackbar.make(view, e.toString(), Snackbar.LENGTH_SHORT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("GAME", getJSONof(mCurrentGame));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentGame = getGameFromJSON(savedInstanceState.getString("GAME"));
        updateStatusBar();
    }
}
