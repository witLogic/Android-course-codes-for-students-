package com.customer.e_cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by muthuveerappans on 8/26/17.
 */

public class ItemsListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.items_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_items);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        itemsAdapter = new ItemsAdapter();
        recyclerView.setAdapter(itemsAdapter);

        findViewById(R.id.cart_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemsListActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }

    private class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ItemsListActivity.this).inflate(R.layout.item_row, parent, false);
            return new ItemsRowHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemsRowHolder vh = (ItemsRowHolder) holder;
            vh.populateViews("Item " + position, (position * 25) + 25);
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    private class ItemsRowHolder extends RecyclerView.ViewHolder {
        TextView item_name;
        TextView item_amount;

        public ItemsRowHolder(View itemView) {
            super(itemView);

            item_amount = itemView.findViewById(R.id.item_amount);
            item_name = itemView.findViewById(R.id.item_name);
        }

        public void populateViews(String itemName, int amount) {
            item_name.setText(itemName);
            item_amount.setText(ItemsListActivity.this.getString(R.string.Rs) + "" + amount);
        }
    }
}
