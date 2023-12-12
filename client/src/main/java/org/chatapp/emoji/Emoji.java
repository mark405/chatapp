package org.chatapp.emoji;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Emoji {
    private static Emoji instance;

    public static Emoji getInstance() {
        if (instance == null) {
            instance = new Emoji();
        }
        return instance;
    }

    private Emoji() {
    }

    public List<ModelEmoji> getStyle1() {
        List<ModelEmoji> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            list.add(new ModelEmoji(i, new ImageIcon(getClass().getResource("/org/chatapp/emoji/icon/" + i + ".png"))));
        }
        return list;
    }

    public List<ModelEmoji> getStyle2() {
        List<ModelEmoji> list = new ArrayList<>();
        for (int i = 21; i <= 40; i++) {
            list.add(new ModelEmoji(i, new ImageIcon(getClass().getResource("/org/chatapp/emoji/icon/" + i + ".png"))));
        }
        return list;
    }

    public ModelEmoji getEmoji(int id) {
        return new ModelEmoji(id, new ImageIcon(getClass().getResource("/org/chatapp/emoji/icon/" + id + ".png")));
    }
}
