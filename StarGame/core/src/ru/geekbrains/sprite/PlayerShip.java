package ru.geekbrains.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class PlayerShip extends Sprite {

    //private static float SPEED_LEN = 0.5f;
    private static final int INVALID_POINTER = -1;

    private TextureAtlas atlas;
    private Rect worldBounds;

    private Vector2 v = new Vector2();
    private Vector2 v0 = new Vector2(50f, 0);
    //private Vector2 touch;
    //private Vector2 newPos;
    //private Vector2 speed;
    private int hitPoints;
    private int damage;

    private boolean isPressedRight;
    private boolean isPressedLeft;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    public PlayerShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.atlas = atlas;
        /*this.touch = new Vector2();
        this.speed = new Vector2();
        this.newPos = new Vector2();*/
        setHeightProportion(10f);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setBottom(worldBounds.getBottom() + 5f);
    }

    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
        /*newPos.set(touch);
        if(newPos.sub(pos).len() <= SPEED_LEN) {
            pos.set(touch);
        } else {
            pos.add(speed);
        }*/
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
            case Input.Keys.UP:
                shoot();
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

    public void shoot() {

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
}
