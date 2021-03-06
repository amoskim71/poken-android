package id.unware.poken.ui.category.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.FeaturedCategoryProduct;
import id.unware.poken.domain.Product;
import id.unware.poken.tools.glide.GlideRequest;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.category.presenter.ICategoryPresenter;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.SingleItemRowHolder> {

    private ArrayList<FeaturedCategoryProduct> itemsList;
    private Context mContext;
    private ICategoryPresenter homePresenter;
    private GlideRequest<Drawable> requestBuilder;

    public CategoryAdapter(Context context,
                           GlideRequests glideRequests,
                           ArrayList<FeaturedCategoryProduct> itemsList,
                           ICategoryPresenter homePresenter) {
        this.mContext = context;
        this.itemsList = itemsList;
        this.homePresenter = homePresenter;

        requestBuilder = glideRequests.asDrawable().fitCenter();
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.mContext).inflate(R.layout.list_featured_product_category, viewGroup, false);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int position) {

        FeaturedCategoryProduct singleItem = itemsList.get(position);
        ArrayList<Product> products = singleItem.products;
        holder.tvTitle.setText(singleItem.product_category.getName());

        for (int i = 0; i < holder.imgs.size(); i++) {
            if (!products.isEmpty()
                    && !products.get(i).images.isEmpty()) {

                requestBuilder
                        .clone()
                        .load(products.get(i).images.get(0).thumbnail)
                        .placeholder(R.drawable.bg_default_light)
                        .error(R.drawable.ic_image_black_24dp)
                        .centerCrop()
                        .into(holder.imgs.get(i));

            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePresenter.onCategoryClick(
                        holder.getAdapterPosition(),
                        itemsList.get(holder.getAdapterPosition()).product_category
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindViews({R.id.img1, R.id.img2, R.id.img3}) List<ImageView> imgs;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }

}