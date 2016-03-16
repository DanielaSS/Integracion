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
package edu.eci.pdsw.samples.tests;

import edu.eci.pdsw.samples.entities.Consulta;
import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.services.ExcepcionServiciosPacientes;
import edu.eci.pdsw.samples.services.ServiciosPacientesPersistencia;
import edu.eci.pdsw.samples.services.ServiciosPacientesStub;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;

/**
 *
 * @author hcadavid
 */
public class ConsultasTest {
    /*
    Clases de equivalencia:
    Se añade un paciente
    Se crea una consulta del paciente
    Se intenta consultar paciente inexistente
    Se intenta añadir consulta a paciente inexistente
    */
    //Se añaden pacientes a los servicios
    @Test
    public void deberiaAddPaciente(){
        try {
            Paciente p=new Paciente(5, "CC", "Daniela", Date.valueOf("1995-10-05"));
            ServiciosPacientesStub stb=new ServiciosPacientesStub();
            stb.registrarNuevoPaciente(p);
        } catch (ExcepcionServiciosPacientes ex) {
            Assert.fail("Lanzo excepcion");
        }
    }  
    //Se añade consulta a un paciente
    @Test
    public void deberiaAddConsultaAlPaciente(){
        try {
            boolean correcto=false;
            Paciente p=new Paciente(1, "CC", "Daniela", Date.valueOf("1995-10-05"));
            ServiciosPacientesStub stb=new ServiciosPacientesStub();
            Consulta c=new Consulta(Date.valueOf("2016-03-03"),"Daniela se pego en el meñique");
            stb.registrarNuevoPaciente(p);
            stb.agregarConsultaAPaciente(1, "CC", c);
            Set<Consulta> consultasP=stb.consultarPaciente(1, "CC").getConsultas();
            for(Consulta i: consultasP){
                if(i.toString().equals(c.toString())){
                    correcto=true;
                }
            }
            Assert.assertTrue(correcto);
        } catch (ExcepcionServiciosPacientes ex) {
            Assert.fail("Lanzo excepcion");
        }
    }   
    //Se intenta consultar un paciente inexistente
    @Test
    public void noDeberiaConsultarPacienteInexistente(){
        try{
            ServiciosPacientesStub stb=new ServiciosPacientesStub();
            stb.consultarPaciente(23, "CC");
            Assert.fail("No se lanzo excepcion");
        } catch(ExcepcionServiciosPacientes ex){
            Assert.assertEquals(ex.getMessage(),"Paciente "+23+" no esta registrado");
        }
    }
    //Se intenta añadir una consulta a paciente inexistente
    @Test
    public void noDeberiaAddConsultaPacienteInexistente(){
        try{
            Consulta c=new Consulta();
            ServiciosPacientesStub stb=new ServiciosPacientesStub();
            stb.agregarConsultaAPaciente(23, "CC", c);
            Assert.fail("No se lanzo excepcion");
        } catch(ExcepcionServiciosPacientes ex){
            Assert.assertEquals(ex.getMessage(),"Paciente "+23+" no esta registrado");
        }  
    }
    
}
