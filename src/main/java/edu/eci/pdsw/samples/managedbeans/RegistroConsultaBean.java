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
package edu.eci.pdsw.samples.managedbeans;
 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.services.ExcepcionServiciosPacientes;
import edu.eci.pdsw.samples.services.ServiciosPacientes;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hcadavid
 */
@ManagedBean(name="RegistroConsulta",eager=true)
@SessionScoped
public class RegistroConsultaBean implements Serializable{
    private int id;
    private String tipo_id;
    private String nombre;
    private String fechaNacimiento;
    //Consulta
    private String fechaConsulta;
    private int hora;
    private int minutos;
    private String resumen;
    ///
    ServiciosPacientes sp=ServiciosPacientes.getInstance();
    private Paciente pacienteConsulta; 
    ///
    
    public void agregarConsulta(){
        /*Date fechayHora =Date.valueOf(fechaConsulta);
        fechayHora.setHours(hora);
        fechayHora.setMinutes(minutos);*/
        Consulta consultaPaciente = new Consulta(Date.valueOf(fechaConsulta),resumen);
        try {
            sp.agregarConsultaAPaciente(pacienteConsulta.getId(), pacienteConsulta.getTipo_id(), consultaPaciente);
        } catch (ExcepcionServiciosPacientes ex) {
             error(ex.getMessage());
            //Logger.getLogger(RegistroConsultaBean.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    public void registrar(){
        try {
            sp.registrarNuevoPaciente(new Paciente(id,tipo_id,nombre,Date.valueOf(fechaNacimiento)));
        } catch (ExcepcionServiciosPacientes ex) {
            error(ex.getMessage());
            //Logger.getLogger(RegistroConsultaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
        
    public List<Consulta> pacienteConsultas(){        
         ArrayList<Consulta> ans=new ArrayList<Consulta>();
        for(Consulta p: pacienteConsulta.getConsultas()){
            ans.add(p);
        }
        return ans;
    } 
    public String moveToRegistrarPaciente(){
        return "registropacientes";
    }
    public String moveToRegistrarConsulta(){
        return "registroconsultas";
    }
    public String getFechaConsulta() {
        return fechaConsulta;
    }

    public String getResumen() {
        return resumen;
    }

    public void setFechaConsulta(String fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public Paciente getPacienteConsulta() {
        return pacienteConsulta;
    }

    public void setPacienteConsulta(Paciente pacienteConsulta) {
        this.pacienteConsulta = pacienteConsulta;
    }

    public List<Paciente> getPacientes() throws ExcepcionServiciosPacientes{
        return sp.getPacientes();
    }
    public int getId() {
        return id;
    }

    public String getTipo_id() {
        return tipo_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipo_id(String tipo_id) {
        this.tipo_id = tipo_id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
        public int getHora() {
        return hora;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }  
    public void error(String e) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e+"."));
    }
}
