package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;

public class BattleShip extends Sprite {

    private static float SPEED_LEN = 0.5f;

    private Vector2 touch;
    private Vector2 newPos;
    private Vector2 speed;

    public BattleShip(TextureRegion region) {
        super(region);
        setSize(10f, 10f);
        this.touch = new Vector2();
        this.speed = new Vector2();
        this.newPos = new Vector2();
    }

    public void update() {
        newPos.set(touch);
        if(newPos.sub(pos).len() <= SPEED_LEN) {
            pos.set(touch);
        } else {
            pos.add(speed);
        }
    }

    public boolean touchDown(Vector2 touch, int pointer) {
        this.touch = touch;
        speed = touch.cpy().sub(pos).setLength(SPEED_LEN);
        return false;
    }
}
