package org.chatapp.event;

import org.chatapp.model.ModelUserAccount;

import java.util.List;

public interface EventMenuLeft {
    void newUser(List<ModelUserAccount> modelUserAccountList);
    void userConnect(int id);
    void userDisconnect(int id);
}
