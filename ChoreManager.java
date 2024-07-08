package com.example.test7;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChoreManager extends BaseActivity {
    protected int getLayoutResourceId() {
        return R.layout.activity_chore_manager;
    }

    private TextView currencyTextView;
    private TextView pointsTextView;
    private int virtualCurrency = 0;
    private int points = 0;
    private EditText searchEditText;
    private TextView titleTextView;
    private TextView totalMoneyTextView;
    private double totalMoneyEarned = 0.0;

    private Button addShortTermChoreButton;
    private Button addLongTermChoreButton;
    private RecyclerView shortTermChoreRecyclerView;
    private RecyclerView longTermChoreRecyclerView;

    private ChoreAdapter shortTermChoreAdapter;
    private ChoreAdapter longTermChoreAdapter;

    private List<Chore> shortTermChores;
    private List<Chore> longTermChores;

    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        setupRecyclerViews();
        setupListeners();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    }

    private void initViews() {
        searchEditText = findViewById(R.id.searchEditText);
        addShortTermChoreButton = findViewById(R.id.addShortTermChoreButton);
        addLongTermChoreButton = findViewById(R.id.addLongTermChoreButton);
        shortTermChoreRecyclerView = findViewById(R.id.shortTermChoreRecyclerView);
        longTermChoreRecyclerView = findViewById(R.id.longTermChoreRecyclerView);
        titleTextView = findViewById(R.id.titleTextView);
        totalMoneyTextView = findViewById(R.id.totalMoneyTextView);
    }

    private void setupRecyclerViews() {
        shortTermChores = new ArrayList<>();
        longTermChores = new ArrayList<>();

        shortTermChoreAdapter = new ChoreAdapter(this::showDeleteConfirmationDialog, this::toggleChoreCompletion);
        longTermChoreAdapter = new ChoreAdapter(this::showDeleteConfirmationDialog, this::toggleChoreCompletion);

        shortTermChoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        longTermChoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        shortTermChoreRecyclerView.setAdapter(shortTermChoreAdapter);
        longTermChoreRecyclerView.setAdapter(longTermChoreAdapter);
    }

    private void showDeleteConfirmationDialog(Chore chore) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this chore?")
                .setPositiveButton("Yes", (dialog, which) -> deleteChore(chore))
                .setNegativeButton("No", null)
                .show();
    }

    private void setupListeners() {
        addShortTermChoreButton.setOnClickListener(v -> showAddShortTermChoreDialog());
        addLongTermChoreButton.setOnClickListener(v -> showAddLongTermChoreDialog());

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterChores(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void showAddShortTermChoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Short Term Chore");

        final EditText input = new EditText(this);
        input.setHint("Chore name");
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String choreName = input.getText().toString();
            if (!choreName.isEmpty()) {
                showTimePickerDialog(choreName, false);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showAddLongTermChoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Long Term Chore");

        final EditText input = new EditText(this);
        input.setHint("Chore name");
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String choreName = input.getText().toString();
            if (!choreName.isEmpty()) {
                showDatePickerDialog(choreName, true);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showTimePickerDialog(String choreName, boolean isLongTerm) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    Calendar deadline = Calendar.getInstance();
                    deadline.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    deadline.set(Calendar.MINUTE, minute1);
                    askForIncomeAndAddChore(choreName, deadline.getTime(), isLongTerm);
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private void showDatePickerDialog(String choreName, boolean isLongTerm) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    Calendar deadline = Calendar.getInstance();
                    deadline.set(year1, month1, dayOfMonth);
                    askForIncomeAndAddChore(choreName, deadline.getTime(), isLongTerm);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void askForIncomeAndAddChore(String choreName, Date deadline, boolean isLongTerm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Income");

        final EditText input = new EditText(this);
        input.setHint("Enter income (if any)");
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String incomeText = input.getText().toString();
            double income = incomeText.isEmpty() ? 0 : Double.parseDouble(incomeText);
            if (isLongTerm) {
                addLongTermChore(choreName, deadline, income);
            } else {
                addShortTermChore(choreName, deadline, income);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addShortTermChore(String name, Date deadline, double income) {
        ShortTermChore chore = new ShortTermChore(name, deadline, income);
        shortTermChores.add(chore);
        shortTermChoreAdapter.setChores(shortTermChores);
    }

    private void addLongTermChore(String name, Date deadline, double income) {
        LongTermChore chore = new LongTermChore(name, deadline, income);
        longTermChores.add(chore);
        longTermChoreAdapter.setChores(longTermChores);
    }

    private void deleteChore(Chore chore) {
        if (chore instanceof ShortTermChore) {
            shortTermChores.remove(chore);
            shortTermChoreRecyclerView.post(() -> shortTermChoreAdapter.setChores(shortTermChores));
            updateTotalMoney();
        } else if (chore instanceof LongTermChore) {
            longTermChores.remove(chore);
            longTermChoreRecyclerView.post(() -> longTermChoreAdapter.setChores(longTermChores));
            updateTotalMoney();
        }
    }

    private void updateTotalMoney() {
        totalMoneyEarned = calculateTotalMoneyEarned();
        totalMoneyTextView.setText(String.format(Locale.getDefault(), "Total money earned: $%.2f", totalMoneyEarned));
    }

    private double calculateTotalMoneyEarned() {
        double total = 0.0;
        for (Chore chore : shortTermChores) {
            if (chore.isCompleted()) {
                total += chore.getIncome();
            }
        }
        for (Chore chore : longTermChores) {
            if (chore.isCompleted()) {
                total += chore.getIncome();
            }
        }
        return total;
    }

    private void toggleChoreCompletion(Chore chore) {
        boolean wasCompleted = chore.isCompleted();
        chore.setCompleted(!wasCompleted);

        if (!wasCompleted) {
            // Chore was just completed
            currencyManager.addCurrency(10);
            currencyManager.addPoints(10);
        } else {
            // Chore was uncompleted
            // Chore was uncompleted
            currencyManager.subCurrency(10);
            currencyManager.subPoints(10);
        }
        updateCurrencyDisplay();

        if (chore instanceof ShortTermChore) {
            shortTermChoreRecyclerView.post(() -> shortTermChoreAdapter.notifyDataSetChanged());
        } else if (chore instanceof LongTermChore) {
            longTermChoreRecyclerView.post(() -> longTermChoreAdapter.notifyDataSetChanged());
        }
        updateTotalMoney();
    }

    private void updateCurrencyAndPoints() {
        currencyTextView.setText(String.valueOf(virtualCurrency));
        pointsTextView.setText(points + " pts");
    }

    private void filterChores(String query)
    {
        List<Chore> filteredShortTermChores = new ArrayList<>();
        List<Chore> filteredLongTermChores = new ArrayList<>();

        for (Chore chore : shortTermChores)
        {
            if (chore.getName().toLowerCase().contains(query.toLowerCase()))
            {
                filteredShortTermChores.add(chore);
            }
        }

        for (Chore chore : longTermChores) {
            if (chore.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredLongTermChores.add(chore);
            }
        }

        shortTermChoreAdapter.setChores(filteredShortTermChores);
        longTermChoreAdapter.setChores(filteredLongTermChores);
    }
}
