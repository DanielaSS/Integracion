/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.services;

import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class ServiciosPacientesPersistencia extends ServiciosPacientes{
    private final DaoFactory dao;
    private DaoPaciente persistencia;
    
    public ServiciosPacientesPersistencia(){
        InputStream input = null;
        
        
        try {
            input = ServiciosPacientesPersistencia.class.getClassLoader().getResource("applicationconfig.properties").openStream();
        } catch (IOException ex) {
            Logger.getLogger(ServiciosPacientesPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        Properties properties=new Properties();
        try {
            properties.load(input);
        } catch (IOException ex) {
            Logger.getLogger(ServiciosPacientesPersistencia.class.getName()).log(Level.INFO, null, ex);
        }
        
        dao=DaoFactory.getInstance(properties);
    }
        
    @Override
    public List<Paciente> getPacientes() throws ExcepcionServiciosPacientes {
        List<Paciente> pacientes=new ArrayList<>();
        try {
            dao.beginSession(); 
            persistencia=dao.getDaoPaciente();
            pacientes=persistencia.load();
            dao.commitTransaction();
            dao.endSession();
        } catch (PersistenceException ex) {
            //Logger.getLogger(ServiciosPacientesPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionServiciosPacientes(ExcepcionServiciosPacientes.EROR_CARGANDO_PACIENTES); 
        }
        return pacientes;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Paciente consultarPaciente(int idPaciente, String tipoid) throws ExcepcionServiciosPacientes {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Paciente paciente;
        try {
            dao.beginSession(); 
            persistencia=dao.getDaoPaciente();
            paciente=persistencia.load(idPaciente, tipoid);
            dao.commitTransaction();
            dao.endSession();
        } catch (PersistenceException ex) {
            //Logger.getLogger(ServiciosPacientesPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionServiciosPacientes(ExcepcionServiciosPacientes.PACIENTE_NO_EXISTENTE);
        }
        return paciente;
    }

    @Override
    public void registrarNuevoPaciente(Paciente p) throws ExcepcionServiciosPacientes {
        try {
            dao.beginSession(); 
            persistencia=dao.getDaoPaciente();
            persistencia.save(p);
            dao.commitTransaction();
            dao.endSession();
        } catch (PersistenceException ex) {
            //Logger.getLogger(ServiciosPacientesPersistencia.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionServiciosPacientes(ExcepcionServiciosPacientes.PACIENTE_EXISTENTE);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //David implementar usando el update del DAO
    @Override
    public void agregarConsultaAPaciente(int idPaciente, String tipoid, Consulta c) throws ExcepcionServiciosPacientes {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
    }
    
}
