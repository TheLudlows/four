package io.four.registry.config;

import java.util.Objects;

/**
 * present a host
 */
public class Host {

    private String ip;
    private int port;


    public Host(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Host(String address) {
        String[] array = address.split(":");
        this.ip = array[0].trim();
        this.port = Integer.parseInt(array[1].trim());
    }

    public Host() {
    }

    public String getIp() {
        return ip;
    }

    public Host setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return port;
    }

    public Host setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return ip + ":" + port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Host host = (Host) o;
        return port == host.port &&
                Objects.equals(ip, host.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
