/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.persistence.mybatis;

import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.mybatis.mappers.PacienteMapper;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author 
 */
public class MyBatisDaoPaciente implements DaoPaciente{
    
    private SqlSession ses;
    private PacienteMapper pmap;
    
    public MyBatisDaoPaciente(SqlSession sesion) {
        ses=sesion;
        pmap=ses.getMapper(PacienteMapper.class);
    }

    @Override
    public Paciente load(int id, String tipoid) throws PersistenceException {
        return pmap.loadPacienteById(id, tipoid);     
    }

    @Override
    public void save(Paciente p) throws PersistenceException {
        Paciente r=pmap.loadPacienteById(p.getId(), p.getTipo_id());
        if(r!=null) throw new PersistenceException(PersistenceException.PACIENTE_EXISTENTE);
        pmap.insertPaciente(p);
        //System.out.println("Paso paciente");
        for(Consulta c:p.getConsultas()){
            pmap.insertConsulta(c, p.getId(), p.getTipo_id());
        }
    }

    @Override
    public void update(Paciente p) throws PersistenceException {
        Paciente r=pmap.loadPacienteById(p.getId(), p.getTipo_id());
        Set<Consulta>pCon=p.getConsultas();
        Set<Consulta>rCon=r.getConsultas();
        pmap.update(p,r);
        for(Consulta c:rCon){
            if(!pCon.contains(c)){
                pmap.insertConsulta(c, p.getId(), p.getTipo_id());
            }
        }
    }

    @Override
    public List<Paciente> load() throws PersistenceException {
        return pmap.loadAll(); 
    }
    
}
