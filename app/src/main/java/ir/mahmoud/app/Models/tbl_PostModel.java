package ir.mahmoud.app.Models;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by soheilsystem on 12/24/2017.
 */

public class tbl_PostModel extends SugarRecord<tbl_PostModel> implements Serializable {

    public static final long serialVersionUID = 1L;
    public long postid;
    public String title, content, date, categorytitle, videourl, imageurl, tagslug, posturl;

    public tbl_PostModel() {
    }

    public tbl_PostModel(long postid, String title, String content, String date, String categorytitle, String videourl, String imageurl, String tagslug, String posturl) {
        this.postid = postid;
        this.title = title;
        this.content = content;
        this.date = date;
        this.categorytitle = categorytitle;
        this.videourl = videourl;
        this.imageurl = imageurl;
        this.tagslug = tagslug;
        this.posturl = posturl;
    }

    public String getPosturl() {
        return posturl;
    }

    public void setPosturl(String posturl) {
        this.posturl = posturl;
    }

    public long getPostid() {
        return postid;
    }

    public void setPostid(long postid) {
        this.postid = postid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategorytitle() {
        return categorytitle;
    }

    public void setCategorytitle(String categorytitle) {
        this.categorytitle = categorytitle;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTagslug() {
        return tagslug;
    }

    public void setTagslug(String tagslug) {
        this.tagslug = tagslug;
    }
}
