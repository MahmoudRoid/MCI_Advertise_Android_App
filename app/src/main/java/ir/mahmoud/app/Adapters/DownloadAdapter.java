package ir.mahmoud.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

import ir.mahmoud.app.Activities.ShowVideoActivity;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Models.tbl_Download;
import ir.mahmoud.app.R;


public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DataObjectHolder> {
    public Context context;
    public List<tbl_Download> myObjectArrayList;


    public DownloadAdapter(Context context, List<tbl_Download> arrayList) {
        this.myObjectArrayList = arrayList;
        this.context = context;
    }

    @Override
    public DownloadAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_item, parent, false);
        return new DownloadAdapter.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DownloadAdapter.DataObjectHolder holder, final int position) {
        holder.tv_title.setText(myObjectArrayList.get(position).getTitle());
        holder.tv_date.setText(myObjectArrayList.get(position).getDate());
        Glide.with(context).load(myObjectArrayList.get(position).getImageurl())
                .apply(new RequestOptions().placeholder(R.mipmap.homeb)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(holder.image);

        holder.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Application.VIDEO + "/" + myObjectArrayList.get(position).getPostid() + ".mp4");
                if (file.exists()) {
                    try {
                        File DoutFile = new File(Application.VIDEO + "/" + myObjectArrayList.get(position).getPostid() + ".mp4");
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("video/*");
                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(DoutFile));
                        share.putExtra(Intent.EXTRA_TEXT,
                                "" + context.getResources().getString(R.string.app_name));
                        context.startActivity(Intent.createChooser(share, "اشتراک ویدئو"));
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                        HSH.showtoast(context, "مشکلی در ارسال ویدئو به وجود آمد");
                    }
                } else {
                    HSH.showtoast(context, "فایل ویدئو یافت نشد");
                }
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if video exists
                File file = new File(Application.VIDEO + "/" + myObjectArrayList.get(position).getPostid() + ".mp4");
                if (file.exists()) {
                    Intent intent = new Intent(context, ShowVideoActivity.class);
                    intent.putExtra("id", String.valueOf(myObjectArrayList.get(position).getPostid()));
                    intent.putExtra("title", myObjectArrayList.get(position).getTitle());
                    intent.putExtra("content", myObjectArrayList.get(position).getContent());
                    intent.putExtra("date", myObjectArrayList.get(position).getDate());
                    intent.putExtra("categoryTitle", myObjectArrayList.get(position).getCategorytitle());
                    intent.putExtra("url", myObjectArrayList.get(position).getVideourl());
                    intent.putExtra("imageUrl", myObjectArrayList.get(position).getImageurl());
                    intent.putExtra("tagSlug", myObjectArrayList.get(position).getTagslug());
                    context.startActivity(intent);
                } else {
                    // nist
                    HSH.showtoast(context, "فایل ویدئو یافت نشد");
                }

            }
        });
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
        ImageView image, shareIcon;
        TextView tv_title, tv_date;
        CardView cardView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.title_tv);
            tv_date = (TextView) itemView.findViewById(R.id.date_tv);
            shareIcon = (ImageView) itemView.findViewById(R.id.share_icon);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }

    }


}


