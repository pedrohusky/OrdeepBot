package com.example.ordeepbot.ui.portfolio;

public class Balance {
    private String asset;
    private float free;
    private float locked;

    public Balance(String asset, float free, float locked) {
        this.asset = asset;
        this.free = free;
        this.locked = locked;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public float getFree() {
        return free;
    }

    public void setFree(float free) {
        this.free = free;
    }

    public float getLocked() {
        return locked;
    }

    public void setLocked(float locked) {
        this.locked = locked;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Balance))
            return false;
        Balance balance = (Balance) obj;
        return balance.getAsset().equals(this.asset) && balance.getFree() == this.free && balance.getLocked() == this.locked;
    }
}
