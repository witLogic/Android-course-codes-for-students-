package com.example.muthuveerappans.course2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by muthuveerappans on 7/15/17.
 */

public class SinglePuzzleFragment extends Fragment {
    private TextView question_text;
    private EditText answer_editText;

    public static SinglePuzzleFragment getInstance(int questionID, String questionText) {
        SinglePuzzleFragment singlePuzzleFragment = new SinglePuzzleFragment();

        Bundle bundle = new Bundle();
        bundle.putString("question_text", questionText);
        bundle.putInt("question_id", questionID);
        singlePuzzleFragment.setArguments(bundle);

        return singlePuzzleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_puzzle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        question_text = (TextView) view.findViewById(R.id.question_text);
        answer_editText = (EditText) view.findViewById(R.id.answer_editText);

        String questionText = getArguments().getString("question_text");
        question_text.setText(questionText);
    }

    public int getQuestionID() {
        return getArguments().getInt("question_id");
    }

    public String getResult() {
        return answer_editText.getText().toString();
    }
}
