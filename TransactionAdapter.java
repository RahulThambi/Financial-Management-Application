package com.example.test7;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat; // for color resources

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction>
{

    public TransactionAdapter(@NonNull Context context, @NonNull List<Transaction> transactions)
    {
        super(context, 0, transactions); // Resource ID (0) for custom layout
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_transaction, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView amountTextView = convertView.findViewById(R.id.amountTextView);
        TextView categoryTextView = convertView.findViewById(R.id.categoryTextView);

        nameTextView.setText(transaction.getName());
        amountTextView.setText(String.format("$%.2f", transaction.getAmount()));
        categoryTextView.setText(transaction.getCategory());

        int textColor = transaction.isExpense() ? ContextCompat.getColor(getContext(), R.color.colorExpense) : ContextCompat.getColor(getContext(), R.color.colorSavings);
        amountTextView.setTextColor(textColor);

        return convertView;
    }
}
