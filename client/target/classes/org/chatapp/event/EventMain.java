package org.chatapp.event;

import org.chatapp.model.ModelUserAccount;

public interface EventMain {

    void showLoading(boolean show);

    void initChat();
    void selectUser(ModelUserAccount modelUserAccount);
    void updateUser(ModelUserAccount modelUserAccount);
}
