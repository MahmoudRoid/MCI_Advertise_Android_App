package ir.mahmoud.app.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.DataObjectHolder> {
    public Context context;
    public List<PostModel> myObjectArrayList;


    public SearchAdapter(Context context, List<PostModel> arrayList) {
        this.myObjectArrayList = arrayList;
        this.context = context;
    }

    @Override
    public SearchAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchAdapter.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.DataObjectHolder holder, int position) {
        holder.tv_title.setText(myObjectArrayList.get(position).getTitle());
        holder.tv_date.setText(myObjectArrayList.get(position).getDate());
        holder.tv_kind.setText(myObjectArrayList.get(position).getCategoryTitle());
        Glide.with(context).load(myObjectArrayList.get(position).getImageUrl())
                .apply(new RequestOptions().placeholder(R.mipmap.homeb)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return myObjectArrayList.size();
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
}

