package matyk.game;

import matyk.engine.Engine;
import matyk.engine.data.Color;
import matyk.engine.data.Material;
import matyk.engine.managers.NodeManager;
import matyk.engine.managers.SceneManager;
import matyk.engine.nodes.Light;
import matyk.engine.nodes.Spatial;
import org.joml.Vector3f;

public class Main {
    public static void main(String[] args) {
        Engine.start();

        Engine.runAfter(() ->
                SceneManager.loadFromJSON(
                        "{\n" +
                        "    \"CHILDREN\": [\n" +
                        "    {\n" +
                        "\t\"TYPE\": \"Spatial\",\n" +
                        "\t\"COMPONENTS\": {\n" +
                        "\t    \"CTransform\": {\n" +
                        "\t\t\"pos\": {\n" +
                        "\t\t    \"x\":0,\n" +
                        "\t\t    \"y\":-5,\n" +
                        "\t\t    \"z\":-20\n" +
                        "\t\t},\n" +
                        "\t\t\"scale\": {\n" +
                        "\t\t    \"x\":10,\n" +
                        "\t\t    \"y\":1,\n" +
                        "\t\t    \"z\":10\n" +
                        "\t\t}\n" +
                        "\t    }\n" +
                        "\t}\n" +
                        "    },\n" +
                        "    {\n" +
                        "\t\"TYPE\": \"Spatial\",\n" +
                        "\t\"COMPONENTS\": {\n" +
                        "\t    \"CTransform\": {\n" +
                        "\t\t\"pos\": {\n" +
                        "\t\t    \"x\":0,\n" +
                        "\t\t    \"y\":0,\n" +
                        "\t\t    \"z\":-20\n" +
                        "\t\t}\n" +
                        "\t    }\n" +
                        "\t}\n" +
                        "    },\n" +
                        "\n" +
                        "    {\n" +
                        "\t\"TYPE\" : \"Light\",\n" +
                        "\t\"COMPONENTS\" : {\n" +
                        "\t    \"CTransform\": {\n" +
                        "\t\t\"pos\": {\n" +
                        "\t\t    \"x\":0,\n" +
                        "\t\t    \"y\":5,\n" +
                        "\t\t    \"z\":-20\n" +
                        "\t\t}\n" +
                        "\t    },\n" +
                        "\t    \"CLight\": {\n" +
                        "\t\t\"r\": 0,\n" +
                        "\t\t\"g\": 1,\n" +
                        "\t\t\"b\": 0\n" +
                        "\t    }\n" +
                        "\t}\n" +
                        "    }\n" +
                        "    ]\n" +
                        "}"
                ));



        /*Engine.addNodes(
                new Spatial().getBuilder()
                        .initMesh("object.obj")
                        .initMat(new Material())
                        .setPos(new Vector3f(0, -5, -20))
                        .setScale(new Vector3f(10, 1, 10))
                        .defaultRot()
                        .build(),
                new Spatial().getBuilder()
                        .initMesh("object.obj")
                        .initMat(new Material())
                        .setPos(new Vector3f(0, 0, -20))
                        .defaultScale()
                        .defaultRot()
                        .build(),
                new Light().getBuilder()
                        .setColor(new Color(0,1,0))
                        .setPos(new Vector3f(0, 5, -20))
                        .build()
        );*/
    }
}
