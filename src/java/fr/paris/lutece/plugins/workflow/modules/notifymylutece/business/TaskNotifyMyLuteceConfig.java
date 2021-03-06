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
package fr.paris.lutece.plugins.workflow.modules.notifymylutece.business;

import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.notification.INotificationType;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.notification.NotificationTypeFactory;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.retrieval.IRetrievalType;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.retrieval.IRetrievalTypeFactory;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.retrieval.RetrievalTypeFactory;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.util.annotation.NotifyMyLuteceConfig;
import fr.paris.lutece.plugins.workflowcore.business.config.TaskConfig;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 *
 * TaskNotifyDirectoryConfig
 *
 */
@NotifyMyLuteceConfig
public class TaskNotifyMyLuteceConfig extends TaskConfig
{
    @NotNull
    @Min( 1 )
    private int _nIdDirectory;
    private int _nPositionEntryDirectoryUserGuid;
    @NotNull
    private Integer[] _listIdsNotificationType;
    @NotNull
    private Integer[] _listIdsRetrievalType;
    private String[] _listUserGuid;
    @NotNull
    private String _strSubject;
    @NotNull
    private String _strMessage;
    @NotNull
    private String _strSenderName;

    /**
     * Get the id directory
     * @return id directory
     */
    public int getIdDirectory(  )
    {
        return _nIdDirectory;
    }

    /**
     * Set id directory
     * @param idDirectory id directory
     */
    public void setIdDirectory( int idDirectory )
    {
        _nIdDirectory = idDirectory;
    }

    /**
     * Get the position of the entry directory associated to the user guid
     * @return position Entry directory user guid
     */
    public int getPositionEntryDirectoryUserGuid(  )
    {
        return _nPositionEntryDirectoryUserGuid;
    }

    /**
     * Set position Entry directory user guid
     * @param nPositionEntryDirectoryUserGuid position of Entry directory user guid
     */
    public void setPositionEntryDirectoryUserGuid( int nPositionEntryDirectoryUserGuid )
    {
        _nPositionEntryDirectoryUserGuid = nPositionEntryDirectoryUserGuid;
    }

    /**
    * Get the subject
    * @return the subject of the message
    */
    public String getSubject(  )
    {
        return _strSubject;
    }

    /**
     * Set the subject of the message
     * @param subject the subject of the message
     */
    public void setSubject( String subject )
    {
        _strSubject = subject;
    }

    /**
     * Get the message
     * @return the message of the notification
     */
    public String getMessage(  )
    {
        return _strMessage;
    }

    /**
     * Set the message of the notification
     * @param message the message of the notification
     */
    public void setMessage( String message )
    {
        _strMessage = message;
    }

    /**
     * Get the sender name
     * @return the sender name
     */
    public String getSenderName(  )
    {
        return _strSenderName;
    }

    /**
     * Set the sender name
     * @param senderName  the sender name
     */
    public void setSenderName( String senderName )
    {
        _strSenderName = senderName;
    }

    /**
     * Set the list of ids notification types
     * @param listIdsNotificationType the list of ids notification type
     */
    public void setListIdsNotificationType( Integer[] listIdsNotificationType )
    {
        _listIdsNotificationType = listIdsNotificationType;
    }

    /**
     * Get the list of ids of notification type
     * @return the list of ids notification type
     */
    public Integer[] getListIdsNotificationType(  )
    {
        return _listIdsNotificationType;
    }

    /**
     * Get the notification types
     * @return the notification types
     */
    public List<INotificationType> getNotificationTypes(  )
    {
        List<INotificationType> listNotificationTypes = new ArrayList<INotificationType>(  );

        if ( ( _listIdsNotificationType != null ) && ( _listIdsNotificationType.length > 0 ) )
        {
            for ( int nIdNotificationType : _listIdsNotificationType )
            {
                listNotificationTypes.add( NotificationTypeFactory.getFactory(  )
                                                                  .getNotificationType( nIdNotificationType ) );
            }
        }

        return listNotificationTypes;
    }

    /**
     * Set the list of ids retrieval type
     * @param listIdsRetrievalType the ids retrieval type
     */
    public void setListIdsRetrievalType( Integer[] listIdsRetrievalType )
    {
        _listIdsRetrievalType = listIdsRetrievalType;
    }

    /**
     * Get the list of ids retrieval type
     * @return the list of ids retrieval type
     */
    public Integer[] getListIdsRetrievalType(  )
    {
        return _listIdsRetrievalType;
    }

    /**
     * Get the retrieval types
     * @return the retrieval types
     */
    public List<IRetrievalType> getRetrievalTypes(  )
    {
        List<IRetrievalType> listRetrievalTypes = new ArrayList<IRetrievalType>(  );

        if ( ( _listIdsRetrievalType != null ) && ( _listIdsRetrievalType.length > 0 ) )
        {
            IRetrievalTypeFactory factory = SpringContextService.getBean( RetrievalTypeFactory.BEAN_FACTORY );

            for ( int nIdRetrievalType : _listIdsRetrievalType )
            {
                listRetrievalTypes.add( factory.getRetrievalType( nIdRetrievalType ) );
            }
        }

        return listRetrievalTypes;
    }

    /**
     * Set the list of user guid
     * @param listUserGuid the list of user guid
     */
    public void setListUserGuid( String[] listUserGuid )
    {
        _listUserGuid = listUserGuid;
    }

    /**
     * Get the list of user guid
     * @return the list of user guid
     */
    public String[] getListUserGuid(  )
    {
        return _listUserGuid;
    }
}
