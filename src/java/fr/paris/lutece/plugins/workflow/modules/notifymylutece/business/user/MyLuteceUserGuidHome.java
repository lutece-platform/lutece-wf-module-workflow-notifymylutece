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
package fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.user;

import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.NotifyMyLutecePlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * MyLuteceUserGuidHome
 *
 */
public final class MyLuteceUserGuidHome
{
    private static final String BEAN_MYLUTECE_USER_GUID_DAO = "workflow-notifymylutece.myLuteceUserGuidDAO";
    private static Plugin _plugin = PluginService.getPlugin( NotifyMyLutecePlugin.PLUGIN_NAME );
    private static IMyLuteceUserGuidDAO _dao = (IMyLuteceUserGuidDAO) SpringContextService.getPluginBean( NotifyMyLutecePlugin.PLUGIN_NAME,
            BEAN_MYLUTECE_USER_GUID_DAO );

    /**
     * Private constructor
     */
    private MyLuteceUserGuidHome(  )
    {
    }

    /**
     * Find the list of User guid associated to the task
     * @param nIdTask the id task
     * @return the list of User Guid
     */
    public static List<String> find( int nIdTask )
    {
        return _dao.load( nIdTask, _plugin );
    }

    /**
     * Create an association user guid - task
     * @param nIdTask the id task
     * @param strUserGuid the user guid
     */
    public static void create( int nIdTask, String strUserGuid )
    {
        _dao.insert( nIdTask, strUserGuid, _plugin );
    }

    /**
     * Remove the associations user guid - task
     * @param nIdTask the id task
     */
    public static void remove( int nIdTask )
    {
        _dao.delete( nIdTask, _plugin );
    }
}
