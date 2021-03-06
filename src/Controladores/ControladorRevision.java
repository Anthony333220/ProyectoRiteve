package Controladores;

import Archivo.Configuracion;
import Archivo.PruebaConexion;
import Clases.Cita;
import Clases.Revision;
import Clases.Secretaria;
import Clases.Tecnico;
import Clases.Vehiculo;
import Vistas.FrmMenuPrincipal;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

/**
 *
 * @author Anthony
 */
public class ControladorRevision {

    private Statement sentencias;
    private ResultSet datos;
    private Revision revision;
    private Vehiculo vehiculo;
    private ControladorVehiculos ctrVehiculo;
    private Statement sentencias2;

    private PruebaConexion conn;

    public ControladorRevision(Statement sentencias, ResultSet datos, Revision revision, Statement sentencias2, PruebaConexion conn) {
        this.sentencias = sentencias;
        this.datos = datos;
        this.revision = revision;
        this.sentencias2 = sentencias2;
        this.conn = conn;
    }

    public ControladorRevision(PruebaConexion conn) {
        this.conn = conn;
        this.sentencias = conn.getSentencias();
        this.datos = conn.getDatos();

    }

    public ControladorRevision() {

        conn = FrmMenuPrincipal.getConexion();  //probar com conexion o conectar
        try {
            this.sentencias2 = FrmMenuPrincipal.getConexion().getConexion().createStatement();
        } catch (SQLException ex) {

            System.out.println("No se lo gro conectar sentencia  con la conexion");
        }

        this.sentencias = conn.getSentencias();
        this.datos = conn.getDatos();
    }

    public boolean crearRevision(Revision revision, Vehiculo vehiculo,Tecnico tecnico) {

        System.out.println("antes de crear la cita el try");
        try {
            
            this.vehiculo = ctrVehiculo.buscarVehiculo(vehiculo);
            
            if (!this.vehiculo.equals(null)) {
                SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");

                sentencias.execute("insert into revision values(null,'" + revision.getVehiculo().getPlaca() + "','" + 
                        fecha.format(revision.getFecha()) + "','" + revision.getHora() + "','" + revision.getVehiculo().getCedula() + "','"
                        + revision.getTipoRevision() + "','" + revision.getObservacione() + "','" + revision.isEstado() + "')");
                System.out.println("Se agrego la revision exitosamente");
            }          
            return true;
        } catch (SQLException ex) {

            System.out.println("No se logro crear cita devido a" + ex.getMessage());
            System.out.println(ex);
        }
        return false;
    }
    
    public Revision buscarRevision(Date fechaRevision){
        try {
            SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd");
            this.datos = this.sentencias.executeQuery("select * from revision where fechaRevision=" + fecha.format(fechaRevision));

            if (datos.next()) {

                Revision revision = new Revision(fechaRevision);                
                
                revision.setVehiculo(new Vehiculo(datos.getInt(1)));
                revision.setFecha(datos.getDate(2));                
                revision.setHora(datos.getTime(3));
                revision.setTecnico(new Tecnico(datos.getInt(4)));
                revision.setTipoRevision(datos.getString(5));
                revision.setObservacione(datos.getString(6));
//                revision.setEstado(datos.geString(7));

                return revision;
            }

        } catch (SQLException ex) {
            System.out.println("no se pudo hayar  un usuario" + ex);
        }
        return null;
    }
    
    

}
