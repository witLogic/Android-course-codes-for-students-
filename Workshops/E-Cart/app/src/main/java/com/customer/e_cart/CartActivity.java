package com.customer.e_cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by muthuveerappans on 8/26/17.
 */

public class CartActivity extends AppCompatActivity {
    TextView total_amount;
    RecyclerView recyclerView;
    CartItemsAdapter cartItemsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cart);

        total_amount = (TextView) findViewById(R.id.total_amount);

        total_amount.setText(getString(R.string.Rs) + "150");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        cartItemsAdapter = new CartItemsAdapter();
        recyclerView.setAdapter(cartItemsAdapter);
    }

    private class CartItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CartActivity.this).inflate(R.layout.cart_row, parent, false);
            return new CartItemsRowHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            CartItemsRowHolder vh = (CartItemsRowHolder) holder;
            vh.populateViews("Item " + position, (position * 25) + 25);
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    private class CartItemsRowHolder extends RecyclerView.ViewHolder {
        TextView item_name;
        TextView item_amount;

        public CartItemsRowHolder(View itemView) {
            super(itemView);

            item_amount = itemView.findViewById(R.id.item_amount);
            item_name = itemView.findViewById(R.id.item_name);
        }

        public void populateViews(String itemName, int amount) {
            item_name.setText(itemName);
            item_amount.setText(CartActivity.this.getString(R.string.Rs) + "" + amount);
        }
    }
}
