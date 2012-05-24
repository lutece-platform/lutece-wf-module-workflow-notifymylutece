/*
 * Copyright (c) 2002-2012, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.notifymylutece.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * TaskNotifyMyLuteceConfigDAO
 *
 */
public class TaskNotifyMyLuteceConfigDAO implements ITaskNotifyMyLuteceConfigDAO
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = " SELECT id_task, id_directory, position_directory_entry_user_guid, sender_name, subject, message " +
        " FROM task_notify_mylutece_cf  WHERE id_task = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO task_notify_mylutece_cf( id_task,id_directory,position_directory_entry_user_guid,sender_name,subject,message )" +
        " VALUES ( ?,?,?,?,?,? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE task_notify_mylutece_cf SET id_directory = ?, position_directory_entry_user_guid = ?, sender_name = ?, subject = ?, message = ? " +
        " WHERE id_task = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM task_notify_mylutece_cf WHERE id_task = ? ";
    private static final String SQL_QUERY_FIND_ALL = " SELECT id_task, id_directory, position_directory_entry_user_guid, sender_name, subject, message " +
        " FROM task_notify_mylutece_cf ";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( TaskNotifyMyLuteceConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, config.getIdTask(  ) );
        daoUtil.setInt( nIndex++, config.getIdDirectory(  ) );
        daoUtil.setInt( nIndex++, config.getPositionEntryDirectoryUserGuid(  ) );
        daoUtil.setString( nIndex++, config.getSenderName(  ) );
        daoUtil.setString( nIndex++, config.getSubject(  ) );
        daoUtil.setString( nIndex++, config.getMessage(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( TaskNotifyMyLuteceConfig config, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        int nIndex = 1;

        daoUtil.setInt( nIndex++, config.getIdDirectory(  ) );
        daoUtil.setInt( nIndex++, config.getPositionEntryDirectoryUserGuid(  ) );
        daoUtil.setString( nIndex++, config.getSenderName(  ) );
        daoUtil.setString( nIndex++, config.getSubject(  ) );
        daoUtil.setString( nIndex++, config.getMessage(  ) );

        daoUtil.setInt( nIndex++, config.getIdTask(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskNotifyMyLuteceConfig load( int nIdTask, Plugin plugin )
    {
        TaskNotifyMyLuteceConfig config = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );

        daoUtil.setInt( 1, nIdTask );

        daoUtil.executeQuery(  );

        int nIndex = 1;

        if ( daoUtil.next(  ) )
        {
            config = new TaskNotifyMyLuteceConfig(  );
            config.setIdTask( daoUtil.getInt( nIndex++ ) );
            config.setIdDirectory( daoUtil.getInt( nIndex++ ) );
            config.setPositionEntryDirectoryUserGuid( daoUtil.getInt( nIndex++ ) );
            config.setSenderName( daoUtil.getString( nIndex++ ) );
            config.setSubject( daoUtil.getString( nIndex++ ) );
            config.setMessage( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free(  );

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdTask, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

        daoUtil.setInt( 1, nIdTask );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskNotifyMyLuteceConfig> loadAll( Plugin plugin )
    {
        List<TaskNotifyMyLuteceConfig> configList = new ArrayList<TaskNotifyMyLuteceConfig>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL, plugin );

        daoUtil.executeQuery(  );

        int nIndex = 1;

        if ( daoUtil.next(  ) )
        {
            TaskNotifyMyLuteceConfig config = new TaskNotifyMyLuteceConfig(  );
            config.setIdTask( daoUtil.getInt( nIndex++ ) );
            config.setIdDirectory( daoUtil.getInt( nIndex++ ) );
            config.setPositionEntryDirectoryUserGuid( daoUtil.getInt( nIndex++ ) );
            config.setSenderName( daoUtil.getString( nIndex++ ) );
            config.setSubject( daoUtil.getString( nIndex++ ) );
            config.setMessage( daoUtil.getString( nIndex++ ) );
            configList.add( config );
        }

        daoUtil.free(  );

        return configList;
    }
}
