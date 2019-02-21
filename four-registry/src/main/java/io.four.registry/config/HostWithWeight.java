package io.four.registry.config;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HostWithWeight {
    private Host host;
    private int weight;

    public HostWithWeight(String address, int port, int weight) {
        host = new Host(address, port);
        this.weight = weight;
    }

    public HostWithWeight() {
    }

    public HostWithWeight(Host host, int weight) {
        this.host = host;
        this.weight = weight;
    }

    public static HostWithWeight buildHWW(String string) {
        String[] str = string.split("@");
        return new HostWithWeight().setHost(new Host(str[0])).setWeight(Integer.parseInt(str[1]));
    }

    public static List<HostWithWeight> buildHWWs(List<String> strings) {
        if (strings == null || strings.size() < 1) {
            return null;
        }
        List list = new CopyOnWriteArrayList();
        for (String str : strings) {
            list.add(buildHWW(str));
        }
        return list;
    }

    @Override
    public String toString() {
        return host + "@" + weight;
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
