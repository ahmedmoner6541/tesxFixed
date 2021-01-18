package com.example.myprogect.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprogect.Interface.ItemClickListner;
import com.example.myprogect.R;

public class ProductSellerViewHolder extends  RecyclerView.ViewHolder  {


     public TextView
            textOrderNumber,
            textAmountProductsOrder,
            textOrderdata,
            textAllOrderPrice;

      public Button showProductsOrders;



    public ProductSellerViewHolder(@NonNull View itemView) {
        super(itemView);

        textOrderNumber = itemView.findViewById(R.id.num_order);
        textAmountProductsOrder = itemView.findViewById(R.id.amount_order);

        showProductsOrders = itemView.findViewById(R.id.show_products_order);


        textOrderdata = itemView.findViewById(R.id.date_order);
        textAllOrderPrice = itemView.findViewById(R.id.slary_order);
    }

}