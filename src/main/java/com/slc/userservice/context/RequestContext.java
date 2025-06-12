package com.slc.userservice.context;

public class RequestContext {
    private static final ThreadLocal<String> actor = new ThreadLocal<>();
    private static final ThreadLocal<String> source = new ThreadLocal<>();
    private static final ThreadLocal<String> role = new ThreadLocal<>();

    public static void set(String actorEmail, String origin, String actorRole) {
        actor.set(actorEmail);
        source.set(origin);
        role.set(actorRole);
    }

    public static String getActor() {
        return actor.get();
    }

    public static String getSource() {
        return source.get();
    }

    public static String getRole() {
        return role.get();
    }

    public static void clear() {
        actor.remove();
        source.remove();
        role.remove();
    }
}
