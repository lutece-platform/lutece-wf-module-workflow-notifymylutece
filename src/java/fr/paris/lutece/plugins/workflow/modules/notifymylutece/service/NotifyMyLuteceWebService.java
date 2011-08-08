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

import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.signrequest.NotifyMyLuteceRequestAuthenticatorService;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.util.constants.NotifyMyLuteceConstants;
import fr.paris.lutece.plugins.workflow.service.security.WorkflowUserAttributesManager;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * NotifyMyLuteceWebService
 *
 */
public final class NotifyMyLuteceWebService
{
    private static final String BEAN_NOTIFY_MYLUTECE_WEBSERVICE = "workflow-notifymylutece.notifyMyLuteceWebService";

    /**
     * Private constructor
     */
    private NotifyMyLuteceWebService(  )
    {
    }

    /**
     * Get the instance of the service
     * @return the instance of the service
     */
    public static NotifyMyLuteceWebService getService(  )
    {
        return (NotifyMyLuteceWebService) SpringContextService.getPluginBean( NotifyMyLutecePlugin.PLUGIN_NAME,
            BEAN_NOTIFY_MYLUTECE_WEBSERVICE );
    }

    /**
     * Calls the MyLutece Notification REST WS to notify an user
     * @param strObject the object
     * @param strMessage the message
     * @param strSender the sender
     * @param strReceiver the receiver
     */
    public void notify( String strObject, String strMessage, String strSender, String strReceiver )
    {
        if ( WorkflowUserAttributesManager.getManager(  ).isEnabled(  ) )
        {
            String strUrl = "http://localhost:8080/lutece" + NotifyMyLuteceConstants.URL_REST_NOTIFY;

            // List parameters to post
            Map<String, String> params = new HashMap<String, String>(  );
            params.put( NotifyMyLuteceConstants.PARAMETER_NOTIFICATION_OBJECT, strObject );
            params.put( NotifyMyLuteceConstants.PARAMETER_NOTIFICATION_MESSAGE, strMessage );
            params.put( NotifyMyLuteceConstants.PARAMETER_NOTIFICATION_SENDER, strSender );
            params.put( NotifyMyLuteceConstants.PARAMETER_NOTIFICATION_RECEIVER, strReceiver );

            // List elements to include to the signature
            List<String> listElements = new ArrayList<String>(  );
            listElements.add( strObject );
            listElements.add( strSender );
            listElements.add( strReceiver );

            try
            {
                HttpAccess httpAccess = new HttpAccess(  );
                httpAccess.doPost( strUrl, params,
                    NotifyMyLuteceRequestAuthenticatorService.getRequestAuthenticator(  ), listElements );
            }
            catch ( HttpAccessException e )
            {
                String strError = "NotifyMyLuteceWebService - Error connecting to '" + strUrl + "' : ";
                AppLogService.error( strError + e.getMessage(  ), e );
                throw new AppException( e.getMessage(  ), e );
            }
        }
        else
        {
            throw new AppException( 
                "NotifyMyLuteceWebService - Could not notify the user : the property file 'workflow.property' is not well configured." );
        }
    }
}
