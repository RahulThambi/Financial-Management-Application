package com.example.test7;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExpenseSaverTracker extends BaseActivity {
    protected int getLayoutResourceId() {
        return R.layout.activity_expense_saver_tracker;
    }


    private TextView totalExpenseTextView;
    private TextView totalSavingsTextView;
    private ListView transactionListView;
    private SimplePieChartView pieChartView;
    private List<Transaction> transactions;
    private List<Transaction> filteredTransactions;
    private TransactionAdapter adapter;
    private double savingsGoal;
    private Date savingsGoalDate;
    private Spinner filterSpinner;
    private EditText searchEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        totalExpenseTextView = findViewById(R.id.totalExpenseTextView);
        totalSavingsTextView = findViewById(R.id.totalSavingsTextView);
        transactionListView = findViewById(R.id.transactionListView);
        pieChartView = findViewById(R.id.pieChartView);
        filterSpinner = findViewById(R.id.filterSpinner);
        searchEditText = findViewById(R.id.searchEditText);

        transactions = new ArrayList<>();
        filteredTransactions = new ArrayList<>();
        adapter = new TransactionAdapter(this, filteredTransactions);
        transactionListView.setAdapter(adapter);

        findViewById(R.id.addExpenseButton).setOnClickListener(v -> showAddTransactionDialog(true));
        findViewById(R.id.addSavingsButton).setOnClickListener(v -> showAddTransactionDialog(false));
        findViewById(R.id.setNewGoalButton).setOnClickListener(v -> showSetNewGoalDialog());
        findViewById(R.id.checkGoalButton).setOnClickListener(v -> showCheckGoalDialog());

        setupFilterSpinner();
        setupSearchEditText();

        transactionListView.setOnItemClickListener((parent, view, position, id) -> showTransactionDetails(filteredTransactions.get(position)));
        transactionListView.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteTransactionDialog(filteredTransactions.get(position));
            return true;
        });

        updateTotals();
        updatePieChart();
    }



    private void setupFilterSpinner()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterTransactions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupSearchEditText()
    {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterTransactions();
            }
        });
    }

    private void filterTransactions()
    {
        String filter = filterSpinner.getSelectedItem().toString();
        String search = searchEditText.getText().toString().toLowerCase();

        filteredTransactions.clear();
        for (Transaction transaction : transactions) {
            boolean matchesFilter = filter.equals("All") ||
                    (filter.equals("Expenses") && transaction.isExpense()) ||
                    (filter.equals("Savings") && !transaction.isExpense()) ||
                    filter.equals(transaction.getCategory());

            boolean matchesSearch = transaction.getName().toLowerCase().contains(search) ||
                    transaction.getCategory().toLowerCase().contains(search);

            if (matchesFilter && matchesSearch) {
                filteredTransactions.add(transaction);
            }
        }

        adapter.notifyDataSetChanged();
        updateTotals();
        updatePieChart();
    }

    private void showAddTransactionDialog(boolean isExpense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_transaction, null);
        builder.setView(dialogView);

        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        EditText amountEditText = dialogView.findViewById(R.id.amountEditText);
        Spinner categorySpinner = dialogView.findViewById(R.id.categorySpinner);

        if (isExpense) {
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.categories_array, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(spinnerAdapter);
        } else {
            categorySpinner.setVisibility(View.GONE);
        }

        builder.setTitle(isExpense ? "Add Expense" : "Add Savings")
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameEditText.getText().toString();
                    double amount = Double.parseDouble(amountEditText.getText().toString());
                    String category = isExpense ? categorySpinner.getSelectedItem().toString() : "Savings";

                    Transaction transaction = new Transaction(name, category, amount, isExpense);
                    transactions.add(transaction);
                    filterTransactions();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.create().show();
    }

    private void showDeleteTransactionDialog(Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Transaction")
                .setMessage("Are you sure you want to delete this transaction?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    transactions.remove(transaction);
                    filterTransactions();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    private void showTransactionDetails(Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_transaction_details, null);
        builder.setView(dialogView);

        TextView nameTextView = dialogView.findViewById(R.id.nameTextView);
        TextView amountTextView = dialogView.findViewById(R.id.amountTextView);
        TextView categoryTextView = dialogView.findViewById(R.id.categoryTextView);
        TextView dateTextView = dialogView.findViewById(R.id.dateTextView);

        if (transaction != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault());
            Date transactionDate = transaction.getDate();
            if (transactionDate != null) {
                dateTextView.setText("Date: " + dateFormat.format(transactionDate));
            } else {
                dateTextView.setText("Date: N/A");
            }
            nameTextView.setText("Name: " + transaction.getName());
            amountTextView.setText(String.format("Amount: $%.2f", transaction.getAmount()));
            categoryTextView.setText("Category: " + transaction.getCategory());

            builder.setTitle(transaction.isExpense() ? "Expense Details" : "Savings Details")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        } else {
            // Handle the case where transaction is null
            dateTextView.setText("No transaction details available.");
        }
    }


    private void showSetNewGoalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_set_new_goal, null);
        builder.setView(dialogView);

        EditText goalAmountEditText = dialogView.findViewById(R.id.goalAmountEditText);
        TextView goalDateTextView = dialogView.findViewById(R.id.goalDateTextView);

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            goalDateTextView.setText(dateFormat.format(calendar.getTime()));
        };

        goalDateTextView.setOnClickListener(v -> new DatePickerDialog(ExpenseSaverTracker.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        builder.setTitle("Set New Savings Goal")
                .setPositiveButton("Set", (dialog, which) -> {
                    savingsGoal = Double.parseDouble(goalAmountEditText.getText().toString());
                    savingsGoalDate = calendar.getTime();
                    Toast.makeText(this, "New savings goal set!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.create().show();
    }

    private void showCheckGoalDialog() {
        if (savingsGoal <= 0 || savingsGoalDate == null) {
            Toast.makeText(this, "No savings goal set!", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_check_goal, null);
        builder.setView(dialogView);

        ProgressBar goalProgressBar = dialogView.findViewById(R.id.goalProgressBar);
        TextView goalProgressTextView = dialogView.findViewById(R.id.goalProgressTextView);
        TextView goalAmountTextView = dialogView.findViewById(R.id.goalAmountTextView);
        TextView goalDateTextView = dialogView.findViewById(R.id.goalDateTextView);
        TextView daysLeftTextView = dialogView.findViewById(R.id.daysLeftTextView);
        TextView savedAmountTextView = dialogView.findViewById(R.id.savedAmountTextView);

        double totalSavings = calculateTotalSavings();
        int progress = (int) ((totalSavings / savingsGoal) * 100);

        goalProgressBar.setProgress(progress);
        goalProgressTextView.setText(progress + "%");
        goalAmountTextView.setText(String.format("Goal: $%.2f", savingsGoal));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        goalDateTextView.setText("End Date: " + dateFormat.format(savingsGoalDate));

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), savingsGoalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        daysLeftTextView.setText(String.format("Days left: %d", daysLeft));

        savedAmountTextView.setText(String.format("Saved: $%.2f / $%.2f", totalSavings, savingsGoal));

        builder.setTitle("Savings Goal Progress")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private double calculateTotalSavings() {
        double totalSavings = 0;
        for (Transaction transaction : transactions) {
            if (!transaction.isExpense()) {
                totalSavings += transaction.getAmount();
            }
        }
        return totalSavings;
    }

    private void updateTotals() {
        double totalExpense = 0;
        double totalSavings = 0;

        for (Transaction transaction : filteredTransactions) {
            if (transaction.isExpense()) {
                totalExpense += transaction.getAmount();
            } else {
                totalSavings += transaction.getAmount();
            }
        }

        totalExpenseTextView.setText(String.format("Total Expense: $%.2f", totalExpense));
        totalSavingsTextView.setText(String.format("Total Savings: $%.2f", totalSavings));
    }

    private void updatePieChart() {
        Map<String, Float> categoryTotals = new HashMap<>();

        for (Transaction transaction : filteredTransactions) {
            if (transaction.isExpense()) {
                String category = transaction.getCategory();
                float amount = (float) transaction.getAmount();
                categoryTotals.put(category, categoryTotals.getOrDefault(category, 0f) + amount);
            }
        }

        if (categoryTotals.isEmpty()) {
            categoryTotals.put("No Expenses", 100f);
        }

        pieChartView.setData(categoryTotals);
    }
}