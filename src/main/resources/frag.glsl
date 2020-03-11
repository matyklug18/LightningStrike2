#version 460 core

#define MAX_POINT_LIGHTS maxpointlights

in vec2 outTexCoord;
in vec3 outNormal;
in vec3 fragPos;

uniform sampler2D diffuseTexture;
uniform samplerCube depthMap;
uniform float far_plane;
uniform vec2 iRes;

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


float shadowCalculation(vec3 fragPos, vec3 pos)
{
    // get vector between fragment position and light position
    vec3 fragToLight = fragPos - pos;
    // use the light to fragment vector to sample from the depth map
    float closestDepth = texture(depthMap, fragToLight).r;
    // it is currently in linear range between [0,1]. Re-transform back to original value
    closestDepth *= far_plane;
    // now get current linear depth as the length between the fragment and light position
    float currentDepth = length(fragToLight);
    // now test for shadows
    float bias = 0.05;
    float shadow = currentDepth -  bias > closestDepth ? 1.0 : 0.0;

    return shadow;
}


vec3 calcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir) {
    vec3 lightDir = normalize(light.pos - fragPos);

    vec3 diffuse = max(dot(normal, lightDir), 0.0) * light.color;

    vec3 reflectDir = reflect(-lightDir, normal);
    vec3 halfwayDir = normalize(lightDir + viewDir);
    float spec = pow(max(dot(normal, halfwayDir), 0.0), materialShininess);
    vec3 specular = specularStrength * spec * light.color;

    //vec4 texColor = texture(texture_sampler, outTexCoord);
    vec3 texColor = vec3(0,1,0);

    float shadow = shadowCalculation(fragPos, light.pos);

    return texColor * (vec3(ambientStrength + (1.0 - shadow)) + diffuse + specular);
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