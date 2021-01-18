package com.example.myprogect.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myprogect.Interface.ItemClickListner;
import com.example.myprogect.R;
import com.facebook.shimmer.ShimmerFrameLayout;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice , txtNameSalary;
    public ImageView imageView;
    public ItemClickListner listner;
   public ShimmerFrameLayout shimmerFrameLayout ;






    public ProductViewHolder(View itemView)
    {
        super(itemView);

        txtNameSalary =   itemView.findViewById(R.id.seller_name_item_sall222);

        imageView =   itemView.findViewById(R.id.product_image);
        txtProductName =   itemView.findViewById(R.id.product_name);
        txtProductDescription =  itemView.findViewById(R.id.product_description);
        txtProductPrice = itemView.findViewById(R.id.product_price);
//
//       shimmerFrameLayout = itemView.findViewById(R.id.shimmerFrameLayout);
//       shimmerFrameLayout.startShimmer();


        
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}