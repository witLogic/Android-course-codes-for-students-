package com.course4.notesapp.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.course4.notesapp.Constants;
import com.course4.notesapp.db.Note;
import com.course4.notesapp.db.NotesCRUDHelper;
import com.course4.notesapp.db.NotesContract;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.course4.notesapp.db.NotesContract.NOTES_TABLE_CONTENT_URI;
import static com.course4.notesapp.db.NotesContract.NoteEntry.COLUMN_NAME_CONTENT;
import static com.course4.notesapp.db.NotesContract.NoteEntry.COLUMN_NAME_TITLE;
import static com.course4.notesapp.db.NotesContract.NoteEntry.COLUMN_TIMESTAMP;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public class NotesListPresenter {
    NotesListContract contract;
    Context context;
    DataTask dataTask;
    NotesCRUDHelper notesCRUDHelper;
    ContentResolver contentResolver;

    public NotesListPresenter(NotesListContract contract) {
        this.contract = contract;
        this.context = (Context) contract;
        this.notesCRUDHelper = NotesCRUDHelper.getInstance(context);
        this.contentResolver = context.getContentResolver();
    }

    public void getNotesList() {
        if (dataTask != null) {
            dataTask.cancel(true);
        }

        dataTask = new DataTask(notesCRUDHelper, contentResolver, new DataTask.Callback() {
            @Override
            public void onDataFetched(ArrayList<NoteViewModal> noteViewModals) {
                if (context != null) {
                    contract.onNotesListFetched(noteViewModals);
                }
            }
        });
        dataTask.execute();
    }

    public void destroyDb() {
        notesCRUDHelper.closeDb();
    }

    private static class DataTask extends AsyncTask<Void, Void, ArrayList<NoteViewModal>> {
        interface Callback {
            public void onDataFetched(ArrayList<NoteViewModal> noteViewModals);
        }

        Callback callback;
        NotesCRUDHelper notesCRUDHelper;
        ContentResolver contentResolver;

        public DataTask(NotesCRUDHelper notesCRUDHelper, ContentResolver contentResolver, Callback callback) {
            this.callback = callback;
            this.notesCRUDHelper = notesCRUDHelper;
            this.contentResolver = contentResolver;
        }

        @Override
        protected ArrayList<NoteViewModal> doInBackground(Void... voids) {
            ArrayList<Note> noteArrayList;
            if (Constants.SHOULD_USE_PROVIDER) {
                String projection[] = new String[]{_ID, COLUMN_NAME_TITLE, COLUMN_NAME_CONTENT, COLUMN_TIMESTAMP};
                Cursor cursor = contentResolver.query(NotesContract.NOTES_URI(), projection, null, null, null);
                noteArrayList = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {
                        noteArrayList.add(new Note(cursor));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            } else {
                noteArrayList = notesCRUDHelper.query(-1, true);
            }
            ArrayList<NoteViewModal> result = new ArrayList<>(noteArrayList.size());
            for (Note n : noteArrayList) {
                result.add(NoteViewModal.adapter(n));
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<NoteViewModal> noteViewModals) {
            if (callback != null) {
                callback.onDataFetched(noteViewModals);
            }
        }
    }
}
