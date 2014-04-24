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
package fr.paris.lutece.plugins.workflow.modules.notifymylutece.service;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.TaskNotifyMyLuteceConfig;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * INotifyMyLuteceService
 *
 */
public interface INotifyMyLuteceService
{
    // CHECKS

    /**
     * Check if the given entry type id is refused
     * @param nIdEntryType the id entry type
     * @return true if it is refused, false otherwise
     */
    boolean isEntryTypeRefused( int nIdEntryType );

    /**
     * Check if the given entry type id is accepted
     * @param nIdEntryType the id entry type
     * @return true if it is accepted, false otherwise
     */
    boolean isEntryTypeAccepted( int nIdEntryType );

    // GETS

    /**
     * Get the list of directorise
     * @return a ReferenceList
     */
    ReferenceList getListDirectories(  );

    /**
     * Get the list of entries that have the accepted type (which are defined in <b>workflow-notifymylutece.properties</b>)
     * @param nIdTask the id task
     * @param locale the Locale
     * @return a ReferenceList
     */
    ReferenceList getListEntries( int nIdTask, Locale locale );

    /**
     * Get the list of entries that have not the refused type (which are defined in the <b>workflow-notifymylutece.properties</b>)
     * @param nIdTask the id task
     * @return a list of IEntry
     */
    List<IEntry> getListEntries( int nIdTask );

    /**
     * Get the receiver
     * @param config the config
     * @param nIdRecord the id record
     * @param nIdDirectory the id directory
     * @return the receiver
     */
    String getReceiver( TaskNotifyMyLuteceConfig config, int nIdRecord, int nIdDirectory );

    /**
     * Get the list of tasks that have an order below the given reference task and that
     * have a ITaskProvider .
     * <br />
     * <b>Example</b> : The list of all tasks that has a provider :
     * <ul>
     * <li>Task1</li>
     * <li>Task2</li>
     * <li>Task3</li>
     * </ul>
     * If the reference task is Task1, the it returns nothing.
     * <br />
     * If the reference task is Task2, the it returns only Task1.
     * <br />
     * If the reference task is Task3, the it returns [Task1, Task2].
     * @param task the reference task
     * @param locale the locale
     * @return a list of {@link ITask}
     */
    List<ITask> getListBelowTasks( ITask task, Locale locale );

    /**
     * Get the list of available users
     * @param config the config
     * @return a {@link ReferenceList}
     */
    ReferenceList getAvailableUsers( TaskNotifyMyLuteceConfig config );

    /**
     * Get the list of selected users
     * @param config the config
     * @return a {@link ReferenceList}
     */
    ReferenceList getSelectedUsers( TaskNotifyMyLuteceConfig config );

    // OTHERS

    /**
     * Fill the model for the notification message
     * @param config the config
     * @param record the record
     * @param directory the directory
     * @param strReceiver the user guid of the receiver
     * @param request the HTTP requset
     * @param nIdAction the id action
     * @param nIdHistory the id history
     * @return the model
     */
    Map<String, Object> fillModel( TaskNotifyMyLuteceConfig config, Record record, Directory directory,
        String strReceiver, HttpServletRequest request, int nIdAction, int nIdHistory );

    /**
     * Send the message
     * @param request the HTTP request
     * @param nIdResourceHistory the id resource history
     * @param locale the locale
     * @param record the record
     * @param directory the directory
     * @param config the config
     * @param task the task
     */
    void sendMessage( HttpServletRequest request, int nIdResourceHistory, Locale locale, Record record,
        Directory directory, TaskNotifyMyLuteceConfig config, ITask task );
}
