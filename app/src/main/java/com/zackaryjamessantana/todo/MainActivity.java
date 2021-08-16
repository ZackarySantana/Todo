package com.zackaryjamessantana.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button _addItem;
    private EditText _itemToAdd;
    private RecyclerView _itemsList;

    private ItemsAdapter _itemsAdapater;

    private List<String> _items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variables
        this._items = new ArrayList<>();
        Runnable saveItems = loadItems();

        // Binding to view
        this._addItem = findViewById(R.id.addItem);
        this._itemToAdd = findViewById(R.id.itemToAdd);
        this._itemsList = findViewById(R.id.itemsList);

        // Adapters
        this._itemsAdapater = new ItemsAdapter(this._items, (position) -> {
            this._items.remove(position);
            this._itemsAdapater.notifyItemRemoved(position);
            saveItems.run();
            Toast.makeText(getApplicationContext(), "An Item Was Removed", Toast.LENGTH_SHORT).show();
        });
        this._itemsList.setAdapter(this._itemsAdapater);
        this._itemsList.setLayoutManager(new LinearLayoutManager(this));

        // Button functionality
        this._addItem.setOnClickListener((view) -> {
            String itemToAddText = this._itemToAdd.getText().toString();

            // Add item to model
            this._items.add(itemToAddText);

            // Notify the adapter
            //this._itemsAdapater.notifyDataSetChanged(); // This is a last resort that should be narrowed down
            this._itemsAdapater.notifyItemInserted(this._items.size() - 1); // This is the narrowed down notifier
            saveItems.run();
            this._itemToAdd.getText().clear();

            Toast.makeText(getApplicationContext(), "An Item Was Added", Toast.LENGTH_SHORT).show();
        });
    }

    private Runnable loadItems() {
        // Grabs the file and loads the items from it
        File file = new File(getFilesDir(), "data.txt");

        try {
            // Clears and adds appropriate items
            this._items.clear();
            this._items.addAll(FileUtils.readLines(file, Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
        }

        // Return a callback function for saving items
        return () -> {
            try {
                FileUtils.writeLines(file, this._items);
            } catch (IOException e) {
                Log.e("MainActivity", "Error writing items", e);
            }
        };
    }
}