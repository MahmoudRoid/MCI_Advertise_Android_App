package ir.mahmoud.app.Interfaces;

/**
 * Created by soheilsystem on 3/9/2018.
 */

public interface IWebServiceByTag {

    void getResult(Object result,String Tag) throws Exception;

    void getError(String ErrorCodeTitle,String Tag) throws Exception;

}
