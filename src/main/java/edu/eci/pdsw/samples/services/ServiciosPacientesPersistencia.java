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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class ServiciosPacientesPersistencia extends ServiciosPacientes{
    private DaoFactory dao;
    private DaoPaciente persistencia;
    
    public ServiciosPacientesPersistencia(DaoFactory dao){
        this.dao=dao;
    }
        
    @Override
    public List<Paciente> getPacientes() {
        persistencia=dao.getDaoPaciente();
        List<Paciente> pacientes=new ArrayList<Paciente>();
        try {
            pacientes=persistencia.load();
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosPacientesPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pacientes;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Paciente consultarPaciente(int idPaciente, String tipoid) throws ExcepcionServiciosPacientes {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        persistencia=dao.getDaoPaciente();
        try {
            return persistencia.load(idPaciente, tipoid);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosPacientes(ExcepcionServiciosPacientes.PACIENTE_NO_EXISTENTE);
        }
    }

    @Override
    public void registrarNuevoPaciente(Paciente p) throws ExcepcionServiciosPacientes {
        persistencia=dao.getDaoPaciente();
        try {
            persistencia.save(p);
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosPacientes(ExcepcionServiciosPacientes.PACIENTE_EXISTENTE);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void agregarConsultaAPaciente(int idPaciente, String tipoid, Consulta c) throws ExcepcionServiciosPacientes {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
