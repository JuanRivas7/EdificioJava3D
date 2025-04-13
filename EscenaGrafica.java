/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Principal;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import java.util.Enumeration;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import steve.crearEscenaGrafica;

/**
 *
 * @author jriva
 */
public class EscenaGrafica {

    BranchGroup objRaiz = new BranchGroup();
    Textura textura = new Textura();
    int paraTextura = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
    color c = new color();
    TransformGroup tgMundo;
    TransformGroup tgPiso;
    crearEscenaGrafica steve= new crearEscenaGrafica();

    public EscenaGrafica() {
        //------- MUNDO--------
        Box bxMundo = new Box(-8.0f, 10.0f, 10.0f, paraTextura, textura.crearTexturas("cielo_1.jpg"));//c.setColor(38, 238, 240)
        Transform3D t3dMundo = new Transform3D();
        t3dMundo.set(new Vector3d(0.0f, -0.15f, 0.0f));
        tgMundo = new TransformGroup(t3dMundo);
        //--------PISO-----------
        Box bxPiso = new Box(2.0f, 0.05f, 2.0f, paraTextura, c.setColor(131, 100, 62));//c.setColor(38, 238, 240)
        Transform3D t3dPiso = new Transform3D();
        t3dPiso.set(new Vector3d(0.0f, -0.5f, 0.0f));
        tgPiso = new TransformGroup(t3dPiso);
        EscalarTG(tgPiso, 5.0f);
        //-----------PAREDES Y VENTANAS------------
        // crearParedCompleta(-0.2f, -0.1f, 0.0f, 0.15f, 0.2f, 0.05f, 255, 167, 38, -10);
        //crearParedCompleta(-0.5f, -0.1f, 0.0f, 0.15f, 0.2f, 0.05f, 255, 167, 38, -10);
        // crearParedCompleta(-0.8f, -0.1f, 0.0f, 0.15f, 0.2f, 0.05f, 255, 167, 38, -10);
        //crearParedCompleta(-0.11f, -0.1f, 0.0f, 0.15f, 0.2f, 0.05f, 255, 167, 38, -10);
        ///||------------PERMISOS------------------||
        tgMundo.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgPiso.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        //||--------------MOVER EL MOUSE----------||
        EscalarTG(tgMundo, 5.0f);
        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(tgMundo);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        //crearVentana(0.0f, 0.0f, 0.0f, 0.2f, 0.2f, 0.05f, -180);

        KeyNavigatorBehavior keyNav = new KeyNavigatorBehavior(tgMundo);
        keyNav.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));

        //-----Behavior para animacion del personaje(que se mueva Pz-----
        Behavior animacionBehavior = new Behavior() {
            private WakeupOnElapsedTime wakeup;
            private Transform3D lastTransform = new Transform3D();

            public void initialize() {
                wakeup = new WakeupOnElapsedTime(50); // Verificar cada 50ms
                wakeupOn(wakeup);
                tgMundo.getTransform(lastTransform);
            }

            public void processStimulus(Enumeration criteria) {
                Transform3D currentTransform = new Transform3D();
                tgMundo.getTransform(currentTransform);

                // Comprobar si hubo movimiento
                if (!currentTransform.epsilonEquals(lastTransform, 0.01f)) {
                    steve.caminar(); // Activar animación
                }

                lastTransform.set(currentTransform);
                wakeupOn(wakeup);
            }
        };
        animacionBehavior.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
        //------------Termina pa que se mueva-------------
        //----------ADD CHILD----------
        tgMundo.addChild(bxMundo);
        tgPiso.addChild(bxPiso);
        //objRaiz.addChild(tgPiso);
        objRaiz.addChild(myMouseRotate);
        objRaiz.addChild(tgMundo);
        steve.Posicion(0, -0.08f, 0);
        tgMundo.addChild(tgPiso);
        objRaiz.addChild(steve.obtenerCuerpo());
        objRaiz.addChild(animacionBehavior);
        tgMundo.addChild(keyNav);

        configurarIluminacion(objRaiz);
    }

    private void crearParedCompleta(float x, float y, float z,
            float ancho, float alto, float profundidad,
            int R, int G, int B,
            float anguloRotacionGrados) {
        // Crear la caja (pared)
        Box bxPared = new Box(ancho, alto, profundidad, paraTextura, c.setColor(R, G, B));

        // Crear transformación compuesta (traslación + rotación)
        Transform3D t3dPared = new Transform3D();

        // 1. Primero aplicar rotación (en el origen)
        Transform3D rotacion = new Transform3D();
        rotacion.rotY(Math.toRadians(anguloRotacionGrados)); // Rotación alrededor del eje Y

        // 2. Luego aplicar traslación
        Transform3D traslacion = new Transform3D();
        traslacion.set(new Vector3d(x, y, z));

        // Combinar ambas transformaciones (primero rotación, luego traslación)
        t3dPared.mul(traslacion, rotacion);

        // Crear grupo de transformación y añadir a la escena
        TransformGroup tgPared = new TransformGroup(t3dPared);
        objRaiz.addChild(tgPared);
        tgPared.addChild(bxPared);
    }
// En tu clase EscenaGrafica

    private void crearVentana(float x, float y, float z, float ancho, float alto,
            float grosorMarco, float angulo) {
        Ventana ventana = new Ventana(x, y, z, ancho, alto, grosorMarco, angulo);
        objRaiz.addChild(ventana.getVentana());
    }

    public void EscalarTG(TransformGroup tg, float x) {
        Transform3D leer = new Transform3D();
        Transform3D mover = new Transform3D();
        tg.getTransform(leer);
        mover.setScale(x);
        leer.mul(mover);
        tg.setTransform(leer);
    }

    private void configurarIluminacion(BranchGroup escena) {
        // 1. Luz ambiental general (para iluminación base)
        AmbientLight luzAmbiental = new AmbientLight();
        luzAmbiental.setColor(new Color3f(0.3f, 0.3f, 0.3f));
        luzAmbiental.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100.0));
        escena.addChild(luzAmbiental);

        // 2. Luz direccional desde arriba (simula el sol)
        DirectionalLight luzDesdeArriba = new DirectionalLight();
        luzDesdeArriba.setColor(new Color3f(0.7f, 0.7f, 0.7f));
        luzDesdeArriba.setDirection(new Vector3f(0.0f, -1.0f, -0.3f)); // Apuntando hacia abajo
        luzDesdeArriba.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100.0));
        escena.addChild(luzDesdeArriba);

        // 3. Luz direccional de relleno (para reducir sombras muy oscuras)
        DirectionalLight luzRelleno = new DirectionalLight();
        luzRelleno.setColor(new Color3f(0.4f, 0.4f, 0.4f));
        luzRelleno.setDirection(new Vector3f(-0.5f, -0.5f, -0.5f));
        luzRelleno.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100.0));
        escena.addChild(luzRelleno);
    }
}
