package com.course4.notesapp.List;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.course4.notesapp.AddNotes.AddNoteActivity;
import com.course4.notesapp.NoteViewModal;
import com.course4.notesapp.R;
import com.course4.notesapp.RecyclerItemClickListener;

import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity implements NotesListContract {
    RecyclerView recyclerView;
    ArrayList<NoteViewModal> noteViewModalList = new ArrayList<>();
    NotesListAdapter notesListAdapter;
    NotesListPresenter notesListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);

        recyclerView = (RecyclerView) findViewById(R.id.notes_recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        notesListAdapter = new NotesListAdapter();
        recyclerView.setAdapter(notesListAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this
                , new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(NotesListActivity.this, AddNoteActivity.class);
                intent.putExtra("add_note_data", noteViewModalList.get(position));
                startActivity(intent);
            }
        }));

        notesListPresenter = new NotesListPresenter(this);
    }

    @Override
    public void onNotesListFetched(ArrayList<NoteViewModal> noteViewModals) {
        if (recyclerView != null && notesListAdapter != null) {
            noteViewModalList.clear();
            noteViewModalList.addAll(noteViewModals);
            notesListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notesListPresenter.destroyDb();
    }

    private class NotesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NLVH(LayoutInflater.from(NotesListActivity.this)
                    .inflate(R.layout.single_note_card, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            NLVH vh = (NLVH) holder;
            NoteViewModal noteViewModal = noteViewModalList.get(position);
            vh.populateViews(noteViewModal.title, noteViewModal.content);
        }

        @Override
        public int getItemCount() {
            return noteViewModalList.size();
        }
    }

    private class NLVH extends RecyclerView.ViewHolder {
        TextView titleTxt;
        TextView contentTxt;

        public NLVH(View itemView) {
            super(itemView);

            titleTxt = (TextView) findViewById(R.id.title);
            contentTxt = (TextView) findViewById(R.id.content);
        }

        public void populateViews(String title, String content) {
            titleTxt.setText(title);
            contentTxt.setText(content);
        }
    }
}
