package pl.red.todolist.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import pl.red.todolist.R;
import pl.red.todolist.model.Priority;

public class ShowTaskActivity extends Activity {
    TextView deadline;
    TextView completed;
    EditText title;
    EditText description;
    RadioGroup priority;

    List<Uri> attachments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_task);
        deadline = findViewById(R.id.deadline_show);
        deadline.setTextSize(25f);
        completed = findViewById(R.id.creation_show);
        completed.setTextSize(25f);
        title = findViewById(R.id.title_show);
        description = findViewById(R.id.description_show);
        priority = findViewById(R.id.priority_show);
        loadTaskIfExists();
    }

    private void loadTaskIfExists() {
        if (getIntent().getBooleanExtra("new", false)) {
            return;
        }
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        LocalDateTime deadline = (LocalDateTime) getIntent().getSerializableExtra("deadline");
        LocalDateTime completed = (LocalDateTime) getIntent().getSerializableExtra("completed");
        Priority priority = Priority.valueOf(getIntent().getStringExtra("priority"));
        attachments = (List<Uri>) getIntent().getSerializableExtra("attachments");
        this.title.setText(title);
        this.priority.check(priority.getId());
        this.description.setText(description);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.deadline.setText(" DEADLINE: " + deadline.format(dateTimeFormatter));
        String completedString = completed == null ? "" : completed.format(dateTimeFormatter);
        this.completed.setText(" COMPLETED: " + completedString);
    }

    public void viewAttachments(View view) {
        Intent intent = new Intent(getApplicationContext(), AttachmentActivity.class);
        intent.putExtra("attachmentList", (ArrayList<Uri>) attachments);
        intent.putExtra("show", true);
        startActivity(intent);
    }
}
