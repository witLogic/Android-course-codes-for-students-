package com.course4.notesapp.AddNotes;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.course4.notesapp.Constants;
import com.course4.notesapp.List.NoteViewModal;
import com.course4.notesapp.db.Note;
import com.course4.notesapp.db.NotesCRUDHelper;
import com.course4.notesapp.db.NotesContract;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.course4.notesapp.db.NotesContract.NoteEntry.COLUMN_NAME_CONTENT;
import static com.course4.notesapp.db.NotesContract.NoteEntry.COLUMN_NAME_TITLE;
import static com.course4.notesapp.db.NotesContract.NoteEntry.COLUMN_TIMESTAMP;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public class AddNotePresenter {
    AddNotesContract contract;
    NotesCRUDHelper notesCRUDHelper;
    InsertTask insertTask;
    ContentResolver contentResolver;

    public AddNotePresenter(AddNotesContract contract) {
        this.contract = contract;
        notesCRUDHelper = NotesCRUDHelper.getInstance((Context) contract);
        contentResolver = ((Context) contract).getContentResolver();
    }

    public void saveNote(NoteViewModal noteViewModal) {
        if (insertTask != null) {
            insertTask.cancel(true);
        }

        insertTask = new InsertTask(notesCRUDHelper, contentResolver, new InsertTask.Callback() {
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
        ContentResolver contentResolver;

        public InsertTask(NotesCRUDHelper notesCRUDHelper, ContentResolver contentResolver, Callback callback) {
            this.notesCRUDHelper = notesCRUDHelper;
            this.callback = callback;
            this.contentResolver = contentResolver;
        }

        @Override
        protected Boolean doInBackground(Note... notes) {
            Note note = notes[0];
            ArrayList<Note> noteArrayList;
            if (Constants.SHOULD_USE_PROVIDER) {
                noteArrayList = new ArrayList<>();
                if (note.id >= 0) {
                    String projection[] = new String[]{_ID, COLUMN_NAME_TITLE, COLUMN_NAME_CONTENT, COLUMN_TIMESTAMP};
                    Cursor cursor = contentResolver.query(NotesContract.NOTES_SINGLE_ROW_URI(note.id)
                            , projection, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            noteArrayList.add(new Note(cursor));
                        } while (cursor.moveToNext());
                    }
                    if (cursor != null)
                        cursor.close();
                }
            } else {
                noteArrayList = notesCRUDHelper.query(note.id, false);
            }
            boolean result = false;

            if (noteArrayList.isEmpty()) {
                // insert as there is no entry
                if (Constants.SHOULD_USE_PROVIDER) {
                    Uri uri = contentResolver.insert(NotesContract.NOTES_URI(), note.getAsContentValues());
                    result = ContentUris.parseId(uri) > 0;
                } else {
                    result = notesCRUDHelper.insert(note) >= 0;
                }
            } else {
                // update
                if (Constants.SHOULD_USE_PROVIDER) {
                    result = contentResolver.update(NotesContract.NOTES_SINGLE_ROW_URI(note.id), note.getAsContentValues(), null, null) > 0;
                } else {
                    result = notesCRUDHelper.update(note) > 0;
                }
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
