package id.unware.poken.ui.browse.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.Category;
import id.unware.poken.domain.Product;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.browse.model.BrowseModel;
import id.unware.poken.ui.browse.presenter.BrowsePresenter;
import id.unware.poken.ui.browse.view.adapter.BrowseProductAdapter;

public class BrowseActivity extends AppCompatActivity implements IBrowseView {

    private static final String TAG = "BrowseActivity";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvProductBrowsing) RecyclerView rvProductBrowsing;

    private Unbinder unbinder;

    private List<Product> listItem = new ArrayList<>();
    private BrowseProductAdapter adapter;

    private BrowsePresenter presenter;

    private int actionId = -1;
    private String actionName = "";

    // CASE BROWSE BY CATEGORY
    private boolean isBrowseByCategory = false;
    private int categoryId = -1;
    private String categoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        unbinder = ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();

            actionId = extras.getInt(Constants.EXTRA_GENERAL_INTENT_ID, -1);
            actionName = extras.getString(Constants.EXTRA_GENERAL_INTENT_VALUE, "");

            // Browse by category
            if (extras.containsKey(Constants.EXTRA_IS_BROWSE_BY_CATEGORY)) {
                isBrowseByCategory = extras.getBoolean(Constants.EXTRA_IS_BROWSE_BY_CATEGORY, false);
                categoryId = (int) extras.getLong(Constants.EXTRA_CATEGORY_ID, -1);
                categoryName = extras.getString(Constants.EXTRA_CATEGORY_NAME, "");
            }
        }

        presenter = new BrowsePresenter(new BrowseModel(), this);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void initView() {

        setupToolbarView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestContent();
            }
        });

        initBrowseListView();

        requestContent();
    }

    private void requestContent() {
        if (!isBrowseByCategory) {
            presenter.getProductDataByIntentId(actionId);
        } else {

            Utils.Logs('i', TAG, "Request product data by category. " +
                    "Name: " + categoryName + ", ID: " + categoryId);

            Category category = new Category();
            category.setId(categoryId);
            category.setName(categoryName);
            presenter.getProductByCategory(category);
        }
    }

    private void setupToolbarView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(actionName);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initBrowseListView() {
        adapter = new BrowseProductAdapter(listItem, presenter);
        adapter.setHasStableIds(true);
        rvProductBrowsing.setHasFixedSize(true);
        rvProductBrowsing.setLayoutManager(new GridLayoutManager(this, 2));
        rvProductBrowsing.setAdapter(adapter);
    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                swipeRefreshLayout.setRefreshing(true);
                break;
            case FINISHED:
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    @Override
    public void pupolateSellerProductList(ArrayList<Product> products) {
        listItem.clear();
        listItem.addAll(products);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProductDetail(Product product) {
        Utils.Logs('i', TAG, "Show product detail with id: " + product.id);
    }
}