package com.example.benchmarkingscs.model;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.benchmarkingscs.controller.gputools.Model;
import com.example.benchmarkingscs.controller.gputools.Shader;
import com.example.benchmarkingscs.view.BenchmarkView;

import java.io.InputStream;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class GPUModel implements Runnable {
    private static final int MAX_LIGHTS = 7;

    private final Context context;

    private Shader shader;
    private Model bunnyModel;

    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private float ratio;
    private float rotationAngle = 0f;
    private volatile boolean isBenchmarkRunning = false;
    private long startTime;
    private long frameCount = 0;

    private final float[][] lightPositions = new float[MAX_LIGHTS][3];
    private final float[][] lightColors = new float[MAX_LIGHTS][3];

    private EGLContext eglContext;
    private EGLDisplay eglDisplay;
    private EGLSurface eglSurface;

    public GPUModel(Context context, BenchmarkView view) {
        this.context = context;
    }

    @Override
    public void run() {
        initEGL();
        initOpenGL();

        Log.d("Benchmark", "Benchmark started...");
        startBenchmark();

        while (isBenchmarkRunning) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - startTime >= 30_000) { // Stop after 30 seconds
                isBenchmarkRunning = false;
                break;
            }

            drawFrame();
            frameCount++;
        }

        Log.d("Benchmark", "Benchmark completed! Total Frames: " + frameCount);
        cleanupEGL();
    }

    private void initEGL() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] version = new int[2];
        egl.eglInitialize(eglDisplay, version);

        int[] configSpec = {
                EGL10.EGL_RENDERABLE_TYPE, 4,
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 16,
                EGL10.EGL_NONE
        };

        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        egl.eglChooseConfig(eglDisplay, configSpec, configs, 1, numConfigs);
        EGLConfig eglConfig = configs[0];

        eglContext = egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, new int[]{
                0x3098, 2, EGL10.EGL_NONE
        });

        eglSurface = egl.eglCreatePbufferSurface(eglDisplay, eglConfig, new int[]{
                EGL10.EGL_WIDTH, 1, EGL10.EGL_HEIGHT, 1, EGL10.EGL_NONE
        });

        egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
    }

    private void initOpenGL() {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        try {
            // Load shaders
            String vertexShaderCode = loadShaderCode(context, "shaders/vertex_shader.glsl");
            String fragmentShaderCode = loadShaderCode(context, "shaders/fragment_shader.glsl");
            shader = new Shader(vertexShaderCode, fragmentShaderCode);

            // Load the model (bunny.obj is the new model)
            bunnyModel = new Model(context, "shaders/bunny.obj");

            Log.d("OpenGL", "Shaders and model loaded successfully.");
        } catch (Exception e) {
            Log.e("OpenGL", "Failed to load shaders or model: " + e.getMessage());
        }

        initializeLighting();
    }

    private void drawFrame() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        shader.use();

        // Set view and projection matrices
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -20, 0, 0, 0, 0, 1, 0);
        Matrix.perspectiveM(projectionMatrix, 0, 45, ratio, 0.1f, 100f);

        shader.setUniformMatrix("uViewMatrix", viewMatrix);
        shader.setUniformMatrix("uProjectionMatrix", projectionMatrix);

        // Set lighting uniforms
        for (int i = 0; i < MAX_LIGHTS; i++) {
            shader.setUniform3f("uLightPos[" + i + "]", lightPositions[i][0], lightPositions[i][1], lightPositions[i][2]);
            shader.setUniform3f("uLightColor[" + i + "]", lightColors[i][0], lightColors[i][1], lightColors[i][2]);
        }

        shader.setUniform3f("uViewPos", 0f, 0f, -20f);

        // Update and set model matrix
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, rotationAngle, 0, 1, 0);
        shader.setUniformMatrix("uModelMatrix", modelMatrix);

        bunnyModel.draw(shader);

        rotationAngle += 2f;

        EGL10 egl = (EGL10) EGLContext.getEGL();
        egl.eglSwapBuffers(eglDisplay, eglSurface);
    }

    private void cleanupEGL() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(eglDisplay, eglSurface);
        egl.eglDestroyContext(eglDisplay, eglContext);
        egl.eglTerminate(eglDisplay);
    }

    public void startBenchmark() {
        isBenchmarkRunning = true;
        startTime = System.currentTimeMillis();
        frameCount = 0;
        rotationAngle = 0f;
    }

    private void initializeLighting() {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            lightPositions[i] = new float[]{(i % 2 == 0 ? 10f : -10f), 10f, (i < 4 ? 10f : -10f)};
            lightColors[i] = new float[]{1f, (i % 3 == 0 ? 0.5f : 1f), (i % 2 == 0 ? 0f : 1f)};
        }
    }

    public long getFrameCount() {
        return frameCount;
    }

    private String loadShaderCode(Context context, String fileName) {
        try (InputStream inputStream = context.getAssets().open(fileName)) {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            return new String(buffer, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load shader code: " + fileName, e);
        }
    }
}
