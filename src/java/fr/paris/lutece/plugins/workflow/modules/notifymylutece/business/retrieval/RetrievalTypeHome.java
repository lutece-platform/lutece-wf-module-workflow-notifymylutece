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

import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.NotifyMyLutecePlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * RetrievalTypeHome
 *
 */
public final class RetrievalTypeHome
{
    private static final String BEAN_RETRIEVAL_TYPE_DAO = "workflow-notifymylutece.retrievalTypeDAO";
    private static Plugin _plugin = PluginService.getPlugin( NotifyMyLutecePlugin.PLUGIN_NAME );
    private static IRetrievalTypeDAO _dao = (IRetrievalTypeDAO) SpringContextService.getPluginBean( NotifyMyLutecePlugin.PLUGIN_NAME,
            BEAN_RETRIEVAL_TYPE_DAO );

    /**
     * Private constructor
     */
    private RetrievalTypeHome(  )
    {
    }

    /**
     * Find the list of id retrieval type associated to the task
     * @param nIdTask the id task
     * @return a list of id retrieval type
     */
    public static List<Integer> find( int nIdTask )
    {
        return _dao.load( nIdTask, _plugin );
    }

    /**
     * Create an association retrieval type - task
     * @param nIdTask the id task
     * @param nIdRetrievalType the id retrieval type
     */
    public static void create( int nIdTask, int nIdRetrievalType )
    {
        _dao.insert( nIdTask, nIdRetrievalType, _plugin );
    }

    /**
     * Remove the associations retrieval type - task
     * @param nIdTask the id task
     */
    public static void remove( int nIdTask )
    {
        _dao.delete( nIdTask, _plugin );
    }
}
