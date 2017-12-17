package ir.mahmoud.app.Interfaces;


import java.util.List;

import ir.mahmoud.app.Models.PostModel;

public interface IWerbService {

    void getResult(List<PostModel> feed) throws Exception;
    void getError(String ErrorCodeTitle)throws Exception;
}
