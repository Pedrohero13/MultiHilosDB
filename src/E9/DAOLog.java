/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package E9;

/**
 *
 * @author pedro
 */
public class DAOLog {
    public boolean guardar(Log pojo ) {
      
        ConexionDB con= ConexionDB.getInstance();
        String sql="insert into registro(fecha,hora,ip_remitente,puerto,entrada,evento)"+ "values('"+ pojo.getFecha()
                +"','"+ pojo.getHora()+"','"+ pojo.getIpRemitente()+"','"+ pojo.getPuerto()+"','"
                + pojo.getEntrada()+"','"+ pojo.isEvento()+"')";
        boolean res=con.execute(sql);
        return res;
    }
}
