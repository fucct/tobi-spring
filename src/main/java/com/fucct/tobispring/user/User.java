package com.fucct.tobispring.user;

public class User {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;

    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;

    public User(final String id, final String name, final String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = Level.BASIC;
        this.login = 0;
        this.recommend = 0;
    }

    public User(final String id, final String name, final String password, final Level level, final int login,
        final int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public User() {
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + " 은 업데이트 불가능합니다.");
        }else{
            if (canUpgradeLevel()) {
                this.level = nextLevel;
            }
        }
    }

    public boolean canUpgradeLevel() {
        switch (this.level) {
            case BASIC:
                return getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
            case SILVER:
                return getRecommend() >= MIN_RECCOMEND_FOR_GOLD;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level: " + this.level);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(final int login) {
        this.login = login;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(final int recommend) {
        this.recommend = recommend;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(final Level level) {
        this.level = level;
    }
}
