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
package fr.paris.lutece.plugins.workflow.modules.notifymylutece.service;

import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.TaskNotifyMyLuteceConfig;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.TaskNotifyMyLuteceConfigHome;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.notification.INotificationTypeService;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.retrieval.IRetrievalTypeService;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.user.IMyLuteceUserGuidService;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;


/**
 *
 * TaskNotifyMyLuteceConfigService
 *
 */
public class TaskNotifyMyLuteceConfigService implements ITaskNotifyMyLuteceConfigService
{
    public static final String BEAN_SERVICE = "workflow-notifymylutece.taskNotifyMyLuteceConfigService";
    @Inject
    private INotificationTypeService _notificationTypeService;
    @Inject
    private IRetrievalTypeService _retrievalTypeService;
    @Inject
    private IMyLuteceUserGuidService _myLuteceUserGuidService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "workflow-notifymylutece.transactionManager" )
    public void create( TaskNotifyMyLuteceConfig config )
    {
        if ( config != null )
        {
            TaskNotifyMyLuteceConfigHome.create( config );

            for ( int nIdNotificationType : config.getListIdsNotificationType(  ) )
            {
                _notificationTypeService.create( config.getIdTask(  ), nIdNotificationType );
            }

            for ( int nIdRetrievalType : config.getListIdsRetrievalType(  ) )
            {
                _retrievalTypeService.create( config.getIdTask(  ), nIdRetrievalType );
            }

            for ( String strUserGuid : config.getListUserGuid(  ) )
            {
                _myLuteceUserGuidService.create( config.getIdTask(  ), strUserGuid );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "workflow-notifymylutece.transactionManager" )
    public void update( TaskNotifyMyLuteceConfig config )
    {
        if ( config != null )
        {
            TaskNotifyMyLuteceConfigHome.update( config );
            _notificationTypeService.remove( config.getIdTask(  ) );

            for ( int nIdNotificationType : config.getListIdsNotificationType(  ) )
            {
                _notificationTypeService.create( config.getIdTask(  ), nIdNotificationType );
            }

            _retrievalTypeService.remove( config.getIdTask(  ) );

            for ( int nIdRetrievalType : config.getListIdsRetrievalType(  ) )
            {
                _retrievalTypeService.create( config.getIdTask(  ), nIdRetrievalType );
            }

            _myLuteceUserGuidService.remove( config.getIdTask(  ) );

            for ( String strUserGuid : config.getListUserGuid(  ) )
            {
                _myLuteceUserGuidService.create( config.getIdTask(  ), strUserGuid );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( "workflow-notifymylutece.transactionManager" )
    public void remove( int nIdTask )
    {
        TaskNotifyMyLuteceConfigHome.remove( nIdTask );
        _notificationTypeService.remove( nIdTask );
        _retrievalTypeService.remove( nIdTask );
        _myLuteceUserGuidService.remove( nIdTask );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskNotifyMyLuteceConfig findByPrimaryKey( int nIdTask )
    {
        TaskNotifyMyLuteceConfig config = TaskNotifyMyLuteceConfigHome.findByPrimaryKey( nIdTask );

        if ( config != null )
        {
            config.setListIdsNotificationType( _notificationTypeService.find( nIdTask ) );
            config.setListIdsRetrievalType( _retrievalTypeService.find( nIdTask ) );
            config.setListUserGuid( _myLuteceUserGuidService.find( config.getIdTask(  ) ) );
        }

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskNotifyMyLuteceConfig> findAll(  )
    {
        List<TaskNotifyMyLuteceConfig> listConfigs = TaskNotifyMyLuteceConfigHome.findAll(  );

        for ( TaskNotifyMyLuteceConfig config : listConfigs )
        {
            if ( config != null )
            {
                config.setListIdsNotificationType( _notificationTypeService.find( config.getIdTask(  ) ) );
                config.setListIdsRetrievalType( _retrievalTypeService.find( config.getIdTask(  ) ) );
                config.setListUserGuid( _myLuteceUserGuidService.find( config.getIdTask(  ) ) );
            }
        }

        return listConfigs;
    }
}
