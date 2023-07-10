package com.example.helloworld;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;
public class MainActivity extends AppCompatActivity {

    private EditText questionEditText;
    private EditText answerEditText;
    private Button submitButton;
    private TextView datasetTextView;
    private EditText passwordEditText;
    private SeekBar seekBar;
    private Button viewDatasetButton;
    private Button shareButton;
    private Button clearDatasetButton;
    private Button musicButton;
    private MediaPlayer mediaPlayer;
    private int resumePosition;

    private final String PASSWORD = "aassdd";
    private final String DATASET_FILENAME = "dataset.txt";
    private final String MUSIC_FILENAME = "song1.mp3";
    private TextView nowPlayingTextView;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar= findViewById(R.id.seek_bar);

        questionEditText = findViewById(R.id.question_edit_text);
        answerEditText = findViewById(R.id.answer_edit_text);
        submitButton = findViewById(R.id.submit_button);
        datasetTextView = findViewById(R.id.dataset_text_view);
        passwordEditText = findViewById(R.id.password_edit_text);
        viewDatasetButton = findViewById(R.id.view_dataset_button);
        shareButton = findViewById(R.id.share_button);
        clearDatasetButton = findViewById(R.id.clear_dataset_button);



        musicButton = findViewById(R.id.music_button);

        mediaPlayer = MediaPlayer.create(this, R.raw.song1);
        nowPlayingTextView = findViewById(R.id.now_playing_text_view);



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read the contents of the dataset file
                String dataset = readDatasetFile();

                // Get the user's question and answer
                String question = questionEditText.getText().toString().trim();
                String answer = answerEditText.getText().toString().trim();

                // Check that both question and answer are not empty
                if (!question.isEmpty() && !answer.isEmpty()) {
                    // Append the user's question and answer to the dataset
                    dataset += question + "       " + answer + "\n";

                    // Write the updated dataset back to the file
                    writeDatasetFile(dataset);

                    // Clear the EditText views
                    questionEditText.setText("");
                    answerEditText.setText("");

                    // Enable the activity2Button
                    Button activity2Button = findViewById(R.id.activity2_button);
                    activity2Button.setEnabled(true);
                } else {
                    // Notify the user that they cannot submit an empty question or answer
                    Toast.makeText(MainActivity.this, "Please enter both question and answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

// Set the OnClickListener for activity2Button
        Button activity2Button = findViewById(R.id.activity2_button);
        activity2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                startActivity(intent);
            }
        });


        viewDatasetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();
                if (password.equals(PASSWORD)) {
                    String dataset = readDatasetFile();
                    datasetTextView.setText(dataset);
                    passwordEditText.setText("");
                    shareButton.setVisibility(View.VISIBLE);
                    clearDatasetButton.setVisibility(View.VISIBLE);
                } else {
                    datasetTextView.setText("Incorrect password");
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();
                if (password.equals(PASSWORD)) {
                    String dataset = readDatasetFile().trim();
                    shareDataset(MainActivity.this, dataset);
                    passwordEditText.setText("");
                } else {
                    datasetTextView.setText("Incorrect password");
                }
            }
        });

        clearDatasetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();
                if (password.equals(PASSWORD)) {
                    writeDatasetFile("");
                    datasetTextView.setText("");
                    passwordEditText.setText("");
                    shareButton.setVisibility(View.GONE);
                    clearDatasetButton.setVisibility(View.GONE);
                } else {
                    datasetTextView.setText("Incorrect password");
                }
            }
        });

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    musicButton.setText("Play");
                    nowPlayingTextView.setText("Play to listen to music");
                } else {
                    mediaPlayer.start();
                    musicButton.setText("Pause");
                    nowPlayingTextView.setText("The music is playing");

                    // Set the maximum value of the seek bar to the duration of the media player
                    seekBar.setMax(mediaPlayer.getDuration());

                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView timerTextView = findViewById(R.id.timer_text_view);
                                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                                    timerTextView.setText(String.format("%02d:%02d", currentPosition / 60, currentPosition % 60));

                                    // Update the seek bar's progress to match the current position of the media player
                                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                }
                            });
                        }
                    }, 0, 1000);

                    // Set up an OnSeekBarChangeListener to handle user interaction with the seek bar
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                // If the user has dragged the seek bar, set the media player's position to match the progress
                                mediaPlayer.seekTo(progress);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {}
                    });
                }
            }
        });
    }

    private void shareDataset(Context context, String dataset) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, dataset);
        context.startActivity(Intent.createChooser(intent, "Share dataset"));
    }

    private String readDatasetFile() {
        String dataset = "";
        try {
            FileInputStream inputStream = openFileInput(DATASET_FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                dataset += line + "\n";
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    private void writeDatasetFile(String dataset) {
        try {
            FileOutputStream outputStream = new FileOutputStream(getDatasetFile());
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write(dataset);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDatasetFile() {
        return new File(getFilesDir(), DATASET_FILENAME);
    }

    //    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            isPlaying = mediaPlayer.isPlaying();
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
//            mediaPlayer.seekTo(resumePosition);
            //mediaPlayer.start();
        }
    }

}

