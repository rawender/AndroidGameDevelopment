package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Shield extends Sprite {

    protected Rect worldBounds;

    private float animateInterval = 0.017f;
    private float animateTimer;
    private float animateCount = 1f;
    private float actionTimer;
    private int count = 0;

    public Shield(TextureAtlas atlas, Rect worldBounds) {
        super(atlas.findRegion("shield"), 1, 11, 11);
        this.worldBounds = worldBounds;
    }

    public void set(float height, Vector2 pos) {
        setHeightProportion(height);
        this.pos.set(pos);
    }

    public void update(float delta, Vector2 pos, ShieldBar shieldBar) {
        animateTimer += delta;
        actionTimer += delta;
        if (animateTimer >= animateInterval) {
            animateTimer = 0f;
            if (++frame == regions.length) {
                frame = 0;
            }
        }
        if (actionTimer >= animateCount) {
            System.out.println("count: " + count);
            actionTimer = 0f;
            if (++count == 10) {
                destroy();
            }
        }
        this.pos.set(pos);
        shieldBar.setSize(0.5f,7f, count);
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(
                bullet.getRight() < getLeft()
                        || bullet.getLeft() > getRight()
                        || bullet.getBottom() > pos.y
                        || bullet.getTop() < getBottom()
        );
    }

    @Override
    public void destroy() {
        super.destroy();
        animateTimer = 0f;
        actionTimer = 0f;
        frame = 0;
        count = 0;
    }
}
