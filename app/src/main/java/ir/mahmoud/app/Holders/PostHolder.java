package ir.mahmoud.app.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ir.mahmoud.app.R;

public class PostHolder extends RecyclerView.ViewHolder {
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