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

import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;

public class MarkedAdapter extends RecyclerView.Adapter<MarkedAdapter.DataObjectHolder> {
    public Context context;
    public List<tbl_PostModel> myObjectArrayList;

    public MarkedAdapter(Context context, List<tbl_PostModel> arrayList) {
        this.myObjectArrayList = arrayList;
        this.context = context;
    }

    @Override
    public MarkedAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new MarkedAdapter.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(MarkedAdapter.DataObjectHolder holder, int position) {
        holder.txt_title.setText(myObjectArrayList.get(position).getTitle());
        holder.txt_date.setText(myObjectArrayList.get(position).getDate());
        Glide.with(context).load(myObjectArrayList.get(position).getImageurl())
                .apply(new RequestOptions().placeholder(R.mipmap.shodani)
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
        TextView txt_title, txt_date;


        public DataObjectHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            image = (ImageView) itemView.findViewById(R.id.img_post);
        }
    }
}

