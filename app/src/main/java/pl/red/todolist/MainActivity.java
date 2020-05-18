package pl.red.todolist;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import pl.red.todolist.buisness.NotificationService;
import pl.red.todolist.buisness.TaskAdapter;
import pl.red.todolist.model.TaskList;

import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static pl.red.todolist.utils.Order.ASC;
import static pl.red.todolist.utils.Order.DESC;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    TaskList tasks;
    TaskAdapter adapter;
    NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.list);
        tasks = new TaskList(20);
        adapter = new TaskAdapter(tasks);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notificationService = new NotificationService(
                new NotificationCompat.Builder(getApplicationContext(), "notify_001"),
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE));
        sendNotification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void addTask(View view) {
    }

    public void sendNotification() {
        String afterDeadline = tasks.getAfterDeadline();
        long count = afterDeadline.chars().filter(i -> i == '\n').count() + 1;
        notificationService.notify(afterDeadline, count);
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

        adapter.notifySorted();
    }

    public void writeToFile(MenuItem item) throws IOException {

        String dir = getExternalFilesDir(DIRECTORY_DOCUMENTS) + "/ToDoList/";
        createRootAppFolderIfNotExists(Paths.get(dir));
        String file = dir + "/tasklist" + LocalDateTime.now() + ".txt ";

        Files.write(Paths.get(file), tasks.lines());

    }

    private void createRootAppFolderIfNotExists(Path dir) throws IOException {
        if (Files.notExists(dir)) {
            Files.createDirectory(dir);
        }
    }
}
