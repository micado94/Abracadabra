package com.oloh.oloh.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.oloh.oloh.R;
import com.oloh.oloh.view.activities.MainActivity;
import com.oloh.oloh.view.adapter.ProductListAdapter;
import com.oloh.oloh.util.Utils;

/**
 * Created by stran on 29/08/2017.
 */

public class ProductListFragment extends Fragment {
    private String subcategoryKey;
    private boolean isShoppingList;

    public ProductListFragment() {
        isShoppingList = true;
    }

    public ProductListFragment(String subcategoryKey) {

        isShoppingList = false;
        this.subcategoryKey = subcategoryKey;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_product_list_fragment, container,
                false);

        if (isShoppingList) {
            view.findViewById(R.id.slide_down).setVisibility(View.VISIBLE);
            view.findViewById(R.id.slide_down).setOnTouchListener(
                    new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {


                            Utils.switchFragmentWithAnimation(
                                    R.id.frag_container,
                                    new HomeFragment(),
                                    ((MainActivity) (getContext())), Utils.HOME_FRAGMENT,
                                    Utils.AnimationType.SLIDE_DOWN);


                            return false;
                        }
                    });
        }

        // Fill Recycler View
        RecyclerView recyclerView = (RecyclerView) view
                .findViewById(R.id.product_list_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ProductListAdapter adapter = new ProductListAdapter(subcategoryKey,
                getActivity(), isShoppingList);

        recyclerView.setAdapter(adapter);

        adapter.SetOnItemClickListener(new ProductListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Utils.switchFragmentWithAnimation(R.id.frag_container,
                        new ProductDetailsFragment(subcategoryKey, position, false),
                        ((MainActivity) (getContext())), null,
                        Utils.AnimationType.SLIDE_LEFT);

            }
        });

        // Handle Back press
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {


                    Utils.switchFragmentWithAnimation(
                            R.id.frag_container,
                            new HomeFragment(),
                            ((MainActivity) (getContext())), Utils.HOME_FRAGMENT,
                            Utils.AnimationType.SLIDE_UP);

                }
                return true;
            }
        });

        return view;
    }

}