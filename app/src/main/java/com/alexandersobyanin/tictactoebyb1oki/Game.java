package com.alexandersobyanin.tictactoebyb1oki;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Game extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String symbolForX = "X";
    private static final String symbolForO = "O";
    private static final String symbolForPat = "P";

    private static String[] dataFields = {"", "", "", "", "", "", "", "", ""};

    private static boolean isTicMove = true;

    private static boolean isSpoken = false;

    ArrayAdapter<String> dataAdapter;

    GridView dataFieldGridView;

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
                        Log.d("Diagonal lines", dataFields[2] + "-" + dataFields[4] + "-" + dataFields[6] + " - " + dataFields[0] + "-" + dataFields[4] + "-" + dataFields[8]);
                        if (!dataFields[4].equals("") && ((dataFields[2].equals(dataFields[4]) && dataFields[4].equals(dataFields[6])) || (dataFields[0].equals(dataFields[4]) && dataFields[4].equals(dataFields[8]))))
                            return dataFields[4];
                        break;
                    }
                    Log.d("Vertical line №" + b1, dataFields[b1] + "-" + dataFields[b1 + 3] + "-" + dataFields[b1 + 6]);
                    if (!dataFields[b1].equals("") && dataFields[b1].equals(dataFields[b1 + 3]) && dataFields[b1].equals(dataFields[b1 + 6]))
                        return dataFields[b1];
                }
                break;
            }
            Log.d("Horizontal line №" + b1, dataFields[b1 * 3] + "-" + dataFields[b1 * 3 + 1] + "-" + dataFields[b1 * 3 + 2]);
            if (!dataFields[b1 * 3].equals("") && dataFields[b1 * 3].equals(dataFields[b1 * 3 + 1]) && dataFields[b1 * 3].equals(dataFields[b1 * 3 + 2]))
                return dataFields[b1 * 3];
        }
        int b2 = 0;
        b1 = 0;
        while (true) {
            if (b1 >= dataFields.length) {
                if (b2 >= 9)
                    return symbolForPat;
                break;
            }
            int b = b2;
            if (!dataFields[b1].equals(""))
                b = b2 + 1;
            b1++;
            b2 = b;
        }
        return "N";
    }

    private void restart() {
        for (int b = 0; ; b++) {
            if (b >= dataFields.length) {
                this.dataAdapter.notifyDataSetChanged();
                this.dataFieldGridView.invalidateViews();
                return;
            }
            dataFields[b] = "";
        }
    }

    private void speak(String paramString) {
        if (isSpoken) {
            this.tts.speak(paramString, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.tts.shutdown();
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        if (((TextView) paramAdapterView.getChildAt(paramInt).findViewById(R.id.one_field)).getText() == "") {
            if (isTicMove) {
                dataFields[paramInt] = symbolForX;
                isTicMove = false;
                this.infoText.setText(R.string.toe_move);
            } else {
                dataFields[paramInt] = symbolForO;
                isTicMove = true;
                this.infoText.setText(R.string.tic_move);
            }
            this.dataAdapter.notifyDataSetChanged();
            this.dataFieldGridView.invalidateViews();
        } else {
            speak(getString(R.string.cell_owned));
            this.infoText.setText(R.string.cell_owned);
        }
        String text = checkGame();
        Log.d("checkGame result", text);
        if (text.equals(symbolForPat)) {
            speak(getString(R.string.no_win));
            this.infoText.setText(R.string.no_win);
            restart();
            return;
        }
        if (text.equals(symbolForX)) {
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
        if (text.equals(symbolForO)) {
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

    protected void onRestoreInstanceState(@NonNull Bundle paramBundle) {
        super.onRestoreInstanceState(paramBundle);
        dataFields = paramBundle.getStringArray("GameField");
        isTicMove = paramBundle.getBoolean("isTicMove");
        isSpoken = paramBundle.getBoolean("Spoken");
        this.tic = paramBundle.getInt("Tic");
        this.toe = paramBundle.getInt("Toe");
    }

    protected void onSaveInstanceState(@NonNull Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putStringArray("GameField", dataFields);
        paramBundle.putBoolean("isTicMove", isTicMove);
        paramBundle.putBoolean("Spoken", isSpoken);
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
        this.dataFieldGridView = findViewById(R.id.gameField);
        this.dataFieldGridView.setOnItemClickListener(this);
        this.dataAdapter = new ArrayAdapter<>(this, R.layout.grid_field, R.id.one_field, dataFields);
        this.dataFieldGridView.setAdapter(this.dataAdapter);
        this.tts = new TextToSpeech(this, param1Int -> {});
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
        if (id == R.id.menu_about) {
            startActivity(new Intent(this, About.class));
        } else if (id == R.id.menu_exit) {
            exitFromGame();
        } else if (id == R.id.menu_speak) {
            if (!isSpoken)
                is_speak_enabled = true;
            isSpoken = is_speak_enabled;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void exitFromGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.on_exit_pressed).setCancelable(false).setPositiveButton(android.R.string.yes, (param1DialogInterface, param1Int) -> Game.this.finish()).setNegativeButton(android.R.string.no, (param1DialogInterface, param1Int) -> param1DialogInterface.cancel());
        builder.create().show();
    }
}