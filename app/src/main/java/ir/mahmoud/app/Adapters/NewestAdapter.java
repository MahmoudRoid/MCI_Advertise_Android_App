package ir.mahmoud.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.CardView;
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

import java.io.File;
import java.util.List;

import ir.mahmoud.app.Activities.ShowVideoActivity;
import ir.mahmoud.app.Activities.VideoDetailActivity;
import ir.mahmoud.app.Classes.Application;
import ir.mahmoud.app.Classes.HSH;
import ir.mahmoud.app.Interfaces.OnLoadMoreListener;
import ir.mahmoud.app.Models.tbl_Download;
import ir.mahmoud.app.Models.tbl_PostModel;
import ir.mahmoud.app.R;


public class NewestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public Context context;
    public List<tbl_Download> myObjectArrayList;
    public String FragType;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 1; // yeki monde b akhar k resid shoroo mikone load more ro call kardan
    private int lastVisibleItem, totalItemCount;


    public NewestAdapter(Context context, List<tbl_Download> arrayList, String FragType, RecyclerView recyclerView) {
        this.myObjectArrayList = arrayList;
        this.context = context;
        this.FragType = FragType;

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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_item, parent, false);
            return new MyDataObjectHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyDataObjectHolder) {
            ((MyDataObjectHolder) holder).tv_title.setText(myObjectArrayList.get(position).getTitle());
            ((MyDataObjectHolder) holder).tv_date.setText(myObjectArrayList.get(position).getDate());
//            ((MyDataObjectHolder) holder).tv_kind.setText(myObjectArrayList.get(position).getCategorytitle());
            Glide.with(context).load(myObjectArrayList.get(position).getImageurl())
                    .apply(new RequestOptions().placeholder(R.mipmap.shodani)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(((MyDataObjectHolder) holder).image);

            ((MyDataObjectHolder) holder).shareIcon.setOnClickListener(new View.OnClickListener() {
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


            ((MyDataObjectHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FragType.equals("Download")) {
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
                            intent.putExtra("postUrl", myObjectArrayList.get(position).getPosturl());
                            context.startActivity(intent);
                        } else {
                            // nist
                            HSH.showtoast(context, "فایل ویدئو یافت نشد");
                        }
                    } else if (FragType.equals("Newest")) {
                        Intent intent;
                        intent = new Intent(context, VideoDetailActivity.class);
                        intent.putExtra("feedItem", tblPostModelToTblDownload(myObjectArrayList.get(position)));
                        context.startActivity(intent);
                    } else {
                        Intent intent;
                        intent = new Intent(context, VideoDetailActivity.class);
                        intent.putExtra("feedItem", myObjectArrayList.get(position));
                        context.startActivity(intent);
                    }

                }
            });


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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private tbl_PostModel tblPostModelToTblDownload(tbl_Download item) {

        tbl_PostModel tbl = new tbl_PostModel();

        tbl.postid = item.getPostid();
        tbl.title = item.getTitle();
        tbl.content = item.getContent();
        tbl.date = item.getDate();
        tbl.categorytitle = item.getCategorytitle();
        tbl.videourl = item.getVideourl();
        tbl.imageurl = item.getImageurl();
        tbl.tagslug = item.getTagslug();
        tbl.posturl = item.getPosturl();

        return tbl;
    }

    public static class MyDataObjectHolder extends RecyclerView.ViewHolder {
        ImageView image, shareIcon;
        TextView tv_title, tv_date;
        CardView cardView;

        public MyDataObjectHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.title_tv);
            tv_date = (TextView) itemView.findViewById(R.id.date_tv);
            shareIcon = (ImageView) itemView.findViewById(R.id.share_icon);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
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



