package ir.mahmoud.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ir.mahmoud.app.Activities.VideoDetailActivity;
import ir.mahmoud.app.Holders.PostHolder;
import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.R;


public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 1;
    //private final int VIEW_TYPE_LOADING = 2;
    private List<PostModel> feedItemList;
    private ProgressBar pb;
    private int s = 0;
    private Context mContext;

    public ListAdapter(Context context, List<PostModel> feedItemList, ProgressBar pb) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.pb = pb;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {

        if (i == VIEW_TYPE_ITEM) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
            return new PostHolder(v);
        }/* else if (i == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading, null);
            return new LoadingViewHolder(view);
        }*/
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        /*if (feedItemList.get(position) == null)
            return VIEW_TYPE_LOADING;
        else*/
        return VIEW_TYPE_ITEM;
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof PostHolder) {

            final PostHolder Holder;
            try {
                Holder = (PostHolder) holder;
                Holder.setIsRecyclable(false);
                Holder.txt_title.setText(feedItemList.get(i).getTitle());
                Holder.txt_date.setText(feedItemList.get(i).getDate());
                Glide.with(mContext).load(feedItemList.get(i).getImageUrl())
                        .apply(new RequestOptions().placeholder(R.mipmap.homeb)
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .into(((PostHolder) holder).img_post);
            } catch (Exception e) {
            }
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
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public void addItem(PostModel post) {
        this.feedItemList.add(post);
        notifyDataSetChanged();
    }
}