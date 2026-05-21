/**
 * Hero.java
 * Circular morphing player (FULL WORKING VERSION)
 */

package entities;

import engine.InputHandler;
import world.Platform;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class Hero extends Entity {

    // ===============================
    // PHYSICS
    // ===============================

    private final float ACCELERATION = 0.55f;
    private final float GROUND_ACCELERATION = 0.72f;
    private final float MAX_SPEED = 5.5f;

    private final float FRICTION = 0.82f;
    private final float AIR_FRICTION = 0.96f;

    private final float GRAVITY = 0.55f;
    private final float MAX_FALL_SPEED = 12f;

    private final float JUMP_FORCE = -12f;

    // ===============================
    // GAME FEEL
    // ===============================

    private boolean onGround;

    private int coyoteTimer = 0;
    private final int COYOTE_TIME = 8;

    private int jumpBufferTimer = 0;
    private final int JUMP_BUFFER = 8;

    // ===============================
    // INPUT
    // ===============================

    private InputHandler input;
    private PlayerState state = PlayerState.IDLE;

    // ===============================
    // SHAPE SYSTEM (CIRCLE CORE)
    // ===============================

    private float radius = 12f; // 🔥 REDUIT

    private float squashX = 1f;
    private float squashY = 1f;

    private float visualX = 1f;
    private float visualY = 1f;

    // ===============================
    // DASH
    // ===============================

    private boolean isDashing = false;
    private int dashTimer = 0;
    private final int DASH_DURATION = 12;
    private final float DASH_SPEED = 11.5f;

    private int dashCooldown = 0;
    private final int DASH_COOLDOWN = 28;

    // ===============================
    // WALL
    // ===============================

    private boolean touchingLeftWall = false;
    private boolean touchingRightWall = false;

    private final float WALL_SLIDE_GRAVITY = 0.28f;
    private final float WALL_JUMP_FORCE_X = 6.5f;
    private final float WALL_JUMP_FORCE_Y = -11f;

    // ===============================
    // DEBUG
    // ===============================

    public boolean ignoreHorizontalCollisions = false;

    // ===============================
    // INIT
    // ===============================

    public Hero(float x, float y, InputHandler input) {
        super(x, y, 24, 24);
        this.input = input;
    }

    // ===============================
    // MAIN UPDATE
    // ===============================

    public void update(List<Platform> platforms) {

        handleInput();
        applyPhysics();

        x += vx;
        updateHitbox();
        horizontalCollisions(platforms);

        y += vy;
        updateHitbox();
        verticalCollisions(platforms);

        checkGrounded(platforms);

        updateState();
        updateShape();

        updateHitbox();
    }

    @Override
    public void update() {}

    // ===============================
    // INPUT
    // ===============================

    private void handleInput() {

        boolean left = input.isPressed(KeyEvent.VK_A) || input.isPressed(KeyEvent.VK_LEFT);
        boolean right = input.isPressed(KeyEvent.VK_D) || input.isPressed(KeyEvent.VK_RIGHT);

        boolean jump = input.isPressed(KeyEvent.VK_SPACE)
                || input.isPressed(KeyEvent.VK_W)
                || input.isPressed(KeyEvent.VK_UP);

        boolean dash = input.isPressed(KeyEvent.VK_SHIFT);

        float accel = onGround ? GROUND_ACCELERATION : ACCELERATION;

        if (left) vx -= accel;
        if (right) vx += accel;

        if (!left && !right) {
            vx *= onGround ? FRICTION : AIR_FRICTION;
        }

        if (Math.abs(vx) < 0.05f) vx = 0;

        vx = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, vx));

        if (jump) jumpBufferTimer = JUMP_BUFFER;

        if (jumpBufferTimer > 0 && coyoteTimer > 0) {
            vy = JUMP_FORCE;
            onGround = false;
            jumpBufferTimer = 0;
            coyoteTimer = 0;
        }

        if (dash && dashCooldown <= 0 && !isDashing) {
            isDashing = true;
            dashTimer = DASH_DURATION;
            dashCooldown = DASH_COOLDOWN;
        }

        if (jump && (touchingLeftWall || touchingRightWall) && !onGround) {
            float dir = touchingLeftWall ? 1 : -1;
            vx = WALL_JUMP_FORCE_X * dir;
            vy = WALL_JUMP_FORCE_Y;

            touchingLeftWall = false;
            touchingRightWall = false;
        }
    }

    // ===============================
    // PHYSICS
    // ===============================

    private void applyPhysics() {

        if (isDashing) {
            float dir = (vx >= 0) ? 1f : -1f;
            vx = DASH_SPEED * dir;
            vy *= 0.6f;
        } else {
            vy += (touchingLeftWall || touchingRightWall)
                    ? WALL_SLIDE_GRAVITY
                    : GRAVITY;
        }

        vy = Math.min(vy, MAX_FALL_SPEED);

        if (coyoteTimer > 0) coyoteTimer--;
        if (jumpBufferTimer > 0) jumpBufferTimer--;
        if (dashCooldown > 0) dashCooldown--;
        if (dashTimer > 0) dashTimer--;

        if (dashTimer <= 0) isDashing = false;
    }

    // ===============================
    // COLLISIONS HORIZONTAL
    // ===============================

    private void horizontalCollisions(List<Platform> platforms) {

        if (ignoreHorizontalCollisions) return;

        touchingLeftWall = false;
        touchingRightWall = false;

        Rectangle2D.Float future =
                new Rectangle2D.Float(x, y + 2, width, height - 4);

        for (Platform p : platforms) {

            if (future.intersects(p.getBounds())) {

                if (vx > 0) {
                    x = p.getX() - width;
                    touchingRightWall = true;
                } else if (vx < 0) {
                    x = p.getX() + p.getW();
                    touchingLeftWall = true;
                }

                vx = 0;
                updateHitbox();
            }
        }
    }

    // ===============================
    // COLLISIONS VERTICAL
    // ===============================

    private void verticalCollisions(List<Platform> platforms) {

        for (Platform p : platforms) {

            if (hitbox.intersects(p.getBounds())) {

                if (vy > 0) {
                    y = p.getY() - height;
                    vy = 0;
                    onGround = true;
                    coyoteTimer = COYOTE_TIME;
                }

                if (vy < 0) {
                    y = p.getY() + p.getH();
                    vy = 0;
                }

                updateHitbox();
            }
        }
    }

    // ===============================
    // GROUND CHECK
    // ===============================

    private void checkGrounded(List<Platform> platforms) {

        float feetY = y + height + 1.5f;

        float left = x + 1;
        float right = x + width - 1;

        boolean grounded = false;

        for (Platform p : platforms) {

            boolean xOverlap = right > p.getX() && left < p.getX() + p.getW();
            boolean yClose = feetY >= p.getY() && feetY <= p.getY() + 3f;

            if (xOverlap && yClose && vy >= 0) {
                grounded = true;
                break;
            }
        }

        onGround = grounded;

        if (onGround) coyoteTimer = COYOTE_TIME;
    }

    // ===============================
    // SHAPE SYSTEM
    // ===============================

    private void updateShape() {

        float speed = Math.min(Math.abs(vx) / MAX_SPEED, 1f);

        float targetX = 1f;
        float targetY = 1f;

        targetX += speed * 0.4f;
        targetY -= speed * 0.25f;

        if (!onGround) {
            if (vy < 0) {
                targetX = 1.25f;
                targetY = 0.75f;
            } else {
                targetX = 0.85f;
                targetY = 1.25f;
            }
        }

        if (isDashing) {
            targetX = 1.6f;
            targetY = 0.65f;
        }

        if (touchingLeftWall || touchingRightWall) {
            targetX = 0.8f;
            targetY = 1.2f;
        }

        squashX += (targetX - squashX) * 0.18f;
        squashY += (targetY - squashY) * 0.18f;

        visualX += (squashX - visualX) * 0.25f;
        visualY += (squashY - visualY) * 0.25f;
    }

    // ===============================
    // RENDER
    // ===============================

    @Override
    public void render(Graphics2D g, int camX, int camY) {

        Graphics2D g2 = (Graphics2D) g.create();

        float drawX = x - camX;
        float drawY = y - camY;

        float w = radius * 2f * visualX;
        float h = radius * 2f * visualY;

        drawX -= (w - radius * 2f) / 2f;
        drawY -= (h - radius * 2f);

        Color c;

        switch (state) {
            case RUN -> c = new Color(0, 200, 255);
            case JUMP -> c = Color.WHITE;
            case FALL -> c = new Color(255, 120, 180);
            case DASH -> c = new Color(255, 140, 40);
            case WALL_SLIDE -> c = new Color(180, 140, 255);
            default -> c = new Color(80, 220, 255);
        }

        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillOval((int)(drawX + 2), (int)(drawY + h - 4), (int)w, (int)(h * 0.4f));

        g2.setColor(c);
        g2.fillOval((int)drawX, (int)drawY, (int)w, (int)h);

        g2.setColor(new Color(255, 255, 255, 70));
        g2.fillOval((int)(drawX + w * 0.2f), (int)(drawY + h * 0.2f),
                (int)(w * 0.35f), (int)(h * 0.35f));

        g2.dispose();
    }

    // ===============================
    // STATE
    // ===============================

    private void updateState() {

        if (isDashing) {
            state = PlayerState.DASH;
            return;
        }

        if (!onGround) {
            state = (vy < 0) ? PlayerState.JUMP : PlayerState.FALL;
        } else {
            state = (Math.abs(vx) > 0.5f)
                    ? PlayerState.RUN
                    : PlayerState.IDLE;
        }
    }

    public PlayerState getState() {
        return state;
    }
}