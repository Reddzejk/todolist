package pl.red.todolist.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.red.todolist.R;
import pl.red.todolist.service.AttachmentAdapter;

public class AttachmentActivity extends Activity {
    RecyclerView rv;
    AttachmentAdapter attachmentAdapter;
    List<Uri> attachments;
    boolean show = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attachment_view);
        attachments = (List<Uri>) getIntent().getExtras().getSerializable("attachmentList");
        show = getIntent().getBooleanExtra("show", false);
        attachmentAdapter = new AttachmentAdapter(attachments,show);
        rv = findViewById(R.id.attachments);
        rv.setAdapter(attachmentAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        if (show) {
            findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
        }
    }


    public void addAttachment(View view) {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select File"), 4);
    }

    @Override
    public void onBackPressed() {
        if (show) {
            super.onBackPressed();
            return;
        }

        Intent intent = new Intent(getApplicationContext(), AddEditTaskActivity.class);
        intent.putExtra("attachments", (ArrayList<Uri>) attachments);
        setResult(Activity.RESULT_OK, intent);
        IsFinish("Save changes?");
    }

    public void IsFinish(String alertmessage) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                finish();
            }
        };

        new AlertDialog.Builder(this)
                .setMessage(alertmessage)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 4 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            attachments.add(uri);
            attachmentAdapter.notifyDataSetChanged();
        }
    }

}

