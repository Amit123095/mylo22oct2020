package com.mindyourlovedone.healthcare.DashBoard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mindyourlovedone.healthcare.HomeActivity.R;
import com.mindyourlovedone.healthcare.database.DocumentQuery;
import com.mindyourlovedone.healthcare.database.EventNoteQuery;
import com.mindyourlovedone.healthcare.model.Document;
import com.mindyourlovedone.healthcare.model.Note;

public class ViewEventActivity extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    ImageView imgBack, imgEdit;
    EditText etNote;
    TextView txtDate, txtSave, txtTitle, txtDelete;
    int id, userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        //  getData();
        initUI();
        initListener();
        initComponent();
    }

    private void initComponent() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Note note = (Note) intent.getExtras().getSerializable("NoteObject");
            txtTitle.setText("Edit Event Notes");
            String notes = note.getTxtNote();
            String dates = note.getTxtDate();
            id = note.getId();
            userid = note.getUserid();
            etNote.setText(notes);
            txtDate.setText(dates);
        }
    }

    private void initListener() {
        imgBack.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        txtDelete.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        txtSave.setOnClickListener(this);
    }

    private void initUI() {
        imgBack = findViewById(R.id.imgBack);
        imgEdit = findViewById(R.id.imgEdit);
        txtDelete = findViewById(R.id.txtDelete);
        etNote = findViewById(R.id.etNote);
        txtDate = findViewById(R.id.txtDate);
        txtSave = findViewById(R.id.txtSave);
        txtTitle = findViewById(R.id.txtTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtSave:
                String note = etNote.getText().toString();
                String date = txtDate.getText().toString();
                Boolean flag = EventNoteQuery.updateEvent(id, note, date);
                if (flag == true) {
                    Toast.makeText(context, "You have updated event successfully", Toast.LENGTH_SHORT).show();
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    finish();
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.txtDelete:
                deleteNote();
                break;
        }
    }

    private void deleteNote() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Delete");
        alert.setMessage("Do you want to Delete this record?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean flags = EventNoteQuery.deleteRecord(id);
                if (flags == true) {
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alert.show();


    }
}
