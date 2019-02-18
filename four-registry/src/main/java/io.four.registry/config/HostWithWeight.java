package io.four.registry.config;

public class HostWithWeight {
    private Host host;
    private int weight;

    public HostWithWeight(Host host, int weight) {
        this.host = host;
        this.weight = weight;
    }

    public HostWithWeight(String string) {
    }

    @Override
    public String toString() {
        return  host + "@" + weight;
    }

    public Host getHost() {
        return host;
    }

    public HostWithWeight setHost(Host host) {
        this.host = host;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public HostWithWeight setWeight(int weight) {
        this.weight = weight;
        return this;
    }
}
