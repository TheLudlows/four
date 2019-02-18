package io.four.registry.config;

import java.util.Objects;

/**
 * present a host
 */
public class Host {

    private String address;
    private int port;

    public String getAddress() {
        return address;
    }

    public Host setAddress(String address) {
        this.address = address;
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
        return    address  + ":" + port ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Host host = (Host) o;
        return port == host.port &&
                Objects.equals(address, host.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port);
    }
}
