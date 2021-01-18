package com.example.myprogect.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprogect.Interface.ItemClickListner;
import com.example.myprogect.R;

public class CartViewHoleder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textproductName ,
                    textproducprice ,
                    textproducQuantity  ;

    private ItemClickListner itemClickListner ;

    public CartViewHoleder(@NonNull View itemView) {
        super(itemView);


        textproductName = itemView.findViewById(R.id.cart_product_name1);
        textproducprice = itemView.findViewById(R.id.cart_product_price1);
        textproducQuantity = itemView.findViewById(R.id.cart_product_quantity1);


    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v , getAdapterPosition() , false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;

    }
}
