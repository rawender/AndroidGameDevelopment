package ru.geekbrains.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.pool.ShieldPool;
import ru.geekbrains.utils.Regions;

public class PlayerShip extends Ship {

    private static final int INVALID_POINTER = 0;
    private static final int HP = 100;

    private Vector2 v0 = new Vector2(25f, 0);

    private ShieldPool shieldPool;

    private boolean isPressedRight;
    private boolean isPressedLeft;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    private Vector2 lastTouch = new Vector2();

    public PlayerShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, ShieldPool shieldPool, Sound shootSound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        setHeightProportion(13f);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.shieldPool = shieldPool;
        this.bulletRegions = Regions.split(atlas.findRegion("bulletMainShip"), 1, 16, 16);
        this.bulletHeight = 3.5f;
        this.bulletV.set(0, 50f);
        this.damage = 1;
        this.hp = HP;
        this.reloadInterval = 0.2f;
        this.shootSound = shootSound;
    }

    public void startNewGame(Rect worldBounds) {
        this.hp = HP;
        this.damage = 1;
        pos.x = worldBounds.pos.x;
        v.setZero();
        flushDestroy();
        isPressedRight = false;
        isPressedLeft = false;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + 5f);
    }

    public void update(float delta) {
        super.update(delta);
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }
        this.pos.mulAdd(v, delta);
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) {
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) {
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }
        lastTouch.set(touch);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    public boolean touchDragged(Vector2 touch, int pointer) {
        this.pos.set(touch.x, this.pos.y);
        return false;
    }

    public void keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                isPressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                isPressedRight = true;
                moveRight();
                break;
        }
    }

    public void keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                isPressedLeft = false;
                if (isPressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                isPressedRight = false;
                if (isPressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(
                bullet.getRight() < getLeft()
                        || bullet.getLeft() > getRight()
                        || bullet.getBottom() > pos.y
                        || bullet.getTop() < getBottom()
        );
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }

    public static int getHP() {
        return HP;
    }

    public void shieldUp() {
        Shield shield = shieldPool.obtain();
        shield.set(this.getHeight() * 1.2f, this.pos);
    }
}
