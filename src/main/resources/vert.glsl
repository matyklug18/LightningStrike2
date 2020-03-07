#version 460

in vec3 pos;

uniform mat4 project;
uniform mat4 view;
uniform mat4 transform;

void main() {
    gl_Position = project * view * transform * vec4(pos, 1.0);
}
