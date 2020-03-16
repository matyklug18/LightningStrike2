#version 330

in vec3 position;

out vec3 texCoords;

uniform mat4 project;
uniform mat4 view;
uniform mat4 transform;

void main() {
    gl_Position = project * view * transform * vec4(position, 1.0);
    texCoords = position;
}
