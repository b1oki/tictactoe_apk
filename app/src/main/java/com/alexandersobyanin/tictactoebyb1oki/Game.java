package com.alexandersobyanin.tictactoebyb1oki;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Game extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static String[] datafield = {"", "", "", "", "", "", "", "", ""};

    private static boolean isTicMove = true;

    private static boolean speaked = false;

    ArrayAdapter<String> dataAdapter;

    GridView dataField;

    TextView infoText;

    int tic = 0;

    TextView ticScores;

    int toe = 0;

    TextView toeScores;

    TextToSpeech tts;

    private String checkGame() {
        int b1;
        for (b1 = 0; ; b1++) {
            if (b1 >= 3) {
                for (b1 = 0; ; b1++) {
                    if (b1 >= 3) {
                        if ((datafield[2].equals(datafield[4]) && datafield[4].equals(datafield[6])) || (datafield[0].equals(datafield[4]) && datafield[4].equals(datafield[8])))
                            return datafield[4];
                        break;
                    }
                    if (datafield[b1].equals(datafield[b1 + 3]) && datafield[b1].equals(datafield[b1 + 6]))
                        return datafield[b1];
                }
                break;
            }
            if (datafield[b1 * 3].equals(datafield[b1 * 3 + 1]) && datafield[b1 * 3].equals(datafield[b1 * 3 + 2]))
                return datafield[b1 * 3];
        }
        int b2 = 0;
        b1 = 0;
        while (true) {
            if (b1 >= datafield.length) {
                if (b2 >= 9)
                    return "P";
                break;
            }
            int b = b2;
            if (!datafield[b1].equals(""))
                b = b2 + 1;
            b1++;
            b2 = b;
        }
        return "N";
    }

    private void restart() {
        for (int b = 0; ; b++) {
            if (b >= datafield.length) {
                this.dataAdapter.notifyDataSetChanged();
                this.dataField.invalidateViews();
                return;
            }
            datafield[b] = "";
        }
    }

    private void speak(String paramString) {
        if (speaked)
            this.tts.speak(paramString, 0, null);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.tts.shutdown();
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        if (((TextView) paramAdapterView.getChildAt(paramInt).findViewById(R.id.one_field)).getText() == "") {
            if (isTicMove) {
                datafield[paramInt] = "X";
                isTicMove = false;
                this.infoText.setText(R.string.toe_move);
            } else {
                datafield[paramInt] = "O";
                isTicMove = true;
                this.infoText.setText(R.string.tic_move);
            }
            this.dataAdapter.notifyDataSetChanged();
            this.dataField.invalidateViews();
        } else {
            speak(getString(R.string.cellownd));
            this.infoText.setText(R.string.cellownd);
        }
        String text = checkGame();
        if (text.equals("P")) {
            speak(getString(R.string.no_win));
            this.infoText.setText(R.string.no_win);
            restart();
            return;
        }
        if (text.equals("X")) {
            speak(getString(R.string.tic_win));
            this.infoText.setText(R.string.tic_win);
            TextView textView = this.ticScores;
            StringBuilder stringBuilder = (new StringBuilder(getString(R.string.tic))).append(' ');
            paramInt = this.tic + 1;
            this.tic = paramInt;
            textView.setText(stringBuilder.append(paramInt).toString());
            restart();
            return;
        }
        if (text.equals("O")) {
            speak(getString(R.string.toe_win));
            this.infoText.setText(R.string.toe_win);
            TextView textView = this.toeScores;
            StringBuilder stringBuilder = (new StringBuilder(getString(R.string.toe))).append(' ');
            paramInt = this.toe + 1;
            this.toe = paramInt;
            textView.setText(stringBuilder.append(paramInt).toString());
            restart();
            return;
        }
        speak(getString(R.string.move));
    }

    protected void onRestoreInstanceState(Bundle paramBundle) {
        super.onRestoreInstanceState(paramBundle);
        datafield = paramBundle.getStringArray("GameField");
        isTicMove = paramBundle.getBoolean("isTicMove");
        speaked = paramBundle.getBoolean("Speaked");
        this.tic = paramBundle.getInt("Tic");
        this.toe = paramBundle.getInt("Toe");
    }

    protected void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putStringArray("GameField", datafield);
        paramBundle.putBoolean("isTicMove", isTicMove);
        paramBundle.putBoolean("Speaked", speaked);
        paramBundle.putInt("Tic", this.tic);
        paramBundle.putInt("Toe", this.toe);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.infoText = findViewById(R.id.infoText);
        this.ticScores = findViewById(R.id.ticScoresText);
        this.toeScores = findViewById(R.id.toeScoresText);
        this.dataField = findViewById(R.id.gameField);
        this.dataField.setOnItemClickListener(this);
        this.dataAdapter = new ArrayAdapter<>(this, R.layout.grid_field, R.id.one_field, datafield);
        this.dataField.setAdapter(this.dataAdapter);
        this.tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            public void onInit(int param1Int) {
            }
        });
        this.infoText.setText(R.string.welcome);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean is_speak_enabled = false;
        switch (id) {
            case R.id.menu_about:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.menu_exit:
                exitFromGame();
                break;
            case R.id.menu_speak:
                if (!speaked)
                    is_speak_enabled = true;
                speaked = is_speak_enabled;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void exitFromGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.onexitpressed).setCancelable(false).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                Game.this.finish();
            }
        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                param1DialogInterface.cancel();
            }
        });
        builder.create().show();
    }
}
