package io.four.remoting;

public interface Remoting {

    void loadConfig(Config config);

    boolean start();

    void close();

}
