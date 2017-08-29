package com.course4.notesapp.AddNotes;

import android.content.Context;
import android.os.AsyncTask;

import com.course4.notesapp.NoteViewModal;
import com.course4.notesapp.db.Note;
import com.course4.notesapp.db.NotesCRUDHelper;

import java.util.ArrayList;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public class AddNotePresenter {
    AddNotesContract contract;
    NotesCRUDHelper notesCRUDHelper;
    InsertTask insertTask;

    public AddNotePresenter(AddNotesContract contract) {
        this.contract = contract;
        notesCRUDHelper = NotesCRUDHelper.getInstance((Context) contract);
    }

    public void saveNote(NoteViewModal noteViewModal) {
        if (insertTask != null) {
            insertTask.cancel(true);
        }

        insertTask = new InsertTask(notesCRUDHelper, new InsertTask.Callback() {
            @Override
            public void onDone(boolean result) {
                if (contract != null) {
                    contract.onSaved(result);
                }
            }
        });

        insertTask.execute(Note.adapter(noteViewModal));
    }

    public void closeDb() {
        notesCRUDHelper.closeDb();
    }

    private static class InsertTask extends AsyncTask<Note, Void, Boolean> {
        interface Callback {
            void onDone(boolean result);
        }

        NotesCRUDHelper notesCRUDHelper;
        Callback callback;

        public InsertTask(NotesCRUDHelper notesCRUDHelper, Callback callback) {
            this.notesCRUDHelper = notesCRUDHelper;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Note... notes) {
            Note note = notes[0];
            ArrayList<Note> noteArrayList = notesCRUDHelper.query(note.id);
            boolean result = false;

            if (noteArrayList.isEmpty()) {
                // insert as there is no entry
                result = notesCRUDHelper.insert(note) >= 0;
            } else {
                // update
                result = notesCRUDHelper.update(note) > 0;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (callback != null) {
                callback.onDone(aBoolean);
            }
        }
    }
}
