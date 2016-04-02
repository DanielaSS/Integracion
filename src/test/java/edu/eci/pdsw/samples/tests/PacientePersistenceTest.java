
/*
 * Copyright (C) 2016 hcadavid
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
package edu.eci.pdsw.samples.tests;

import java.sql.PreparedStatement;
import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author hcadavid
 */
public class PacientePersistenceTest {
    //1         DAOPaciente.save()      Paciente nuevo que se registra con mas de una consulta
    
    @After
    public void borrar() throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "", "");
        Statement stmt = conn.createStatement();
        stmt.execute("delete from CONSULTAS");
        stmt.execute("delete from PACIENTES");
         
    }
    @Test
    public void classEquivRegistroPacienteMasDeUnaConsulta(){
        System.out.println("Prueba 1 Paciente mas de una consulta");
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();
        try {
            properties.load(input);
        } catch (IOException ex) {
            fail("No se cargaron las propiedades");
        }
            
        DaoFactory daof=DaoFactory.getInstance(properties);
        try {
            daof.beginSession();
            
            String query="select pac.nombre, pac.fecha_nacimiento, con.idCONSULTAS, con.fecha_y_hora, con.resumen "
                    + "from PACIENTES as pac inner join CONSULTAS as con on con.PACIENTES_id=pac.id "
                    + "and con.PACIENTES_tipo_id=pac.tipo_id where pac.id=? and pac.tipo_id=?";
            
            //IMPLEMENTACION DE LAS PRUEBAS
            DaoPaciente a=daof.getDaoPaciente();
            Paciente paciente=new Paciente(500,"CC","Martin no subir",Date.valueOf("2005-08-15"));
            Consulta unaConsulta=new Consulta(Date.valueOf("2016-03-03"),"Martin lo subio");
            Consulta otraConsulta=new Consulta(Date.valueOf("2015-10-10"),"Ahora carlos lo subio");
            Set<Consulta> setConsultas=new HashSet<Consulta>();
            setConsultas.add(unaConsulta);
            setConsultas.add(otraConsulta);
            paciente.setConsultas(setConsultas);
            a.save(paciente);
            daof.commitTransaction();
            Paciente p=a.load(paciente.getId(), paciente.getTipo_id());
            daof.endSession();
            System.out.println("Prueba mas de una consulta");
            assertTrue(paciente.equals(p));
            
        } catch (PersistenceException ex) {
            fail("Lanzo excepcion "+ ex.getMessage());
        }         
    }
    //2         DAOPaciente.save()      Paciente nuevo que se registra sin consultas
    @Test
    public void classEquivRegistroPacienteSinConsultas(){
        System.out.println("Prueba 2 Paciente sin consultas");
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();
        try {
            properties.load(input);
        } catch (IOException ex) {
            fail("No se cargaron las propiedades");
        }
            
        DaoFactory daof=DaoFactory.getInstance(properties);
        try {

            String query="select pac.nombre, pac.fecha_nacimiento, con.idCONSULTAS, con.fecha_y_hora, con.resumen "
                    + "from PACIENTES as pac left join CONSULTAS as con on con.PACIENTES_id=pac.id "
                    + "and con.PACIENTES_tipo_id=pac.tipo_id where pac.id=? and pac.tipo_id=? OR con.idCONSULTAS=NULL";
            daof.beginSession();
            
            //IMPLEMENTACION DE LAS PRUEBAS
            DaoPaciente a=daof.getDaoPaciente();
            Paciente paciente=new Paciente(100,"CE","Casvad",Date.valueOf("2000-08-15"));

            a.save(paciente);
            
            daof.commitTransaction(); 
            Paciente p=a.load(paciente.getId(), paciente.getTipo_id());
            daof.endSession();  
            assertTrue(paciente.equals(p));
            
        } catch (PersistenceException ex) {
            fail("Lanzo excepcion "+ex.getMessage());
        }   
    }
    //3 	DAOPaciente.save() 	Paciente nuevo que se registra con sólo una consulta 	
   @Test
    public void classEquivSaveNuevoPacienteConUnaConsulta(){
        System.out.println("Prueba 3, paciente nuevo que se registra con solo una consulta");
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();       
        try {
            properties.load(input);
        } catch (IOException ex) {
            fail("No se cargaron las propiedades");
        }
        DaoFactory daof=DaoFactory.getInstance(properties);
        
        try{
            daof.beginSession();
            
            DaoPaciente persistenciaPaciente=daof.getDaoPaciente();
            Paciente unPaciente=new Paciente(3,"CC","Isabel Marin",Date.valueOf("1990-08-15"));
            Consulta unaConsulta=new Consulta(Date.valueOf("2016-03-03"),"Golpe en la cabeza por desmayo");
            Set<Consulta> setConsultas=new HashSet<Consulta>();
            setConsultas.add(unaConsulta);
            unPaciente.setConsultas(setConsultas);
            
            persistenciaPaciente.save(unPaciente);
            //System.out.println("Paso");
            daof.commitTransaction();
            
            Paciente p=persistenciaPaciente.load(unPaciente.getId(), unPaciente.getTipo_id());
            daof.endSession();          
            assertTrue(unPaciente.equals(p));
           
            
        } catch (PersistenceException ex) {
            fail("Lanzo Excepcion"+ex.getMessage());
  
        }  
    }
    //4 	DAOPaciente.save() 	Paciente nuevo YA existente que se registra con más de una consulta
    @Test
    public void classEquivPacienteRepetido() throws PersistenceException{
        System.out.println("Prueba 4, paciente nuevo YA existente que se registra con más de una consulta");
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();       
        try {
            properties.load(input);
        } catch (IOException ex) {
            fail("No se cargaron las propiedades");
        }
        DaoFactory daof=DaoFactory.getInstance(properties);
        try{
            daof.beginSession();
            DaoPaciente persistenciaPaciente=daof.getDaoPaciente();
            Paciente unPaciente=new Paciente(400,"CC","Maria alejandra Gallego",Date.valueOf("1999-01-30"));
            Consulta unaConsulta=new Consulta(Date.valueOf("2016-01-26"),"Alergia a picadura de abeja");
            Consulta dosConsulta=new Consulta(Date.valueOf("2016-01-27"),"Revision picadura abeja");
            Consulta tresConsulta=new Consulta(Date.valueOf("2016-02-21"),"Revision efecto de los antinflamatorios");
            Set<Consulta> setConsultas=new HashSet<Consulta>();
            setConsultas.add(unaConsulta);
            setConsultas.add(dosConsulta);
            setConsultas.add(tresConsulta);
            unPaciente.setConsultas(setConsultas);
            persistenciaPaciente.save(unPaciente);
            daof.commitTransaction(); 
            persistenciaPaciente.save(unPaciente);  
            daof.commitTransaction(); 
            fail("No lanzo excepcion");
        } catch (PersistenceException ex) {
            assertEquals(ex.getMessage(), PersistenceException.PACIENTE_EXISTENTE);
        } finally{
            daof.endSession();
        }
    }
    @Test
    public void pruebaAgregarConsulta() throws PersistenceException{
        InputStream input = null;
        input = ClassLoader.getSystemResourceAsStream("applicationconfig_test.properties");
        Properties properties=new Properties();       
        try {
            properties.load(input);
        } catch (IOException ex) {
            fail("No se cargaron las propiedades");
        }
        DaoFactory daof=DaoFactory.getInstance(properties);
        try{
            daof.beginSession();
            DaoPaciente persistenciaPaciente=daof.getDaoPaciente();
            Paciente unPaciente=new Paciente(400,"CC","Maria alejandra Gallego",Date.valueOf("1999-01-30"));
            Consulta unaConsulta=new Consulta(Date.valueOf("2016-01-26"),"Alergia a picadura de abeja");
            Consulta dosConsulta=new Consulta(Date.valueOf("2016-01-27"),"Revision picadura abeja");
            Consulta tresConsulta=new Consulta(Date.valueOf("2016-02-21"),"Revision efecto de los antinflamatorios");
            Set<Consulta> setConsultas=new HashSet<Consulta>();
            persistenciaPaciente.save(unPaciente);
            setConsultas.add(unaConsulta);
            setConsultas.add(dosConsulta);
            setConsultas.add(tresConsulta);
            unPaciente.setConsultas(setConsultas);
            System.out.println("Paso hasta aqui, entra a update");
            persistenciaPaciente.update(unPaciente);
            System.out.println("Paso de update");
            daof.commitTransaction(); 
            persistenciaPaciente.save(unPaciente);  
            daof.commitTransaction(); 
        } catch (PersistenceException ex) {
            System.out.println(ex);
        } finally{
            daof.endSession();
        }    
    }
}
