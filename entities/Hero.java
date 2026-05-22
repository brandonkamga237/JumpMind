/**
 * Hero.java
 *
 * Main playable character class.
 *
 * Features:
 * - Smooth platformer movement
 * - Jump buffering
 * - Coyote time
 * - Dash system
 * - Wall sliding & wall jumping
 * - Dynamic squash/stretch animation
 * - Collision handling
 * - Player state management
 *
 * The hero is rendered as a morphing circular shape
 * inspired by modern precision platformers.
 */

package entities;

// Handles keyboard input
import engine.InputHandler;

// Platform collision objects
import world.Platform;

// Java graphics & geometry imports
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Hero player entity.
 *
 * Extends the base Entity class and contains:
 * - physics
 * - movement
 * - rendering
 * - collision logic
 * - gameplay systems
 */
public class Hero extends Entity {

    // ======================================================
    // MOVEMENT PHYSICS
    // ======================================================

    // Horizontal acceleration while moving in air
    private final float ACCELERATION = 0.55f;

    // Horizontal acceleration while moving on the ground
    // Higher than air acceleration for snappier controls
    private final float GROUND_ACCELERATION = 0.72f;

    // Maximum horizontal speed
    private final float MAX_SPEED = 5.5f;


    // ======================================================
    // FRICTION & GRAVITY
    // ======================================================

    // Ground friction
    // Quickly slows the player when no input is pressed
    private final float FRICTION = 0.82f;

    // Air friction
    // Keeps momentum longer while airborne
    private final float AIR_FRICTION = 0.96f;

    // Gravity force applied every frame
    private final float GRAVITY = 0.55f;

    // Maximum vertical falling speed
    private final float MAX_FALL_SPEED = 12f;


    // ======================================================
    // JUMP SETTINGS
    // ======================================================

    // Upward jump velocity
    // Negative because Y axis goes downward in Java graphics
    private final float JUMP_FORCE = -12f;

    // True when player is standing on the ground
    private boolean onGround;


    // ======================================================
    // GAME FEEL SYSTEMS
    // ======================================================

    // Coyote time counter
    // Allows jumping a few frames after leaving a platform
    private int coyoteTimer = 0;

    // Maximum coyote time duration
    private final int COYOTE_TIME = 8;

    // Jump buffer counter
    // Stores jump input before landing
    private int jumpBufferTimer = 0;

    // Maximum jump buffer duration
    private final int JUMP_BUFFER = 8;


    // ======================================================
    // INPUT & PLAYER STATE
    // ======================================================

    // Keyboard input handler
    private InputHandler input;

    // Current player state
    // Used for animation and gameplay logic
    private PlayerState state = PlayerState.IDLE;


    // ======================================================
    // PLAYER VISUALS & SQUASH/STRETCH
    // ======================================================

    // Base radius of the circular player
    private float radius = 12f;

    // Current horizontal squash scale
    private float squashX = 1f;

    // Current vertical squash scale
    private float squashY = 1f;

    // Smoothed visual horizontal scale
    private float visualX = 1f;

    // Smoothed visual vertical scale
    private float visualY = 1f;


    // ======================================================
    // DASH SYSTEM
    // ======================================================

    // True while player is dashing
    private boolean isDashing = false;

    // Current dash timer
    private int dashTimer = 0;

    // Dash duration in frames
    private final int DASH_DURATION = 12;

    // Dash movement speed
    private final float DASH_SPEED = 11.5f;

    // Current dash cooldown timer
    private int dashCooldown = 0;

    // Delay before dash can be reused
    private final int DASH_COOLDOWN = 28;


    // ======================================================
    // WALL INTERACTION
    // ======================================================

    // True when touching wall on left side
    private boolean touchingLeftWall = false;

    // True when touching wall on right side
    private boolean touchingRightWall = false;

    // Reduced gravity while sliding on wall
    private final float WALL_SLIDE_GRAVITY = 0.28f;

    // Horizontal wall jump force
    private final float WALL_JUMP_FORCE_X = 6.5f;

    // Vertical wall jump force
    private final float WALL_JUMP_FORCE_Y = -11f;


    // ======================================================
    // COLLISION SETTINGS
    // ======================================================

    // Disables horizontal collisions temporarily
    // Useful for dash or special movement states
    public boolean ignoreHorizontalCollisions = false;


    // ======================================================
    // CONSTRUCTOR
    // ======================================================

    /**
     * Creates a new Hero player.
     *
     * @param x Initial X position
     * @param y Initial Y position
     * @param input Keyboard input handler
     */
    public Hero(float x, float y, InputHandler input) {

        // Calls Entity constructor
        super(x, y, 24, 24);

        this.input = input;
    }


    // ======================================================
    // MAIN UPDATE LOOP
    // ======================================================

    /**
     * Main update method executed every frame.
     *
     * Handles:
     * - input
     * - movement
     * - collisions
     * - animation
     * - player state
     */
    public void update(List<Platform> platforms) {

        // Read keyboard input
        handleInput();

        // Apply gravity and gameplay physics
        applyPhysics();

        // Move horizontally
        x += vx;

        // Update collision box
        updateHitbox();

        // Resolve horizontal collisions
        horizontalCollisions(platforms);

        // Move vertically
        y += vy;

        // Update collision box again
        updateHitbox();

        // Resolve vertical collisions
        verticalCollisions(platforms);

        // Detect if player is grounded
        checkGrounded(platforms);

        // Update player state
        updateState();

        // Update squash/stretch visuals
        updateShape();

        // Final hitbox update
        updateHitbox();
    }

    /**
     * Empty override from Entity base class.
     */
    @Override
    public void update() {}


    // ======================================================
    // INPUT HANDLING
    // ======================================================

    /**
     * Reads keyboard input and applies:
     * - movement
     * - jumping
     * - dash
     * - wall jump
     */
    private void handleInput() {

        // Movement inputs
        boolean left = input.isPressed(KeyEvent.VK_A)
                || input.isPressed(KeyEvent.VK_LEFT);

        boolean right = input.isPressed(KeyEvent.VK_D)
                || input.isPressed(KeyEvent.VK_RIGHT);

        // Jump inputs
        boolean jump = input.isPressed(KeyEvent.VK_SPACE)
                || input.isPressed(KeyEvent.VK_W)
                || input.isPressed(KeyEvent.VK_UP);

        // Dash input
        boolean dash = input.isPressed(KeyEvent.VK_SHIFT);

        // Use stronger acceleration on ground
        float accel = onGround
                ? GROUND_ACCELERATION
                : ACCELERATION;

        // Move left
        if (left) vx -= accel;

        // Move right
        if (right) vx += accel;

        // Apply friction when no movement key is pressed
        if (!left && !right) {
            vx *= onGround
                    ? FRICTION
                    : AIR_FRICTION;
        }

        // Remove tiny floating-point velocity values
        if (Math.abs(vx) < 0.05f) vx = 0;

        // Clamp horizontal speed
        vx = Math.max(-MAX_SPEED, Math.min(MAX_SPEED, vx));

        // Store jump input into jump buffer
        if (jump) jumpBufferTimer = JUMP_BUFFER;

        // Perform jump if:
        // - jump input exists
        // - coyote time still active
        if (jumpBufferTimer > 0 && coyoteTimer > 0) {

            vy = JUMP_FORCE;

            onGround = false;

            jumpBufferTimer = 0;
            coyoteTimer = 0;
        }

        // Start dash
        if (dash && dashCooldown <= 0 && !isDashing) {

            isDashing = true;

            dashTimer = DASH_DURATION;
            dashCooldown = DASH_COOLDOWN;
        }

        // Wall jump
        if (jump && (touchingLeftWall || touchingRightWall) && !onGround) {

            // Jump away from wall
            float dir = touchingLeftWall ? 1 : -1;

            vx = WALL_JUMP_FORCE_X * dir;
            vy = WALL_JUMP_FORCE_Y;

            touchingLeftWall = false;
            touchingRightWall = false;
        }
    }


    // ======================================================
    // PHYSICS
    // ======================================================

    /**
     * Applies:
     * - gravity
     * - dash physics
     * - wall slide gravity
     * - gameplay timers
     */
    private void applyPhysics() {

        // Dash physics
        if (isDashing) {

            // Dash direction based on current velocity
            float dir = (vx >= 0) ? 1f : -1f;

            vx = DASH_SPEED * dir;

            // Reduce vertical velocity during dash
            vy *= 0.6f;

        } else {

            // Apply reduced gravity while wall sliding
            vy += (touchingLeftWall || touchingRightWall)
                    ? WALL_SLIDE_GRAVITY
                    : GRAVITY;
        }

        // Clamp falling speed
        vy = Math.min(vy, MAX_FALL_SPEED);

        // Update gameplay timers
        if (coyoteTimer > 0) coyoteTimer--;
        if (jumpBufferTimer > 0) jumpBufferTimer--;
        if (dashCooldown > 0) dashCooldown--;
        if (dashTimer > 0) dashTimer--;

        // Stop dash when timer expires
        if (dashTimer <= 0) isDashing = false;
    }


    // ======================================================
    // HORIZONTAL COLLISIONS
    // ======================================================

    /**
     * Handles wall collisions.
     */
    private void horizontalCollisions(List<Platform> platforms) {

        // Ignore collisions if disabled
        if (ignoreHorizontalCollisions) return;

        // Reset wall states
        touchingLeftWall = false;
        touchingRightWall = false;

        // Slightly smaller collision box for smoother wall interaction
        Rectangle2D.Float future =
                new Rectangle2D.Float(x, y + 2, width, height - 4);

        for (Platform p : platforms) {

            if (future.intersects(p.getBounds())) {

                // Collision while moving right
                if (vx > 0) {

                    x = p.getX() - width;
                    touchingRightWall = true;
                }

                // Collision while moving left
                else if (vx < 0) {

                    x = p.getX() + p.getW();
                    touchingLeftWall = true;
                }

                // Stop horizontal movement
                vx = 0;

                updateHitbox();
            }
        }
    }


    // ======================================================
    // VERTICAL COLLISIONS
    // ======================================================

    /**
     * Handles floor and ceiling collisions.
     */
    private void verticalCollisions(List<Platform> platforms) {

        for (Platform p : platforms) {

            if (hitbox.intersects(p.getBounds())) {

                // Landing on platform
                if (vy > 0) {

                    y = p.getY() - height;

                    vy = 0;

                    onGround = true;

                    // Reset coyote time
                    coyoteTimer = COYOTE_TIME;
                }

                // Hitting ceiling
                if (vy < 0) {

                    y = p.getY() + p.getH();

                    vy = 0;
                }

                updateHitbox();
            }
        }
    }


    // ======================================================
    // GROUND DETECTION
    // ======================================================

    /**
     * Detects if player is standing on ground.
     */
    private void checkGrounded(List<Platform> platforms) {

        // Feet position
        float feetY = y + height + 1.5f;

        // Horizontal foot range
        float left = x + 1;
        float right = x + width - 1;

        boolean grounded = false;

        for (Platform p : platforms) {

            // Horizontal overlap check
            boolean xOverlap =
                    right > p.getX()
                            && left < p.getX() + p.getW();

            // Vertical proximity check
            boolean yClose =
                    feetY >= p.getY()
                            && feetY <= p.getY() + 3f;

            // Ground detected
            if (xOverlap && yClose && vy >= 0) {

                grounded = true;
                break;
            }
        }

        onGround = grounded;

        // Refresh coyote time while grounded
        if (onGround) coyoteTimer = COYOTE_TIME;
    }


    // ======================================================
    // SHAPE ANIMATION SYSTEM
    // ======================================================

    /**
     * Updates squash/stretch animation
     * based on movement state.
     */
    private void updateShape() {

        // Normalize speed
        float speed = Math.min(Math.abs(vx) / MAX_SPEED, 1f);

        // Default scales
        float targetX = 1f;
        float targetY = 1f;

        // Stretch while running
        targetX += speed * 0.4f;
        targetY -= speed * 0.25f;

        // Air deformation
        if (!onGround) {

            // Rising
            if (vy < 0) {

                targetX = 1.25f;
                targetY = 0.75f;
            }

            // Falling
            else {

                targetX = 0.85f;
                targetY = 1.25f;
            }
        }

        // Dash stretch
        if (isDashing) {

            targetX = 1.6f;
            targetY = 0.65f;
        }

        // Wall slide deformation
        if (touchingLeftWall || touchingRightWall) {

            targetX = 0.8f;
            targetY = 1.2f;
        }

        // Smooth interpolation
        squashX += (targetX - squashX) * 0.18f;
        squashY += (targetY - squashY) * 0.18f;

        // Additional visual smoothing
        visualX += (squashX - visualX) * 0.25f;
        visualY += (squashY - visualY) * 0.25f;
    }


    // ======================================================
    // RENDERING
    // ======================================================

    /**
     * Renders the hero on screen.
     */
    @Override
    public void render(Graphics2D g, int camX, int camY) {

        // Create isolated graphics context
        Graphics2D g2 = (Graphics2D) g.create();

        // Convert world position to screen position
        float drawX = x - camX;
        float drawY = y - camY;

        // Apply squash/stretch scaling
        float w = radius * 2f * visualX;
        float h = radius * 2f * visualY;

        // Center scaled shape
        drawX -= (w - radius * 2f) / 2f;
        drawY -= (h - radius * 2f);

        // Player color
        Color c;

        switch (state) {

            case RUN -> c = new Color(0, 200, 255);

            case JUMP -> c = Color.WHITE;

            case FALL -> c = new Color(255, 120, 180);

            case DASH -> c = new Color(255, 140, 40);

            case WALL_SLIDE -> c = new Color(180, 140, 255);

            default -> c = new Color(80, 220, 255);
        }

        // Draw shadow
        g2.setColor(new Color(0, 0, 0, 70));

        g2.fillOval(
                (int)(drawX + 2),
                (int)(drawY + h - 4),
                (int)w,
                (int)(h * 0.4f)
        );

        // Draw main body
        g2.setColor(c);

        g2.fillOval(
                (int)drawX,
                (int)drawY,
                (int)w,
                (int)h
        );

        // Draw light reflection
        g2.setColor(new Color(255, 255, 255, 70));

        g2.fillOval(
                (int)(drawX + w * 0.2f),
                (int)(drawY + h * 0.2f),
                (int)(w * 0.35f),
                (int)(h * 0.35f)
        );

        // Destroy temporary graphics context
        g2.dispose();
    }


    // ======================================================
    // PLAYER STATE MANAGEMENT
    // ======================================================

    /**
     * Updates player state based on movement.
     */
    private void updateState() {

        // Dash state has highest priority
        if (isDashing) {

            state = PlayerState.DASH;
            return;
        }

        // Air states
        if (!onGround) {

            state = (vy < 0)
                    ? PlayerState.JUMP
                    : PlayerState.FALL;
        }

        // Ground states
        else {

            state = (Math.abs(vx) > 0.5f)
                    ? PlayerState.RUN
                    : PlayerState.IDLE;
        }
    }


    // ======================================================
    // GETTERS
    // ======================================================

    /**
     * Returns current player state.
     */
    public PlayerState getState() {
        return state;
    }
}