package pl.red.todolist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import pl.red.todolist.model.Priority;
import pl.red.todolist.model.Task;

public class AddEditTaskActivity extends Activity {
    TimePicker tpicker;
    DatePicker dpicker;
    EditText title;
    EditText description;
    RadioGroup priority;
    ImageView v;
    private final List<Uri> paths = new ArrayList<>();

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
        v = findViewById(R.id.imageView);
    }

    private void loadTaskIfExists() {
        if (getIntent().getBooleanExtra("new", false)) {
            return;
        }
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        LocalDateTime deadline = (LocalDateTime) getIntent().getSerializableExtra("deadline");
        Priority priority = Priority.valueOf(getIntent().getStringExtra("priority"));
        this.title.setText(title);
        this.description.setText(description);
        dpicker.init(deadline.getYear(), deadline.getMonthValue(), deadline.getDayOfMonth(), null);
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
//                .addAllAttachments(paths.stream().map(Attachment::new).collect(Collectors.toList()))
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
        LocalDate date = LocalDate.of(dpicker.getYear(), dpicker.getMonth(), dpicker.getDayOfMonth());
        LocalTime time = LocalTime.of(tpicker.getHour(), tpicker.getMinute());
        return LocalDateTime.of(date, time);
    }

    public void addAttachment(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpg");
        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3) {
            Uri path = data.getData();
            paths.add(path);
        }
    }
}
