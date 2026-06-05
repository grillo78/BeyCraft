#version 150

in vec3 Position;
in vec3 Normal;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec2 LightmapUV;

out vec2 texCoord0;
out vec2 texCoord2;
out vec4 vertexColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    texCoord0 = UV0;
    texCoord2 = LightmapUV;

    // Lighting lambertiano en view space
    vec3 normalView = normalize(mat3(ModelViewMat) * Normal);
    // Luz principal desde arriba-frente, luz de relleno desde abajo
    float diffuse = max(dot(normalView, normalize(vec3(0.2, 1.0, 0.5))), 0.0) * 0.6 + 0.4;
    vertexColor = vec4(diffuse, diffuse, diffuse, 1.0);
}
