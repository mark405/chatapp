package org.chatapp.form;

import net.miginfocom.swing.MigLayout;
import org.chatapp.component.Chat_Body;
import org.chatapp.component.Chat_Bottom;
import org.chatapp.component.Chat_Title;
import org.chatapp.event.EventChat;
import org.chatapp.event.PublicEvent;
import org.chatapp.model.ModelReceiveMessage;
import org.chatapp.model.ModelSendMessage;
import org.chatapp.model.ModelUserAccount;

import javax.swing.*;
import java.awt.*;

public class Chat extends JPanel {
    private Chat_Title chatTitle;
    private Chat_Body chatBody;
    private Chat_Bottom chatBottom;
    public Chat() {
        initComponents();
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx", "0[fill]0", "0[]0[100%, bottom]0[shrink 0]0"));
        chatTitle = new Chat_Title();
        chatBody = new Chat_Body();
        chatBottom = new Chat_Bottom();
        PublicEvent.getInstance().addEventChat(new EventChat() {
            @Override
            public void receiveMessage(ModelReceiveMessage modelReceiveMessage) {
                if (chatTitle.getModelUserAccount().getId() == modelReceiveMessage.getFromUserId()) {
                    chatBody.addItemLeft(modelReceiveMessage);
                }
            }

            @Override
            public void sendMessage(ModelSendMessage modelSendMessage) {
                chatBody.addItemRight(modelSendMessage);
            }
        });
        add(chatTitle, "wrap");
        add(chatBody, "wrap");
        add(chatBottom, "h ::50%");
    }
    public void setUser(ModelUserAccount modelUserAccount) {
        chatTitle.setModelUserAccount(modelUserAccount);
        chatBottom.setUser(modelUserAccount);
        chatBody.clearChat();
    }
    public void updateUser(ModelUserAccount modelUserAccount) {
        chatTitle.updateUser(modelUserAccount);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new Color(255, 255, 255));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 727, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 681, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}