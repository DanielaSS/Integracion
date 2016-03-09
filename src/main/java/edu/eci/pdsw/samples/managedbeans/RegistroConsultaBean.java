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

import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.services.ExcepcionServiciosPacientes;
import edu.eci.pdsw.samples.services.ServiciosPacientes;
import java.io.Serializable;
import java.sql.Date;
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
    ServiciosPacientes sp=ServiciosPacientes.getInstance();

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
    
    public void registrar(){
        try {
            sp.registrarNuevoPaciente(new Paciente(id,tipo_id,nombre,Date.valueOf(fechaNacimiento)));
        } catch (ExcepcionServiciosPacientes ex) {
            
        }
    }    
    public String moveToRegistrarPaciente(){
        return "registropacientes";
    }
    public String moveToRegistrarConsulta(){
        return "registroconsultas";
    }
        
}
