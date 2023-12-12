package org.chatapp.event;

import org.chatapp.model.ModelRegister;
import org.chatapp.model.ModelLogin;

public interface EventLogin {

    public void login(ModelLogin login, EventMessage message);

    public void register(ModelRegister data, EventMessage message);

    public void goRegister();

    public void goLogin();
}
