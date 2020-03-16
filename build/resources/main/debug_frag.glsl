#version 330

in vec3 texCoords;

out vec4 color;

uniform samplerCube cubemap;

void main() {
    color = texture(cubemap, texCoords);
}
