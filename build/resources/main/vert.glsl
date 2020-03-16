#version 460 core
in vec3 pos;
in vec2 texCoord;
in vec3 normal;
uniform mat4 view;
uniform mat4 project;
uniform mat4 transform;
out vec2 outTexCoord;
out vec3 outNormal;
out vec3 fragPos;

void main()
{
    gl_Position = project * view * transform * vec4(pos, 1.0);
    outTexCoord = texCoord;
    outNormal = mat3(transpose(inverse(transform))) * normal;
    fragPos = vec3(transform * vec4(pos, 1.0));
}