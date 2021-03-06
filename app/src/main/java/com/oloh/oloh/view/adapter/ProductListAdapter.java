package com.oloh.oloh.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oloh.oloh.R;
import com.oloh.oloh.model.entities.Products;
import com.oloh.oloh.view.activities.MainActivity;
import com.oloh.oloh.view.customview.TextDrawable;
import com.oloh.oloh.model.CenterRepository;
import com.oloh.oloh.model.entities.Money;
import com.oloh.oloh.util.ColorGenerator;
import com.oloh.oloh.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by stran on 29/08/2017.
 */

public class ProductListAdapter extends
        RecyclerView.Adapter<ProductListAdapter.VersionViewHolder> implements
        ItemTouchHelperAdapter {

    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    private TextDrawable.IBuilder mDrawableBuilder;

    private TextDrawable drawable;

    private String ImageUrl;

    private List<Products> productsList = new ArrayList<Products>();
    private OnItemClickListener clickListener;

    private Context context;

    public ProductListAdapter(String subcategoryKey, Context context,
                              boolean isCartlist) {

        if (isCartlist) {

            productsList = CenterRepository.getCenterRepository()
                    .getListOfProductsInShoppingList();

        } else {

            productsList = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                    .get(subcategoryKey);
        }

        this.context = context;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_product_list, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder holder,
                                 final int position) {

        holder.itemName.setText(productsList.get(position)
                .getItemName());

        holder.itemDesc.setText(productsList.get(position)
                .getItemShortDesc());

       String sellCostString = Money.euro(
                BigDecimal.valueOf(Double.valueOf(productsList.get(position)
                        .getSellMRP()))).toString()
                + "  ";

        /*String buyMRP = Money.rupees(
                BigDecimal.valueOf(Long.valueOf(productsList.get(position)
                        .getMRP()))).toString();*/

        String costString = sellCostString /*+ buyMRP*/;

        holder.itemCost.setText(costString, TextView.BufferType.SPANNABLE);

        Spannable spannable = (Spannable) holder.itemCost.getText();

        spannable.setSpan(new StrikethroughSpan(), sellCostString.length(),
                costString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                .endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(String.valueOf(productsList
                .get(position).getItemName().charAt(0)), mColorGenerator
                .getColor(productsList.get(position).getItemName()));

        ImageUrl = productsList.get(position).getImageUrl();

        Glide.with(context).load(ImageUrl).placeholder(drawable)
                .error(drawable).animate(R.anim.base_slide_right_in)
                .centerCrop().into(holder.imagView);

        holder.addItem.findViewById(R.id.add_item).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        //current object
                        Products tempObj = productsList.get(position);


                        //if current object is lready in shopping list
                        if (CenterRepository.getCenterRepository()
                                .getListOfProductsInShoppingList().contains(tempObj)) {


                            //get position of current item in shopping list
                            int indexOfTempInShopingList = CenterRepository
                                    .getCenterRepository().getListOfProductsInShoppingList()
                                    .indexOf(tempObj);

                            // increase quantity of current item in shopping list
                            if (Integer.parseInt(tempObj.getQuantity()) == 0) {

                                ((MainActivity) getContext())
                                        .updateItemCount(true);

                            }


                            // update quanity in shopping list
                            CenterRepository
                                    .getCenterRepository()
                                    .getListOfProductsInShoppingList()
                                    .get(indexOfTempInShopingList)
                                    .setQuantity(
                                            String.valueOf(Integer
                                                    .valueOf(tempObj
                                                            .getQuantity()) + 1));


                            //update checkout amount
                            ((MainActivity) getContext()).updateCheckOutAmount(
                                    BigDecimal
                                            .valueOf(Double
                                                    .valueOf(productsList
                                                            .get(position)
                                                            .getSellMRP())),
                                    true);

                            // update current item quanitity
                            holder.quanitity.setText(tempObj.getQuantity());

                        } else {

                            ((MainActivity) getContext()).updateItemCount(true);

                            tempObj.setQuantity(String.valueOf(1));

                            holder.quanitity.setText(tempObj.getQuantity());

                            CenterRepository.getCenterRepository()
                                    .getListOfProductsInShoppingList().add(tempObj);

                            ((MainActivity) getContext()).updateCheckOutAmount(
                                    BigDecimal
                                            .valueOf(Double
                                                    .valueOf(productsList
                                                            .get(position)
                                                            .getSellMRP())),
                                    true);


                        }

                        Utils.vibrate(getContext());

                    }
                });

        holder.removeItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Products tempObj = (productsList).get(position);

                if (CenterRepository.getCenterRepository().getListOfProductsInShoppingList()
                        .contains(tempObj)) {

                    int indexOfTempInShopingList = CenterRepository
                            .getCenterRepository().getListOfProductsInShoppingList()
                            .indexOf(tempObj);

                    if (Integer.valueOf(tempObj.getQuantity()) != 0) {

                        CenterRepository
                                .getCenterRepository()
                                .getListOfProductsInShoppingList()
                                .get(indexOfTempInShopingList)
                                .setQuantity(
                                        String.valueOf(Integer.valueOf(tempObj
                                                .getQuantity()) - 1));

                        ((MainActivity) getContext()).updateCheckOutAmount(
                                BigDecimal.valueOf(Double.valueOf(productsList
                                        .get(position).getSellMRP())),
                                false);

                        holder.quanitity.setText(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(indexOfTempInShopingList).getQuantity());

                        Utils.vibrate(getContext());

                        if (Integer.valueOf(CenterRepository
                                .getCenterRepository().getListOfProductsInShoppingList()
                                .get(indexOfTempInShopingList).getQuantity()) == 0) {

                            CenterRepository.getCenterRepository()
                                    .getListOfProductsInShoppingList()
                                    .remove(indexOfTempInShopingList);

                            notifyDataSetChanged();

                            ((MainActivity) getContext())
                                    .updateItemCount(false);

                        }

                    }

                } else {

                }

            }

        });

    }


    private MainActivity getContext() {
        return (MainActivity) context;
    }

    @Override
    public int getItemCount() {
        return productsList == null ? 0 : productsList.size();
    }

    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onItemDismiss(int position) {
        productsList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(productsList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(productsList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    class VersionViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        TextView itemName, itemDesc, itemCost, availability, quanitity,
                addItem, removeItem;
        ImageView imagView;

        public VersionViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.item_name);

            itemDesc = (TextView) itemView.findViewById(R.id.item_short_desc);

            itemCost = (TextView) itemView.findViewById(R.id.item_price);

            availability = (TextView) itemView
                    .findViewById(R.id.iteam_avilable);

            quanitity = (TextView) itemView.findViewById(R.id.iteam_amount);

            itemName.setSelected(true);

            imagView = ((ImageView) itemView.findViewById(R.id.product_thumb));

            addItem = ((TextView) itemView.findViewById(R.id.add_item));

            removeItem = ((TextView) itemView.findViewById(R.id.remove_item));

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }
    }

}
