package ir.mahmoud.app.Models;

import java.io.Serializable;

public class SlideShowModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String Id = "";
    private String Image = "";

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
