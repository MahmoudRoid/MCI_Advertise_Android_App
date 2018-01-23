package ir.mahmoud.app.Adapters;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ir.mahmoud.app.Interfaces.OnLoadMoreListener;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context context;
    public List<tbl_PostModel> myObjectArrayList;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 1; // yeki monde b akhar k resid shoroo mikone load more ro call kardan
    private int lastVisibleItem, totalItemCount;


    public SearchAdapter(Context context, List<tbl_PostModel> arrayList,RecyclerView recyclerView) {
        this.myObjectArrayList = arrayList;
        this.context = context;


        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return myObjectArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
            return new SearchAdapter.DataObjectHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof DataObjectHolder) {
            ((DataObjectHolder) holder).tv_title.setText(myObjectArrayList.get(position).getTitle());
            ((DataObjectHolder) holder).tv_date.setText(myObjectArrayList.get(position).getDate());
            ((DataObjectHolder) holder).tv_kind.setText(myObjectArrayList.get(position).getCategorytitle());
            Glide.with(context).load(myObjectArrayList.get(position).getImageurl())
                    .apply(new RequestOptions().placeholder(R.mipmap.shodani)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(((DataObjectHolder) holder).image);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return myObjectArrayList == null ? 0 : myObjectArrayList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void addItems(List<tbl_PostModel> arrayList){
        this.myObjectArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tv_title, tv_date, tv_kind;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.title_tv);
            tv_date = (TextView) itemView.findViewById(R.id.date_tv);
            tv_kind = (TextView) itemView.findViewById(R.id.kind_tv);
            image = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.pb);
        }
    }
}

