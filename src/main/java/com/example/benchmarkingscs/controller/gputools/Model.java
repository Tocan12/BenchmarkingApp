package com.example.benchmarkingscs.controller.gputools;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private List<float[]> positions = new ArrayList<>();
    private List<float[]> normals = new ArrayList<>();
    private List<float[]> texCoords = new ArrayList<>();
    private List<int[]> faces = new ArrayList<>(); // Stores indices for positions, normals, and texCoords

    // Constructor for raw resource loading
    public Model(Context context, int resourceId) {
        loadModel(context.getResources().openRawResource(resourceId));
    }

    // New constructor for asset loading
    public Model(Context context, String assetPath) {
        try {
            InputStream inputStream = context.getAssets().open(assetPath);
            loadModel(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load model from assets: " + e.getMessage());
        }
    }

    // Unified method to load a model
    private void loadModel(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length < 2) continue;

                switch (parts[0]) {
                    case "v": // Vertex position
                        positions.add(new float[]{
                                Float.parseFloat(parts[1]),
                                Float.parseFloat(parts[2]),
                                Float.parseFloat(parts[3])
                        });
                        break;
                    case "vn": // Vertex normal
                        normals.add(new float[]{
                                Float.parseFloat(parts[1]),
                                Float.parseFloat(parts[2]),
                                Float.parseFloat(parts[3])
                        });
                        break;
                    case "vt": // Texture coordinate
                        texCoords.add(new float[]{
                                Float.parseFloat(parts[1]),
                                Float.parseFloat(parts[2])
                        });
                        break;
                    case "f": // Face (vertex indices)
                        for (int i = 1; i < parts.length; i++) {
                            String[] indices = parts[i].split("/");
                            int positionIndex = Integer.parseInt(indices[0]) - 1;
                            int texCoordIndex = indices.length > 1 && !indices[1].isEmpty() ? Integer.parseInt(indices[1]) - 1 : -1;
                            int normalIndex = indices.length > 2 ? Integer.parseInt(indices[2]) - 1 : -1;

                            faces.add(new int[]{positionIndex, texCoordIndex, normalIndex});
                        }
                        break;
                }
            }

            reader.close();
            System.out.println("Model loaded successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load model: " + e.getMessage());
        }
    }

    public List<float[]> getPositions() {
        return positions;
    }

    public List<float[]> getNormals() {
        return normals;
    }

    public List<float[]> getTexCoords() {
        return texCoords;
    }

    public List<int[]> getFaces() {
        return faces;
    }

    public void draw(Shader shader) {
        Vertex vertexBuffer = new Vertex(this);
        vertexBuffer.draw(shader);
    }
}
