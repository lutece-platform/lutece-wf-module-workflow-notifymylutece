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
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.notification.INotificationTypeService;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.retrieval.IRetrievalTypeService;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.user.IMyLuteceUserGuidService;
import fr.paris.lutece.plugins.workflow.utils.WorkflowUtils;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfig;
import fr.paris.lutece.plugins.workflowcore.service.config.TaskConfigService;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;


/**
 *
 * TaskNotifyMyLuteceConfigService
 *
 */
public class TaskNotifyMyLuteceConfigService extends TaskConfigService
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
    @Transactional( NotifyMyLutecePlugin.BEAN_TRANSACTION_MANAGER )
    public void create( ITaskConfig config )
    {
        super.create( config );

        TaskNotifyMyLuteceConfig notifyConfig = getConfigBean( config );

        if ( notifyConfig != null )
        {
            for ( int nIdNotificationType : notifyConfig.getListIdsNotificationType(  ) )
            {
                if ( nIdNotificationType != WorkflowUtils.CONSTANT_ID_NULL )
                {
                    _notificationTypeService.create( config.getIdTask(  ), nIdNotificationType );
                }
            }

            for ( int nIdRetrievalType : notifyConfig.getListIdsRetrievalType(  ) )
            {
                if ( nIdRetrievalType != WorkflowUtils.CONSTANT_ID_NULL )
                {
                    _retrievalTypeService.create( config.getIdTask(  ), nIdRetrievalType );
                }
            }

            for ( String strUserGuid : notifyConfig.getListUserGuid(  ) )
            {
                _myLuteceUserGuidService.create( config.getIdTask(  ), strUserGuid );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( NotifyMyLutecePlugin.BEAN_TRANSACTION_MANAGER )
    public void update( ITaskConfig config )
    {
        super.update( config );

        TaskNotifyMyLuteceConfig notifyConfig = getConfigBean( config );

        if ( notifyConfig != null )
        {
            _notificationTypeService.remove( config.getIdTask(  ) );

            if ( ( notifyConfig.getListIdsNotificationType(  ) != null ) &&
                    ( notifyConfig.getListIdsNotificationType(  ).length > 0 ) )
            {
                for ( int nIdNotificationType : notifyConfig.getListIdsNotificationType(  ) )
                {
                    if ( nIdNotificationType != WorkflowUtils.CONSTANT_ID_NULL )
                    {
                        _notificationTypeService.create( config.getIdTask(  ), nIdNotificationType );
                    }
                }
            }

            _retrievalTypeService.remove( config.getIdTask(  ) );

            if ( ( notifyConfig.getListIdsRetrievalType(  ) != null ) &&
                    ( notifyConfig.getListIdsRetrievalType(  ).length > 0 ) )
            {
                for ( int nIdRetrievalType : notifyConfig.getListIdsRetrievalType(  ) )
                {
                    if ( nIdRetrievalType != WorkflowUtils.CONSTANT_ID_NULL )
                    {
                        _retrievalTypeService.create( config.getIdTask(  ), nIdRetrievalType );
                    }
                }
            }

            _myLuteceUserGuidService.remove( config.getIdTask(  ) );

            if ( ( notifyConfig.getListUserGuid(  ) != null ) && ( notifyConfig.getListUserGuid(  ).length > 0 ) )
            {
                for ( String strUserGuid : notifyConfig.getListUserGuid(  ) )
                {
                    _myLuteceUserGuidService.create( config.getIdTask(  ), strUserGuid );
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( NotifyMyLutecePlugin.BEAN_TRANSACTION_MANAGER )
    public void remove( int nIdTask )
    {
        super.remove( nIdTask );
        _notificationTypeService.remove( nIdTask );
        _retrievalTypeService.remove( nIdTask );
        _myLuteceUserGuidService.remove( nIdTask );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T findByPrimaryKey( int nIdTask )
    {
        TaskNotifyMyLuteceConfig config = super.findByPrimaryKey( nIdTask );

        if ( config != null )
        {
            List<Integer> listIdsNotificationType = _notificationTypeService.find( nIdTask );

            if ( ( listIdsNotificationType != null ) && !listIdsNotificationType.isEmpty(  ) )
            {
                config.setListIdsNotificationType( listIdsNotificationType.toArray( 
                        new Integer[listIdsNotificationType.size(  )] ) );
            }

            List<Integer> listIdsRetrievalType = _retrievalTypeService.find( nIdTask );

            if ( ( listIdsRetrievalType != null ) && !listIdsRetrievalType.isEmpty(  ) )
            {
                config.setListIdsRetrievalType( listIdsRetrievalType.toArray( 
                        new Integer[listIdsRetrievalType.size(  )] ) );
            }

            List<String> listUserGuid = _myLuteceUserGuidService.find( config.getIdTask(  ) );

            if ( ( listUserGuid != null ) && !listUserGuid.isEmpty(  ) )
            {
                config.setListUserGuid( listUserGuid.toArray( new String[listUserGuid.size(  )] ) );
            }
        }

        return (T) config;
    }
}
