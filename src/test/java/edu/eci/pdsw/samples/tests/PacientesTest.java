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

import edu.eci.pdsw.samples.entities.Paciente;
import edu.eci.pdsw.samples.services.ExcepcionServiciosPacientes;
import edu.eci.pdsw.samples.services.ServiciosPacientesStub;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hcadavid
 */
public class PacientesTest {
    /**
     * Clases de equivalenci:
     * 1) Se registra un paciente sin problemas 
     * 2) Se registra varias veces el mismo paciente
     * 3) Se registra enviando null, al paciente
     * 4) 
     */
    
    public PacientesTest() {
    }
    
    @Before
    public void setUp() {
    }
    //Metodo RegistrarPacientes
    //Prueba 1, Se registra sin problemas un paciente
    @Test
    public void classEqUnoRegistroPacienteSinProblemasTest() {
        try{
           Paciente Emma= new Paciente(1,"CC","Emma Caballero",java.sql.Date.valueOf("2013-08-15"));
           ServiciosPacientesStub servicios= new ServiciosPacientesStub();
           servicios.registrarNuevoPaciente(Emma);
           Assert.assertEquals(servicios.consultarPaciente(1,"CC").toString(),Emma.toString());
        }catch(ExcepcionServiciosPacientes e){
            Assert.fail("Lanzo excepcion "+ e.getMessage());
        }
    }
    //Prueba 2, Se registra el mismo paciente dos veces, deberia presentar excepcion
    @Test
    public void classEqDosRegistroPacienteDocumentoIdNegativo(){
        try{
           Paciente Thomas= new Paciente(2,"CC","Thomas Caballero",java.sql.Date.valueOf("2010-10-07"));
           ServiciosPacientesStub servicios= new ServiciosPacientesStub();
           servicios.registrarNuevoPaciente(Thomas);
           servicios.registrarNuevoPaciente(Thomas);
           Assert.fail("No lanzo excepcion");
        }catch(ExcepcionServiciosPacientes e){
            Assert.assertTrue("Lanzo Excepcion"+e.getMessage(), true);
        }
    }
  
       
}
