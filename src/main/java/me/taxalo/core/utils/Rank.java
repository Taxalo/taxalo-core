package me.taxalo.core.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Rank {

    private List<String> permissions;
    private String prefix;
    private int priority;

    public Rank(List<String> permissions, String prefix, int priority) {
        this.permissions = permissions;
        this.prefix = prefix;
        this.priority = priority;
    }
}
