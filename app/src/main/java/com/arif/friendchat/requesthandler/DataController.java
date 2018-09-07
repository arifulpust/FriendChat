package com.arif.friendchat.requesthandler;

/**
 * Created by bipulkhan on 12/18/16.
 */

public interface DataController {

    void DataReceivedFromDataController(String response, int tag);
    void errorReceivedFromDataController(String error, int tag);
}
