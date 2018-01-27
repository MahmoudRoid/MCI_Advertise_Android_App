package ir.mahmoud.app.Adapters;

import android.content.Context;
import android.content.Intent;
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

import ir.mahmoud.app.Activities.VideoDetailActivity;
import ir.mahmoud.app.Interfaces.OnLoadMoreListener;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;


public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    private final int VIEW_TYPE_ITEM = 0;
    //private final int VIEW_TYPE_LOADING = 2;
    private List<tbl_PostModel> feedItemList;
    private ProgressBar pb;
    private int s = 0;
    private Context mContext;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 1; // yeki monde b akhar k resid shoroo mikone load more ro call kardan
    private int lastVisibleItem, totalItemCount;



    public ListAdapter(Context context, List<tbl_PostModel> feedItemList, ProgressBar pb,RecyclerView recyclerView) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.pb = pb;

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new PostHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return feedItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof PostHolder) {
            ((PostHolder) holder).txt_title.setText(feedItemList.get(i).getTitle());
            ((PostHolder) holder).txt_date.setText(feedItemList.get(i).getDate());

            Glide.with(mContext).load(feedItemList.get(i).getImageurl())
                    .apply(new RequestOptions().placeholder(R.mipmap.shodani)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(((PostHolder) holder).img_post);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(mContext, VideoDetailActivity.class);
                intent.putExtra("feedItem", feedItemList.get(i));
                mContext.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return feedItemList == null ? 0 : feedItemList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void addItem(tbl_PostModel post) {
        this.feedItemList.add(post);
        notifyDataSetChanged();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {

        public TextView txt_title;
        public TextView txt_date;
        public ImageView img_post;

        public PostHolder(View view) {
            super(view);
            this.txt_title = view.findViewById(R.id.txt_title);
            this.txt_date = view.findViewById(R.id.txt_date);
            this.img_post = view.findViewById(R.id.img_post);
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