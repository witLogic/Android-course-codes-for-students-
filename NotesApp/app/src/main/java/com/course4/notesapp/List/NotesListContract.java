package com.course4.notesapp.List;

import com.course4.notesapp.NoteViewModal;

import java.util.ArrayList;

/**
 * Created by muthuveerappans on 8/30/17.
 */

public interface NotesListContract {
    void onNotesListFetched(ArrayList<NoteViewModal> noteViewModals);
}
