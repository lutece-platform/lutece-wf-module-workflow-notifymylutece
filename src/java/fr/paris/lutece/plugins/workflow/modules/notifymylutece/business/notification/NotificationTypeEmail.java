/*
 * Copyright (c) 2002-2013, Mairie de Paris
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

import fr.paris.lutece.plugins.workflow.modules.notifymylutece.util.constants.NotifyMyLuteceConstants;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * NotificationTypeEmail
 *
 */
public class NotificationTypeEmail extends AbstractNotificationType
{
    /**
     * {@inheritDoc}
     */
    public void notify( String strObject, String strMessage, String strSender, String strReceiver )
    {
        // Get the sender email
        String strSenderEmail = MailService.getNoReplyEmail(  );

        // Get the list of LuteceUser email attribute defined in workflow-notifymylutece.properties
        String strAttributeEmails = AppPropertiesService.getProperty( NotifyMyLuteceConstants.PROPERTY_LUTECEUSER_ATTRIBUTE_EMAIL );

        // Build the list of emails in which the message will be sent
        List<String> listEmails = new ArrayList<String>(  );

        if ( StringUtils.isNotBlank( strAttributeEmails ) )
        {
            String[] listAttributeEmails = strAttributeEmails.split( NotifyMyLuteceConstants.COMMA );

            LuteceUser user = SecurityService.getInstance(  ).getUser( strReceiver );

            if ( ( user != null ) && ( listAttributeEmails != null ) && ( listAttributeEmails.length > 0 ) )
            {
                for ( String strAttributeEmail : listAttributeEmails )
                {
                    listEmails.add( user.getUserInfo( strAttributeEmail.trim(  ) ) );
                }
            }
        }

        for ( String strEmail : listEmails )
        {
            if ( StringUtils.isNotBlank( strEmail ) )
            {
                // Build the mail message                
                MailService.sendMailHtml( strEmail, strSender, strSenderEmail, strObject, strMessage );
            }
        }
    }
}
