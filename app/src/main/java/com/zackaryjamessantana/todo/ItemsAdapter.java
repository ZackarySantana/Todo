package com.zackaryjamessantana.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Handles adding rows that accurately represent our model
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<String> _items;
    private OnLongClickListener _listener;

    public ItemsAdapter(List<String> items, OnLongClickListener listener) {
        this._items = items;
        this._listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("onCreate");
        System.out.println(this._items);
        // Using "LayoutInflator" with the context, to inflate the view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        // Wrapping the view in a view holder
        return new ViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Grab item at position and bind it to the view holder
        System.out.println("onBindView");
        System.out.println(this._items);
        holder.bind(this._items.get(position));
    }

    @Override
    public int getItemCount() {
        return this._items.size();
    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    /**
     * Provides easy access to the view of the recyclerview
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView _textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this._textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String item) {
            this._textView.setText(item);

            this._textView.setOnLongClickListener((view) -> {
                // Calling listener to remove item for us
                _listener.onItemLongClicked(getAdapterPosition());
                return true;
            });
        }
    }
}
