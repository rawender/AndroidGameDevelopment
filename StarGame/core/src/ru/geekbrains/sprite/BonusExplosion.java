package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;

public class BonusExplosion extends Sprite {

    private Sound sound;

    private float animateInterval = 0.1f;
    private float animateTimer;

    public BonusExplosion(TextureAtlas atlas, Sound sound) {
        super(atlas.findRegion("bonus_explosion"), 1, 9, 9);
        this.sound = sound;
    }

    public void set(float height, Vector2 pos) {
        setHeightProportion(height);
        this.pos.set(pos);
        sound.play();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        animateTimer += delta;
        if (animateTimer >= animateInterval) {
            animateTimer = 0f;
            if (++frame == regions.length) {
                destroy();
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        frame = 0;
    }
}
