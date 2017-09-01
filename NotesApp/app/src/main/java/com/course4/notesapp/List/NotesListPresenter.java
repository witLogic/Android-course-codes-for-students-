package com.course4.notesapp.List;

import android.content.Context;
import android.os.AsyncTask;

import com.course4.notesapp.NoteViewModal;
import com.course4.notesapp.db.Note;
import com.course4.notesapp.db.NotesCRUDHelper;

import java.util.ArrayList;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public class NotesListPresenter {
    NotesListContract contract;
    Context context;
    DataTask dataTask;
    NotesCRUDHelper notesCRUDHelper;

    public NotesListPresenter(NotesListContract contract) {
        this.contract = contract;
        this.context = (Context) contract;
        this.notesCRUDHelper = NotesCRUDHelper.getInstance(context);
    }

    public void getNotesList() {
        if (dataTask != null) {
            dataTask.cancel(true);
        }
        dataTask = new DataTask(notesCRUDHelper, new DataTask.Callback() {
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

        public DataTask(NotesCRUDHelper notesCRUDHelper, Callback callback) {
            this.callback = callback;
            this.notesCRUDHelper = notesCRUDHelper;
        }

        @Override
        protected ArrayList<NoteViewModal> doInBackground(Void... voids) {
            ArrayList<Note> noteArrayList = notesCRUDHelper.query(-1, true);
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
