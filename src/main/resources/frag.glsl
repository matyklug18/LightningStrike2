#version 460 core

#define MAX_POINT_LIGHTS maxpointlights

in vec2 outTexCoord;
in vec3 outNormal;
in vec3 fragPos;

uniform sampler2D texture_sampler;

uniform vec3 viewPos;

out vec4 outColor;

const float specularStrength = 0.5;
const float ambientStrength = 0.1;
const float materialShininess = 32;

//Light Structs
#if MAX_POINT_LIGHTS != 0
#define __POINT_LIGHTS__

struct PointLight {
    vec3 pos;
    vec3 color;
    vec3 attenuation;
};

uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform int pointLightCount;

vec3 calcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir) {
    vec3 lightDir = normalize(light.pos - fragPos);

    vec3 diffuse = max(dot(normal, lightDir), 0.0) * light.color;

    vec3 reflectDir = reflect(-lightDir, normal);
    vec3 halfwayDir = normalize(lightDir + viewDir);
    float spec = pow(max(dot(normal, halfwayDir), 0.0), materialShininess);
    vec3 specular = specularStrength * spec * light.color;

    //vec4 texColor = texture(texture_sampler, outTexCoord);
    vec3 texColor = vec3(0,1,0);
    return texColor * (vec3(ambientStrength) + diffuse + specular);
}
#endif

void main()
{
    vec3 outRGBA = vec3(0.0);

    vec3 norm = normalize(outNormal);
    vec3 viewDir = normalize(viewPos - fragPos);

    #ifdef __POINT_LIGHTS__
    for(int i = 0; i < pointLightCount; i++) {
        outRGBA += calcPointLight(pointLights[i], norm, fragPos, viewDir);
    }
    #endif

    outRGBA *= 0.4;

    outColor = vec4(outRGBA, 1.);
}