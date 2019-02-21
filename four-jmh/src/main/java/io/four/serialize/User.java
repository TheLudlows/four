package io.four.serialize;

import java.io.Serializable;

/**
 * @author TheLudlows
 */
public class User implements Serializable {
    int age;

    public User(int i) {
        age = i;
    }

    public User() {
    }
}
