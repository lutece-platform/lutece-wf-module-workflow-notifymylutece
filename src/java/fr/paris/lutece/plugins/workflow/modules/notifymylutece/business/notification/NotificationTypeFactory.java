/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.notification;

import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.NotifyMyLutecePlugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * NotificationTypeFactory
 *
 */
public final class NotificationTypeFactory
{
    private static final String BEAN_NOTIFYMYLUTECE_NOTIFICATION_TYPE_FACTORY = "workflow-notifymylutece.notificationTypeFactory";
    private Map<String, INotificationType> _mapNotificationTypes;

    /**
     * Private constructor
     */
    private NotificationTypeFactory(  )
    {
    }

    /**
     * Get the factory
     * @return the factory
     */
    public static NotificationTypeFactory getFactory(  )
    {
        return (NotificationTypeFactory) SpringContextService.getPluginBean( NotifyMyLutecePlugin.PLUGIN_NAME,
            BEAN_NOTIFYMYLUTECE_NOTIFICATION_TYPE_FACTORY );
    }

    /**
     * Set the notification types
     * @param mapNotificationTypes a list of {@link INotificationType}
     */
    public void setNotificationTypes( Map<String, INotificationType> mapNotificationTypes )
    {
        _mapNotificationTypes = mapNotificationTypes;
    }

    /**
     * Get the notification types
     * @return a map of (id_notifiaction_type, {@link INotificationType}
     */
    public Map<String, INotificationType> getNotificationTypes(  )
    {
        if ( _mapNotificationTypes == null )
        {
            init(  );
        }

        return _mapNotificationTypes;
    }

    /**
     * Get the notification type given an id
     * @param nIdNotificationType the id notification type
     * @return a {@link INotificationType}
     */
    public INotificationType getNotificationType( int nIdNotificationType )
    {
        if ( _mapNotificationTypes == null )
        {
            init(  );
        }

        return _mapNotificationTypes.get( Integer.toString( nIdNotificationType ) );
    }

    /**
     * Init the map in case the map is null
     */
    private void init(  )
    {
        _mapNotificationTypes = new HashMap<String, INotificationType>(  );

        for ( INotificationType notificationType : SpringContextService.getBeansOfType( INotificationType.class ) )
        {
            _mapNotificationTypes.put( Integer.toString( notificationType.getIdType(  ) ), notificationType );
        }
    }
}
