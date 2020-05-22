package pl.red.todolist;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;

import pl.red.todolist.buisness.NotificationService;
import pl.red.todolist.buisness.TaskAdapter;
import pl.red.todolist.model.Task;
import pl.red.todolist.model.TaskListFacade;

import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static pl.red.todolist.utils.Order.ASC;
import static pl.red.todolist.utils.Order.DESC;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    TaskListFacade tasks;
    TaskAdapter adapter;
    NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.list);
        tasks = new TaskListFacade();
        adapter = new TaskAdapter(tasks, getIntent());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notificationService = new NotificationService(
                new NotificationCompat.Builder(getApplicationContext(), "notify_001"),
                (NotificationManager) Objects.requireNonNull(this.getSystemService(Context.NOTIFICATION_SERVICE)));
        sendNotification();
        tasks.deserialize(loadData());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private byte[] loadData() {
        final String dir = getExternalFilesDir(DIRECTORY_DOCUMENTS) + "/ToDoList/";

        byte[] bytes = new byte[0];
        try {
            Path path = Paths.get(dir + "/tasklist.todo");
            if (Files.exists(path)) {
                bytes = Files.readAllBytes(Paths.get(dir + "/tasklist.todo"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public void addTask(View view) {
        Intent intent = new Intent(getApplicationContext(), AddEditTaskActivity.class);
        intent.putExtra("new", true);
        startActivityForResult(intent, 1);
    }


    public void sendNotification() {
        String afterDeadline = tasks.getAfterDeadline();
        if (!afterDeadline.isEmpty()) {
            long count = afterDeadline.chars().filter(i -> i == '\n').count() + 1;
            notificationService.notify(afterDeadline, count);
        }
    }

    public void writeToFiles(MenuItem item) throws IOException {
        final String dir = getExternalFilesDir(DIRECTORY_DOCUMENTS) + "/ToDoList/";

        createRootAppFolderIfNotExists(Paths.get(dir));
        String file = dir + "/tasklist" + LocalDateTime.now() + ".txt";
        Files.write(Paths.get(file), tasks.lines());
        Files.write(Paths.get(dir + "/tasklist.todo"), tasks.getSerialized());
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    private void createRootAppFolderIfNotExists(Path dir) throws IOException {
        if (Files.notExists(dir)) {
            Files.createDirectory(dir);
        }
    }

    public void sort(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.az:
                tasks.sortTitle(ASC);
                break;
            case R.id.za:
                tasks.sortTitle(DESC);
                break;
            case R.id.deadline_asc:
                tasks.sortDeadline(ASC);
                break;
            case R.id.deadline_desc:
                tasks.sortDeadline(DESC);
                break;
            case R.id.status_asc:
                tasks.sortStatus(ASC);
                break;
            case R.id.status_desc:
                tasks.sortStatus(DESC);
                break;
            case R.id.priority_asc:
                tasks.sortPriority(ASC);
                break;
            case R.id.priority_desc:
                tasks.sortPriority(DESC);
                break;
        }
        adapter.notifyChanged();
    }

    public void edit(View view) {
        callParent(view);
        int position = getPosition();

        Intent intent = new Intent(getApplicationContext(), AddEditTaskActivity.class);
        intent.putExtra("title", tasks.getTitleTask(position));
        intent.putExtra("description", tasks.getDescriptionTask(position));
        intent.putExtra("deadline", tasks.getDeadlineDateTimeTask(position));
        intent.putExtra("priority", tasks.getPriorityNameTask(position));
        startActivityForResult(intent, 2);
    }

    public void delete(View view) {
        callParent(view);
        int position = getPosition();
        if (position > -1) {
            tasks.removeTask(position);
            adapter.notifyDeleted(position);
            Toast.makeText(this, "Data deleted", Toast.LENGTH_SHORT).show();
        }
    }

    private int getPosition() {
        return getIntent().getIntExtra("position", -1);
    }

    private void callParent(View view) {
        ((View) view.getParent()).performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Task result = (Task) Objects.requireNonNull(data).getSerializableExtra("newTask");
                tasks.addTask(result);
                adapter.notifyChanged();
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Task result = (Task) Objects.requireNonNull(data).getSerializableExtra("newTask");
                tasks.replaceTask(getPosition(), result);
                adapter.notifyChanged();
            }
        }
    }
}