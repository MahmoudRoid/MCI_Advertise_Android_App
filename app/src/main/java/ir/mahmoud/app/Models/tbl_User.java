package ir.mahmoud.app.Models;

import com.orm.SugarRecord;

/**
 * Created by soheilsystem on 3/11/2018.
 */

public class tbl_User extends SugarRecord<tbl_User> {

    public String number;

    public tbl_User(){}

    public tbl_User(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
