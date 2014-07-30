/*
 * This is free and unencumbered software released into the public domain.
 */

package com.mycompany.dao;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;

/**
 * Used to generate a connection factory object used by Mybatis DAO impls.
 *
 * @author Shawn McKinney
 * @version $Rev$
 */
public class ConnectionFactory
{
    protected static final SqlSessionFactory FACTORY;

    static
    {
        try
        {
            Reader reader = Resources.getResourceAsReader( "Configuration.xml" );
            FACTORY = new SqlSessionFactoryBuilder().build( reader );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e.toString() );
        }
    }

    /**
     *
     * @return
     */
    public static SqlSessionFactory getSqlSessionFactory()
    {
        return FACTORY;
    }
}