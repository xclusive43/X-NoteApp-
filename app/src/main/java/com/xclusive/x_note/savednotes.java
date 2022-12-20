package com.xclusive.x_note;

import static com.xclusive.x_note.MainActivity.color;
import static com.xclusive.x_note.MainActivity.update;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.xclusive.x_note.Adapter.Adapter_notes;
import com.xclusive.x_note.SQlite.DataBase;
import com.xclusive.x_note.model.Model_notes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class savednotes extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static Adapter_notes Adapter_notes;
    private ArrayList<Model_notes> model_notes = new ArrayList<>();
    private RecyclerView recyclerView1;
    private TextView noteCounts;
    public static Cursor cursor;
    public static DataBase DB;
    public Model_notes Deletedfile = null;
    private SearchView searchView;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savednotes);
        setItems();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView1);
        searchView.setOnQueryTextListener(this);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems() {
        model_notes = new ArrayList<>();
        DB = new DataBase(this);
        recyclerView1 = findViewById(R.id.recyclerview1);
        searchView = findViewById(R.id.searchView);
        noteCounts = findViewById(R.id.noteCounts);
        recyclerView1.setHasFixedSize(true);
        //layout for different size;
        StaggeredGridLayoutManager linearLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView1.setLayoutManager(linearLayoutManager);
        cursor = DB.getAlldata();
        noteCounts.setText(String.valueOf(cursor.getCount()));
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                model_notes.add(new Model_notes(cursor.getString(0), cursor.getString(5), cursor.getString(6), cursor.getString(1)
                        , cursor.getString(4), cursor.getString(2)));
            }
        } else {
            Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
        }
        Collections.reverse(model_notes);
        Adapter_notes = new Adapter_notes(model_notes, savednotes.this);
        recyclerView1.setAdapter(Adapter_notes);
        Adapter_notes.notifyDataSetChanged();
    }

    public void addnotesIntent(View view) {
        update = 0;
        color = "#FCF3F3F3";
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("TASK", 0);
        intent.putExtra("TITLE", "");
        intent.putExtra("SUBTITLE", "");
        intent.putExtra("NOTE", "");
        intent.putExtra("DATE", "");
        intent.putExtra("COLOR", "");
        intent.putExtra("ID", "");
        startActivity(intent);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            Deletedfile = null;
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    Deletedfile = model_notes.get(pos);
                    model_notes.remove(pos);
                    Adapter_notes.notifyItemRemoved(pos);
                    Snackbar.make(recyclerView1, "Note: " + Deletedfile.getTitle().toString(), Snackbar.LENGTH_LONG)
                            .setAction("Undo", v -> {
                                Model_notes deleteedtemp = Deletedfile;
                                Adapter_notes.notifyItemInserted(pos);
                                DB.insertdata(deleteedtemp.getId(), deleteedtemp.getTitle(), deleteedtemp.getSubtitle(), "",
                                        deleteedtemp.getDates(), deleteedtemp.getNotes()
                                        , deleteedtemp.getColor(), 0);
                                setItems();
                            }).show();
                    DB.deletedata(Deletedfile.getId());
                    setItems();
                    break;
                case ItemTouchHelper.RIGHT:
                    Deletedfile = model_notes.get(pos);
                    StringBuilder sharedata = new StringBuilder();
                    sharedata.append("TITLE: ").append(Deletedfile.getTitle()).append("\n").append("SUBTITLE: ").append(Deletedfile.getSubtitle()).append("\n").append("DATE: ").append(Deletedfile.getDates()).append("\n").append("NOTES: ").append(Deletedfile.getNotes());

                    try {
                        FileOutputStream fileOutputStream = savednotes.this.openFileOutput(Deletedfile.getTitle() + ".txt", MODE_PRIVATE);
                        fileOutputStream.write(sharedata.toString().getBytes());
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    File file = new File(savednotes.this.getFilesDir(), Deletedfile.getTitle() + ".txt");
                    Uri path = FileProvider.getUriForFile(savednotes.this, "com.xclusive.x_note.fileprovider", file);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/txt");
                    intent.putExtra(Intent.EXTRA_SUBJECT, file);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_STREAM, path);
                    startActivity(intent);
                    Adapter_notes.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(savednotes.this, R.color.transparent))
                    .addSwipeLeftActionIcon(R.drawable.delete)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(savednotes.this, R.color.transparent))
                    .addSwipeRightActionIcon(R.drawable.share)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onQueryTextChange(String newText) {
        String input = newText.toLowerCase();
        ArrayList<Model_notes> searchlist = new ArrayList<>();

        for (Model_notes notes : model_notes) {
            if (notes.getTitle().toLowerCase().contains(input)) {
                searchlist.add(notes);
            }
        }
        Adapter_notes.updatelist(searchlist);
        Adapter_notes.notifyDataSetChanged();
        return true;
    }

    public void aboutsection(View view) {
        startActivity(new Intent(getApplicationContext(), About_us.class));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        Log.e("DATA", "EXEC0");
        setItems();
        super.onResume();
    }
}