/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.modules.notifymylutece.service;

import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.TaskNotifyMyLuteceConfig;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.TaskNotifyMyLuteceConfigHome;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.notification.NotificationTypeHome;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.retrieval.RetrievalTypeHome;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.user.MyLuteceUserGuidHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * TaskNotifyMyLuteceConfigService
 *
 */
public final class TaskNotifyMyLuteceConfigService
{
    private static final String BEAN_TASK_NOTIFY_MYLUTECE_CONFIG_SERVICE = "workflow-notifymylutece.taskNotifyMyLuteceConfigService";

    /**
     * Private constructor
     */
    private TaskNotifyMyLuteceConfigService(  )
    {
    }

    /**
     * Get the instance of {@link TaskNotifyMyLuteceConfigService}
     * @return the instance of {@link TaskNotifyMyLuteceConfigService}
     */
    public static TaskNotifyMyLuteceConfigService getService(  )
    {
        return (TaskNotifyMyLuteceConfigService) SpringContextService.getPluginBean( NotifyMyLutecePlugin.PLUGIN_NAME,
            BEAN_TASK_NOTIFY_MYLUTECE_CONFIG_SERVICE );
    }

    /**
     * Create a new config
     * @param config the config
     */
    public void create( TaskNotifyMyLuteceConfig config )
    {
        if ( config != null )
        {
            TaskNotifyMyLuteceConfigHome.create( config );

            for ( int nIdNotificationType : config.getListIdsNotificationType(  ) )
            {
                NotificationTypeHome.create( config.getIdTask(  ), nIdNotificationType );
            }

            for ( int nIdRetrievalType : config.getListIdsRetrievalType(  ) )
            {
                RetrievalTypeHome.create( config.getIdTask(  ), nIdRetrievalType );
            }

            for ( String strUserGuid : config.getListUserGuid(  ) )
            {
                MyLuteceUserGuidHome.create( config.getIdTask(  ), strUserGuid );
            }
        }
    }

    /**
     * Update a config
     * @param config the config
     */
    public void update( TaskNotifyMyLuteceConfig config )
    {
        if ( config != null )
        {
            TaskNotifyMyLuteceConfigHome.update( config );
            NotificationTypeHome.remove( config.getIdTask(  ) );

            for ( int nIdNotificationType : config.getListIdsNotificationType(  ) )
            {
                NotificationTypeHome.create( config.getIdTask(  ), nIdNotificationType );
            }

            RetrievalTypeHome.remove( config.getIdTask(  ) );

            for ( int nIdRetrievalType : config.getListIdsRetrievalType(  ) )
            {
                RetrievalTypeHome.create( config.getIdTask(  ), nIdRetrievalType );
            }

            MyLuteceUserGuidHome.remove( config.getIdTask(  ) );

            for ( String strUserGuid : config.getListUserGuid(  ) )
            {
                MyLuteceUserGuidHome.create( config.getIdTask(  ), strUserGuid );
            }
        }
    }

    /**
     * Remove a config
     * @param nIdTask the task id
     */
    public void remove( int nIdTask )
    {
        TaskNotifyMyLuteceConfigHome.remove( nIdTask );
        NotificationTypeHome.remove( nIdTask );
        RetrievalTypeHome.remove( nIdTask );
        MyLuteceUserGuidHome.remove( nIdTask );
    }

    /**
     * Find a config
     * @param nIdTask the id task
     * @return an instance of {@link TaskNotifyMyLuteceConfig}
     */
    public TaskNotifyMyLuteceConfig findByPrimaryKey( int nIdTask )
    {
        TaskNotifyMyLuteceConfig config = TaskNotifyMyLuteceConfigHome.findByPrimaryKey( nIdTask );

        if ( config != null )
        {
            config.setListIdsNotificationType( NotificationTypeHome.find( nIdTask ) );
            config.setListIdsRetrievalType( RetrievalTypeHome.find( nIdTask ) );
            config.setListUserGuid( MyLuteceUserGuidHome.find( config.getIdTask(  ) ) );
        }

        return config;
    }

    /**
     * Get all configs
     * @return a list of {@link TaskNotifyMyLuteceConfig}
     */
    public List<TaskNotifyMyLuteceConfig> findAll(  )
    {
        List<TaskNotifyMyLuteceConfig> listConfigs = TaskNotifyMyLuteceConfigHome.findAll(  );

        for ( TaskNotifyMyLuteceConfig config : listConfigs )
        {
            if ( config != null )
            {
                config.setListIdsNotificationType( NotificationTypeHome.find( config.getIdTask(  ) ) );
                config.setListIdsRetrievalType( RetrievalTypeHome.find( config.getIdTask(  ) ) );
                config.setListUserGuid( MyLuteceUserGuidHome.find( config.getIdTask(  ) ) );
            }
        }

        return listConfigs;
    }
}
