package ru.geekbrains.pool;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.sprite.BonusExplosion;

public class BonusExplosionPool extends SpritesPool<BonusExplosion> {

    private TextureAtlas atlas;
    private Sound sound;

    public BonusExplosionPool(TextureAtlas atlas, Sound sound) {
        this.atlas = atlas;
        this.sound = sound;
    }
    @Override
    protected BonusExplosion newObject() {
        return new BonusExplosion(atlas, sound);
    }
}
