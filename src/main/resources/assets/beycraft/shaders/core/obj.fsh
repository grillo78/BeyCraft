#version 150

in vec2 texCoord0;
in vec2 texCoord2;
in vec4 vertexColor;

uniform sampler2D Sampler0;  // textura diffuse (slot 0)
uniform sampler2D Sampler2;  // lightmap de Minecraft (slot 2)
uniform vec4 ColorModulator;
uniform float Alpha;

out vec4 fragColor;

void main() {
    vec4 diffuse = texture(Sampler0, texCoord0);

    diffuse.a = min(diffuse.a, Alpha);

    // Alphatest: descartar píxeles completamente transparentes
    if (diffuse.a < 0.1) discard;

    // Lightmap: combina luz de bloque y luz de cielo
    vec4 light = texture(Sampler2, texCoord2);

    fragColor = diffuse * light * vertexColor * ColorModulator ;
}
