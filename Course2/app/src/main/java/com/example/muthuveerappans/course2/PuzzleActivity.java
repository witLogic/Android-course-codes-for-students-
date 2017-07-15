package com.example.muthuveerappans.course2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by muthuveerappans on 7/14/17.
 */

public class PuzzleActivity extends AppCompatActivity
        implements View.OnClickListener {
    private TextView left_textView;

    private Button submit_button;

    private SinglePuzzleFragment singlePuzzleFragment;

    private int currentQuestionID = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.puzzle_activity);

        left_textView = (TextView) findViewById(R.id.left_textView);

        submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(this);

        puzzleData(currentQuestionID, true);
    }

    private void updateQuestionCount() {
        left_textView.setText("O" + currentQuestionID);
    }

    /**
     * Helper to get the puzzle data tagged to a ID.
     *
     * @param questionID      The ID of the puzzle.
     * @param addPuzzleToView If true adds the puzzle to this view. Else returns the data.
     * @return The puzzle data for the given ID.
     */
    private String[] puzzleData(int questionID, boolean addPuzzleToView) {
        String[] puzzleData = Data.getPuzzleData(questionID);

        if (puzzleData == null) {
            Log.e("PuzzleActivity", "No data for the given ID " + currentQuestionID);
        } else if (addPuzzleToView) {
            addPuzzle(Integer.valueOf(puzzleData[0]), puzzleData[1]);
        }

        return puzzleData;
    }

    /**
     * Adds the puzzle to the view. This done using a fragment. To learn more about fragments visit
     * https://developer.android.com/guide/components/fragments.html
     *
     * @param questionID
     * @param questionText
     */
    private void addPuzzle(int questionID, String questionText) {
        updateQuestionCount();

        singlePuzzleFragment = SinglePuzzleFragment.getInstance(questionID, questionText);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.puzzle_fragment, singlePuzzleFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private boolean checkResult(int questionID, String result) {
        String[] puzzleData = puzzleData(questionID, false);
        String puzzleResult = puzzleData[2];

        return puzzleResult.equals(result);
    }

    private boolean isPuzzleOver() {
        return currentQuestionID > Data.getPuzzleCount();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_button) {

            if (singlePuzzleFragment != null) {

                String answer = singlePuzzleFragment.getResult();
                int questionID = singlePuzzleFragment.getQuestionID();

                boolean result = checkResult(questionID, answer);

                // if the result is true, move to next puzzle.
                if (result) {

                    currentQuestionID += 1;
                    puzzleData(currentQuestionID, true);

                    Toast.makeText(PuzzleActivity.this
                            , "Correct answer", Toast.LENGTH_SHORT).show();

                    // Check if the user has completed all the questions in the puzzle.
                    if (isPuzzleOver()) {
                        Toast.makeText(PuzzleActivity.this, "Puzzle over.", Toast.LENGTH_SHORT).show();
                        submit_button.setEnabled(false);
                    }
                } else {
                    Toast.makeText(PuzzleActivity.this
                            , "The answer is incorrect. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
