/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.pdsw.samples.persistence.jdbcimpl;

import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author hcadavid
 */
public class JDBCDaoPaciente implements DaoPaciente {

    Connection con;

  
    public JDBCDaoPaciente(Connection con) {
        this.con = con;
    }
        

    @Override
    public Paciente load(int idpaciente, String tipoid) throws PersistenceException {
        PreparedStatement ps;
        try {
            String query="SELECT * from PACIENTES WHERE id=? AND tipo_id=?";
            ps=con.prepareStatement(query);
            ps.setInt(1, idpaciente);
            ps.setString(2, tipoid);
            ResultSet rs=ps.executeQuery();
            if(!rs.next())throw new PersistenceException(PersistenceException.PACIENTE_NO_EXISTENTE);
            Paciente ans=new Paciente(idpaciente, tipoid, rs.getString(3), rs.getDate(4));
            String queryConsultas="SELECT idCONSULTAS, fecha_y_hora, resumen FROM CONSULTAS WHERE PACIENTES_id=? AND PACIENTES_tipo_id=?";
            ps=con.prepareStatement(queryConsultas);
            ps.setInt(1,idpaciente);
            ps.setString(2, tipoid);
            rs=ps.executeQuery();
            Set<Consulta> consultas=new HashSet<>();
            while(rs.next()){
                Consulta tmp=new Consulta(rs.getDate(2),rs.getString(3));
                tmp.setId(rs.getInt(1));
                consultas.add(tmp);  
            }
            ans.setConsultas(consultas);
            return ans;
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while loading "+idpaciente,ex);
        }
    }

    @Override
    public void save(Paciente p) throws PersistenceException {
        PreparedStatement ps;
        try {
            String query="SELECT * from PACIENTES WHERE id=? AND tipo_id=?";
            ps=con.prepareStatement(query);
            ps.setInt(1, p.getId());
            ps.setString(2, p.getTipo_id());
            ResultSet rs=ps.executeQuery();
            if(!rs.next()){
                String insertarPaciente="INSERT INTO PACIENTES (id,tipo_id,nombre,fecha_nacimiento) VALUES (?,?,?,?)";
                ps=con.prepareStatement(insertarPaciente);
                ps.setInt(1,p.getId());
                ps.setString(2, p.getTipo_id());
                ps.setString(3, p.getNombre());
                ps.setDate(4, p.getFechaNacimiento());
                ps.execute();
            }else throw new PersistenceException(PersistenceException.PACIENTE_EXISTENTE); 
            String insertConsultas="INSERT INTO CONSULTAS (fecha_y_hora,resumen,PACIENTES_id,PACIENTES_tipo_id) "
                    + "VALUES (?,?,?,?)";
            ps=con.prepareStatement(insertConsultas);
            int n=1;
            for(Consulta c :  p.getConsultas()){
                ps.setDate(1,c.getFechayHora());
                ps.setString(2,c.getResumen());
                ps.setInt(3, p.getId());
                ps.setString(4, p.getTipo_id());  
                ps.execute();
            }
        } catch (SQLException e) {
            throw new PersistenceException("An error ocurred while saving a product.",e);
        }

    }

    @Override
    public void update(Paciente p) throws PersistenceException {
        PreparedStatement ps;
        try {
            Set<Consulta> consultasPacienteUno=p.getConsultas();           
            String updated="UPDATE PACIENTES SET nombre=?,fecha_nacimiento=? WHERE id=? AND tipo_id=?";
            ps=con.prepareStatement(updated);
            ps.setString(1, p.getNombre());
            ps.setDate(2, p.getFechaNacimiento());
            ps.setInt(3, p.getId());
            ps.setString(4, p.getTipo_id());
            ps.execute();
            String delete="DELETE FROM CONSULTAS WHERE PACIENTES_id=? AND PACIENTES_tipo_id=?";
            ps=con.prepareStatement(delete);
            ps.setInt(1, p.getId());
            ps.setString(2, p.getTipo_id());
            ps.execute();
            String insert="INSERT INTO CONSULTAS (fecha_y_hora,resumen,PACIENTES_id,PACIENTES_tipo_id) "
                    + "VALUES (?,?,?,?)";
            ps=con.prepareStatement(insert);
            ps.setInt(3, p.getId());
            ps.setString(4, p.getTipo_id());
            for(Consulta c:consultasPacienteUno){
                ps.setDate(1, c.getFechayHora());
                ps.setString(2, c.getResumen());
                ps.execute();
            }
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while updating a product.",ex);
        }
    }

    @Override
    public List<Paciente> load() throws PersistenceException {
        PreparedStatement ps;
        ArrayList<Paciente> pacientes=new ArrayList<Paciente>();
        String query="SELECT id, tipo_id FROM PACIENTES";
        try {
            ps=con.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                pacientes.add(load(rs.getInt(1),rs.getString(2)));
            }
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while list all.",ex);  
        }
        return pacientes;
    }
    
}
