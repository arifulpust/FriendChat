package com.arif.friendchat.Interface;

import java.util.List;

/**
 * Created by bipulkhan on 12/18/16.
 */

public interface DataController {



    void DataReceivedFromDataController(String response, int tag);
    void errorReceivedFromDataController(String error, int tag);
}
