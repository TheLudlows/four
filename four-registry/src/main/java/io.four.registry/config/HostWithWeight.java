package io.four.registry.config;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HostWithWeight extends Host {
    private int weight;

    public HostWithWeight(String address, int port, int weight) {
        super(address, port);
        this.weight = weight;
    }

    public HostWithWeight(String host, int weight) {
        super(host);
        this.weight = weight;
    }

    public static HostWithWeight buildHWW(String string) {
        String[] str = string.split("@");
        return new HostWithWeight(str[0], Integer.parseInt(str[1]));
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
        return super.toString() + "@" + weight;
    }


    public int getWeight() {
        return weight;
    }

    public HostWithWeight setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
