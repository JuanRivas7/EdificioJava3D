package Edificio;
import Edificio.crearEscenaGrafica_1;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

public class crearEscenaGrafica {
    public BranchGroup objRaiz = new BranchGroup();
    public TransformGroup tgPiso;  // Hacer público para moverlo desde edificio.java
    private crearEscenaGrafica_1 escenaSteve;

    public crearEscenaGrafica() {
        // Crear piso (con capacidad para ser movido)
        tgPiso = new TransformGroup();
        tgPiso.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);  // ¡IMPORTANTE!
        
        Box bxPiso = new Box(100.0f, 0.20f, 100.0f, Primitive.GENERATE_NORMALS, new Appearance());
        tgPiso.addChild(bxPiso);

        // Crear personaje (posición fija en el centro)
        escenaSteve = new crearEscenaGrafica_1();
        Transform3D t3dPersonaje = new Transform3D();
        t3dPersonaje.set(new Vector3d(0.0f, 0.0f, 0.0f));  // Personaje en el origen
        TransformGroup tgPersonaje = new TransformGroup(t3dPersonaje);
        tgPersonaje.addChild(escenaSteve.objRaiz);

        // Construir escena
        objRaiz.addChild(tgPiso);
        objRaiz.addChild(tgPersonaje);
    }
}