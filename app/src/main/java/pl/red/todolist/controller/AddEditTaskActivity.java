package pl.red.todolist.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pl.red.todolist.R;
import pl.red.todolist.model.Attachment;
import pl.red.todolist.model.Priority;
import pl.red.todolist.model.Task;

public class AddEditTaskActivity extends Activity {
    TimePicker tpicker;
    DatePicker dpicker;
    EditText title;
    EditText description;
    RadioGroup priority;

    List<Uri> attachments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_edit_task);
        tpicker = findViewById(R.id.time_picker);
        tpicker.setIs24HourView(true);
        dpicker = findViewById(R.id.date_picker);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        priority = findViewById(R.id.priority);
        loadTaskIfExists();
    }

    private void loadTaskIfExists() {
        if (getIntent().getBooleanExtra("new", false)) {
            return;
        }
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        LocalDateTime deadline = (LocalDateTime) getIntent().getSerializableExtra("deadline");
        Priority priority = Priority.valueOf(getIntent().getStringExtra("priority"));
        attachments = (List<Uri>) getIntent().getSerializableExtra("attachments");
        this.title.setText(title);
        this.description.setText(description);
        dpicker.init(deadline.getYear(), deadline.getMonth().getValue()-1, deadline.getDayOfMonth(), null);
        tpicker.setMinute(deadline.getMinute());
        tpicker.setHour(deadline.getHour());
        this.priority.check(priority.getId());
    }

    public void save(View view) {
        if (emptyContents()) {
            return;
        }
        RadioButton checked = findViewById(priority.getCheckedRadioButtonId());

        Task newTask = Task.builder().title(title.getText().toString())
                .description(description.getText().toString())
                .priority(Priority.valueOf(String.valueOf(checked.getText())))
                .deadlineDate(parseDate())
                .replaceAttachments(attachments.stream().map(Uri::toString).map(Attachment::new).collect(Collectors.toList()))
                .build();
        sendTask(newTask);

    }

    void sendTask(Task newTask) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("newTask", newTask);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private boolean emptyContents() {
        title.setBackgroundColor(Color.TRANSPARENT);
        description.setBackgroundColor(Color.TRANSPARENT);
        boolean result = false;
        if (title.getText().length() <= 0) {
            title.setBackgroundColor(Color.RED);
            result = true;
        }
        if (description.getText().length() <= 0) {
            description.setBackgroundColor(Color.RED);
            result = true;
        }
        return result;
    }

    private LocalDateTime parseDate() {
        LocalDate date = LocalDate.of(dpicker.getYear(), dpicker.getMonth() + 1, dpicker.getDayOfMonth());
        LocalTime time = LocalTime.of(tpicker.getHour(), tpicker.getMinute());
        return LocalDateTime.of(date, time);
    }

    public void viewAttachments(View view) {
        Intent intent = new Intent(getApplicationContext(), AttachmentActivity.class);
        intent.putExtra("attachmentList", (ArrayList<Uri>) attachments);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3 && resultCode == RESULT_OK) {
            attachments = (List<Uri>) data.getSerializableExtra("attachments");
        }
    }
}
