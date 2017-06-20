package com.example.hoang.mobies.network.guest_session;

/**
 * Created by Inpriron on 6/20/2017.
 */

public class GuestObject {
    boolean success;
    String guest_session_id;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getGuest_session_id() {
        return guest_session_id;
    }

    public void setGuest_session_id(String guest_session_id) {
        this.guest_session_id = guest_session_id;
    }
}
