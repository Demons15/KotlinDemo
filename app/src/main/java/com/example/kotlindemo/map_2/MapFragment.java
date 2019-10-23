// (c)2016 Flipboard Inc, All Rights Reserved.

package com.example.kotlindemo.map_2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kotlindemo.BaseFragment;
import com.example.kotlindemo.GankBeautyResultToItemsMapper;
import com.example.kotlindemo.R;
import com.example.kotlindemo.adapter.ItemListAdapter;
import com.example.kotlindemo.model.Item;
import com.example.kotlindemo.network.Network;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MapFragment extends BaseFragment {
    private int page = 0;

    @BindView(R.id.pageTv)
    TextView pageTv;
    @BindView(R.id.previousPageBt)
    Button previousPageBt;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.gridRv)
    RecyclerView gridRv;

    ItemListAdapter adapter = new ItemListAdapter();

    @OnClick(R.id.previousPageBt)
    void previousPage() {
        loadPage(--page);
        if (page == 1) {
            previousPageBt.setEnabled(false);
        }
    }

    @OnClick(R.id.nextPageBt)
    void nextPage() {
        loadPage(++page);
        if (page == 2) {
            previousPageBt.setEnabled(true);
        }
    }

    private void loadPage(int page) {
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        disposable = Network.getGankApi()
                .getBeauties(10, page)
                .map(GankBeautyResultToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Item>>() {
                    @Override
                    public void accept(@NonNull List<Item> items) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);
                        pageTv.setText(getString(R.string.page_with_number, MapFragment.this.page));
                        adapter.setItems(items);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_map;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_map;
    }
}
