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

import java.util.ArrayList;

import ir.mahmoud.app.Models.PostModel;
import ir.mahmoud.app.R;

public class SameVideoAdapter extends RecyclerView.Adapter<SameVideoAdapter.DataObjectHolder> {
    public Context context;
    public ArrayList<PostModel> myObjectArrayList;
    public String Language_kind;

    public SameVideoAdapter(Context context, ArrayList<PostModel> arrayList) {
        this.myObjectArrayList = arrayList;
        this.context = context;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tv_title, tv_date, tv_bazdid;


        public DataObjectHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.title_tv);
            tv_date = (TextView) itemView.findViewById(R.id.date_tv);
            //tv_bazdid = (TextView) itemView.findViewById(R.id.bazdid_tv);
            image = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }


    @Override
    public SameVideoAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.same_video_item, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(SameVideoAdapter.DataObjectHolder holder, int position) {
        holder.tv_title.setText(myObjectArrayList.get(position).getTitle());
        holder.tv_date.setText(myObjectArrayList.get(position).getDate());
        //holder.tv_bazdid.setText(myObjectArrayList.get(position).getTitle());
        Glide.with(context).load(myObjectArrayList.get(position).getImageUrl())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
               .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return myObjectArrayList.size();
    }
}
