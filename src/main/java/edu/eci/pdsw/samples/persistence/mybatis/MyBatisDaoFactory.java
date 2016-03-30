/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.persistence.mybatis;

import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoPaciente;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author 
 */
public class MyBatisDaoFactory extends DaoFactory {
    
    private static SqlSessionFactory sqlSessionFabrica;
    private SqlSession sesion;
    
    public static SqlSessionFactory getSqlSessionFactory(String config) {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream(config);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    public MyBatisDaoFactory(String property) {
        sqlSessionFabrica=getSqlSessionFactory(property);
    }

    @Override
    public void beginSession() throws PersistenceException {
        sesion=sqlSessionFabrica.openSession();
    }

    @Override
    public DaoPaciente getDaoPaciente() {
        return new MyBatisDaoPaciente(sesion);
    }

    @Override
    public void commitTransaction() throws PersistenceException {
        sesion.commit();
    }

    @Override
    public void rollbackTransaction() throws PersistenceException {
        sesion.rollback();
    }

    @Override
    public void endSession() throws PersistenceException {
        sesion.close();
    }

}
