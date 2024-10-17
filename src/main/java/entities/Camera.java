package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

@Getter
@Setter
@Log4j2
public class Camera {
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private Vector3f worldUp;

    private float pitch;
    private float yaw;
    private float roll;

    private float lastX;
    private float lastY;
    private boolean firstMouse;

    public Camera(Vector3f position, Vector3f up, float yaw, float pitch) {
        log.info("Caméra initialisé");
        this.position = position;
        this.worldUp = up;
        this.yaw = yaw;
        this.pitch = pitch;
        this.front = new Vector3f();
        this.right = new Vector3f();
        this.up = new Vector3f();
        updateCameraVectors();
        this.firstMouse = true;
    }

    public void processKeyboard(float deltaTime) {
        float cameraSpeed = 2.5f * deltaTime;
        if (GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            log.info("Z key pressed");
            position.add(front.mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            log.info("S key pressed");
            position.sub(front.mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            log.info("Q key pressed");
            position.sub(right.mul(cameraSpeed));
        }
        if (GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            log.info("D key pressed");
            position.add(right.mul(cameraSpeed));
        }
    }

    public void processMouseMovement(float xoffset, float yoffset, boolean constrainPitch) {
        xoffset *= 0.1f;
        yoffset *= 0.1f;

        yaw += xoffset;
        pitch += yoffset;

        if (constrainPitch) {
            if (pitch > 89.0f) {
                pitch = 89.0f;
            }
            if (pitch < -89.0f) {
                pitch = -89.0f;
            }
        }

        updateCameraVectors();
    }

    private void updateCameraVectors() {
        log.info("Updating camera vectors");
        Vector3f front = new Vector3f();
        front.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        this.front = front.normalize();

        right = worldUp.cross(this.front).normalize();
        up = this.front.cross(right).normalize();
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, position.add(front), up);
    }
}
