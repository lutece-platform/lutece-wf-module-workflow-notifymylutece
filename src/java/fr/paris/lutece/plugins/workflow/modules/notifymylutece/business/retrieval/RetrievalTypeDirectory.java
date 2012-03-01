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
package fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.retrieval;

import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.TaskNotifyMyLuteceConfig;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.NotifyMyLuteceService;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.util.constants.NotifyMyLuteceConstants;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * RetrievalTypeDirectory
 *
 */
public class RetrievalTypeDirectory extends AbstractRetrievalType
{
    /**
    * {@inheritDoc}
    */
    public List<String> getReceiver( TaskNotifyMyLuteceConfig config, int nIdRecord, int nIdDirectory )
    {
        List<String> listReceivers = new ArrayList<String>(  );
        listReceivers.add( NotifyMyLuteceService.getService(  ).getReceiver( config, nIdRecord, nIdDirectory ) );

        return listReceivers;
    }

    /**
     * {@inheritDoc}
     */
    public String checkConfigData( HttpServletRequest request )
    {
        // First check if the user has checked this retrieval type
        boolean bIsRetrievalTypeChecked = false;
        String[] listRetrievalTypes = request.getParameterValues( NotifyMyLuteceConstants.PARAMETER_RETRIEVAL_TYPE );

        if ( ( listRetrievalTypes != null ) && ( listRetrievalTypes.length > 0 ) )
        {
            for ( String strRetrievalType : listRetrievalTypes )
            {
                if ( StringUtils.isNotBlank( strRetrievalType ) && StringUtils.isNumeric( strRetrievalType ) )
                {
                    int nIdRetrievalType = Integer.parseInt( strRetrievalType );

                    if ( nIdRetrievalType == getIdType(  ) )
                    {
                        bIsRetrievalTypeChecked = true;
                    }
                }
            }
        }

        if ( bIsRetrievalTypeChecked )
        {
            String strPositionEntryDirectoryUserGuid = request.getParameter( NotifyMyLuteceConstants.PARAMETER_POSITION_ENTRY_DIRECTORY_USER_GUID );
            int nPositionEntryDirectoryUserGuid = DirectoryUtils.CONSTANT_ID_NULL;

            if ( StringUtils.isNotBlank( strPositionEntryDirectoryUserGuid ) &&
                    StringUtils.isNumeric( strPositionEntryDirectoryUserGuid ) )
            {
                nPositionEntryDirectoryUserGuid = Integer.parseInt( strPositionEntryDirectoryUserGuid );
            }

            if ( nPositionEntryDirectoryUserGuid == DirectoryUtils.CONSTANT_ID_NULL )
            {
                return NotifyMyLuteceConstants.PROPERTY_LABEL_POSITION_ENTRY_DIRECTORY_USER_GUID;
            }
        }

        return null;
    }
}
