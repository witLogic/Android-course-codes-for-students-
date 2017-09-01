package com.course4.notesapp.AddNotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.course4.notesapp.NoteViewModal;
import com.course4.notesapp.R;
import com.course4.notesapp.db.Note;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public class AddNoteActivity extends AppCompatActivity implements AddNotesContract {
    EditText titleEdt;
    EditText contentEdt;
    AddNotePresenter presenter;
    int noteID = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_note_page);

        titleEdt = (EditText) findViewById(R.id.title_edt);
        contentEdt = (EditText) findViewById(R.id.content_edt);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            NoteViewModal noteViewModal = bundle.getParcelable("add_note_data");

            if (noteViewModal != null) {
                populateViews(noteViewModal.content, noteViewModal.title);
                noteID = noteViewModal.id;
            }
        }

        findViewById(R.id.save_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEdt.getText().toString();
                String content = contentEdt.getText().toString();
                NoteViewModal noteViewModal = new NoteViewModal(noteID, title, content);
                presenter.saveNote(noteViewModal);
            }
        });

        presenter = new AddNotePresenter(this);
    }

    private void populateViews(String content, String title) {
        titleEdt.setText(title);
        contentEdt.setText(content);
    }

    @Override
    public void onSaved(boolean result) {
        if (result) {
            Toast.makeText(AddNoteActivity.this, "Note saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.closeDb();
    }

}
