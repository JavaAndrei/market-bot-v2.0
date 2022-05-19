package pro.keenetic.marketbot.bot.market_bot.models;

public enum Permission {
    ADMIN("permissions:admin"),
    OBSERVER("permissions:observer"),
    TRADER("permissions:trader");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }


}
