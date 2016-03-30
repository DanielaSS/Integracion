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
//import static edu.eci.pdsw.samples.textview.MyBatisExample.getSqlSessionFactory;
import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.eci.pdsw.samples.mybatis.mappers.PacienteMapper;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import org.junit.After;

/**
 *
 * @author hcadavid
 */
public class PersistenceTest {
@After
    public void borrar() throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "", "");
        Statement stmt = conn.createStatement();
        stmt.execute("delete from CONSULTAS");
        stmt.execute("delete from PACIENTES");
         
    }
    public static SqlSessionFactory getSqlSessionFactory(){
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                //System.out.println("Por crear");
                inputStream = Resources.getResourceAsStream("mybatis-config-h2.xml");
                //System.out.println("Creado");
                //System.out.println("SessionFactory");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                //System.out.println("Paso");
            } catch (IOException e) {
                //System.out.println(e.toString());
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }  
    @Test
    public void Plantilla(){
        //System.out.println("Prueba1");
        //System.out.println("Paso 0");
        SqlSessionFactory sessionfact = getSqlSessionFactory();
        //System.out.println("Paso 1");
        SqlSession sqlss = sessionfact.openSession();
        //System.out.println("Paso 2");
        PacienteMapper pedmp=sqlss.getMapper(PacienteMapper.class);
        //System.out.println("paso 3");
        System.out.println(pedmp.loadPacienteById(1, "cc"));
        sqlss.close();
        
    }
    @Test
    public void testPacienteRegistrarPacienteSinConsultas(){
        //System.out.println("Prueba2");
        SqlSessionFactory sessionfact = getSqlSessionFactory();
        //System.out.println("Paso 1");
        SqlSession sqlss = sessionfact.openSession();
        //System.out.println("Paso 2");
        PacienteMapper pedmp=sqlss.getMapper(PacienteMapper.class);
        //System.out.println("paso 3");
        //System.out.println(pedmp.loadPacienteById(1, "cc"));
        Paciente p=new Paciente(1,"CC","AAAAAA BBBBB",Date.valueOf("2000-01-01"));
        pedmp.insertPaciente(p);
        sqlss.commit();
        Paciente resul=pedmp.loadPacienteById(1, "CC");
        sqlss.close();
        assertTrue(p.equals(resul));       
    }
    @Test
    public void testPacienteRegistrarPacienteConUnaConsulta(){
        //System.out.println("Prueba3");
        //System.out.println("Paso 0");
        SqlSessionFactory sessionfact = getSqlSessionFactory();
        //System.out.println("Paso 1");
        SqlSession sqlss = sessionfact.openSession();
        //System.out.println("Paso 2");
        PacienteMapper pedmp=sqlss.getMapper(PacienteMapper.class);
        //System.out.println("paso 3");
        //System.out.println(pedmp.loadPacienteById(1, "cc"));
        Paciente p=new Paciente(2,"CE","BBBB CCCCC",Date.valueOf("1993-02-03"));
        Consulta unaConsulta=new Consulta(Date.valueOf("2000-01-26"),"Alergia a picadura de abeja");
        Set<Consulta> setConsultas=new HashSet<Consulta>();
        setConsultas.add(unaConsulta);
        //System.out.println("Paso 1");
        pedmp.insertPaciente(p);
        //System.out.println("Intermedio");
        pedmp.insertConsulta(unaConsulta, p.getId(), p.getTipo_id());
        //System.out.println("Paso 2");
        sqlss.commit();
        p.setConsultas(setConsultas);
        Paciente resultado=pedmp.loadPacienteById(2, "CE");
        sqlss.close();
        assertTrue(p.equals(resultado));              
    }
    @Test
    public void testPacienteRegistrarPacienteConMasDeUnaConsulta(){
        //System.out.println("Prueba4");
        //System.out.println("Paso 0");
        SqlSessionFactory sessionfact = getSqlSessionFactory();
        //System.out.println("Paso 1");
        SqlSession sqlss = sessionfact.openSession();
        //System.out.println("Paso 2");
        PacienteMapper pedmp=sqlss.getMapper(PacienteMapper.class);
        //System.out.println("paso 3");
        //System.out.println(pedmp.loadPacienteById(1, "cc"));
        Paciente p=new Paciente(3,"CC","ASDF ASGD",Date.valueOf("1993-02-03"));
        Consulta unaConsulta=new Consulta(Date.valueOf("2001-01-26"),"Alergia a picadura de abeja");
        Consulta otraConsulta=new Consulta(Date.valueOf("2002-01-26"),"Alergia a picadura de avispa");
        Set<Consulta> setConsultas=new HashSet<Consulta>();
        setConsultas.add(unaConsulta);
        setConsultas.add(otraConsulta);
        p.setConsultas(setConsultas);
        pedmp.insertPaciente(p);
        pedmp.insertConsulta(unaConsulta, p.getId(), p.getTipo_id());
        pedmp.insertConsulta(otraConsulta, p.getId(), p.getTipo_id());
        sqlss.commit();
        Paciente resultado=pedmp.loadPacienteById(3, "CC");
        sqlss.close();
        
        assertTrue(p.equals(resultado));
    
    }
    @Test
    public void testPacienteExistente(){
        SqlSessionFactory sessionfact = getSqlSessionFactory();
        SqlSession sqlss = sessionfact.openSession();
        PacienteMapper pedmp=sqlss.getMapper(PacienteMapper.class);
        
        sqlss.close();
        
    }
}
