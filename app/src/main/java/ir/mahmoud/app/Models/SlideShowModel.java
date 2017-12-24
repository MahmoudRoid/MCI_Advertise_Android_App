package ir.mahmoud.app.Models;

import java.io.Serializable;

public class SlideShowModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String imageUrl = "";


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getImage() {
        return imageUrl;
    }

    public void setImage(String image) {
        imageUrl = image;
    }
}
