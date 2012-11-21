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

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.EntryTypeGeolocation;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldFilter;
import fr.paris.lutece.plugins.directory.business.RecordFieldHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.TaskNotifyMyLuteceConfig;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.notification.INotificationType;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.retrieval.IRetrievalType;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.util.constants.NotifyMyLuteceConstants;
import fr.paris.lutece.plugins.workflow.service.security.IWorkflowUserAttributesManager;
import fr.paris.lutece.plugins.workflow.service.taskinfo.ITaskInfoProvider;
import fr.paris.lutece.plugins.workflow.service.taskinfo.TaskInfoManager;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.plugins.workflowcore.service.task.ITaskService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * NotifyMyLuteceService
 *
 */
public final class NotifyMyLuteceService implements INotifyMyLuteceService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "workflow-notifymylutece.notifyMyLuteceService";
    private static final String TEMPLATE_TASK_NOTIFY_MYLUTECE_NOTIFICATION = "admin/plugins/workflow/modules/notifymylutece/task_notify_mylutece_notification.html";
    private List<Integer> _listRefusedEntryTypes;
    private List<Integer> _listAcceptedEntryTypes;

    // SERVICES
    @Inject
    @Named( TaskNotifyMyLuteceConfigService.BEAN_SERVICE )
    private ITaskConfigService _taskNotifyMyLuteceConfigService;
    @Inject
    private ITaskService _taskService;
    @Inject
    private IWorkflowUserAttributesManager _userAttributesManager;

    /**
     * Private constructor
     */
    private NotifyMyLuteceService(  )
    {
        // Init list accepted entry types
        _listAcceptedEntryTypes = fillListEntryTypes( NotifyMyLuteceConstants.PROPERTY_ACCEPTED_DIRECTORY_ENTRY_TYPE );

        // Init list refused entry types
        _listRefusedEntryTypes = fillListEntryTypes( NotifyMyLuteceConstants.PROPERTY_REFUSED_DIRECTORY_ENTRY_TYPE );
    }

    // CHECKS

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEntryTypeRefused( int nIdEntryType )
    {
        boolean bIsRefused = true;

        if ( ( _listRefusedEntryTypes != null ) && !_listRefusedEntryTypes.isEmpty(  ) )
        {
            bIsRefused = _listRefusedEntryTypes.contains( nIdEntryType );
        }

        return bIsRefused;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEntryTypeAccepted( int nIdEntryType )
    {
        boolean bIsAccepted = false;

        if ( ( _listAcceptedEntryTypes != null ) && !_listAcceptedEntryTypes.isEmpty(  ) )
        {
            bIsAccepted = _listAcceptedEntryTypes.contains( nIdEntryType );
        }

        return bIsAccepted;
    }

    // GETS

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getListDirectories(  )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        ReferenceList listDirectories = DirectoryHome.getDirectoryList( pluginDirectory );
        ReferenceList refenreceListDirectories = new ReferenceList(  );
        refenreceListDirectories.addItem( DirectoryUtils.CONSTANT_ID_NULL, StringUtils.EMPTY );

        if ( listDirectories != null )
        {
            refenreceListDirectories.addAll( listDirectories );
        }

        return refenreceListDirectories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getListEntries( int nIdTask, Locale locale )
    {
        TaskNotifyMyLuteceConfig config = _taskNotifyMyLuteceConfigService.findByPrimaryKey( nIdTask );
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        ReferenceList refenreceListEntries = new ReferenceList(  );
        refenreceListEntries.addItem( DirectoryUtils.CONSTANT_ID_NULL, DirectoryUtils.EMPTY_STRING );

        if ( config != null )
        {
            EntryFilter entryFilter = new EntryFilter(  );
            entryFilter.setIdDirectory( config.getIdDirectory(  ) );
            entryFilter.setIsGroup( EntryFilter.FILTER_FALSE );
            entryFilter.setIsComment( EntryFilter.FILTER_FALSE );

            for ( IEntry entry : EntryHome.getEntryList( entryFilter, pluginDirectory ) )
            {
                int nIdEntryType = entry.getEntryType(  ).getIdType(  );

                if ( isEntryTypeAccepted( nIdEntryType ) )
                {
                    refenreceListEntries.addItem( entry.getPosition(  ), buildReferenceEntryToString( entry, locale ) );
                }
            }
        }

        return refenreceListEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IEntry> getListEntries( int nIdTask )
    {
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        TaskNotifyMyLuteceConfig config = _taskNotifyMyLuteceConfigService.findByPrimaryKey( nIdTask );

        List<IEntry> listEntries = new ArrayList<IEntry>(  );

        if ( config != null )
        {
            EntryFilter entryFilter = new EntryFilter(  );
            entryFilter.setIdDirectory( config.getIdDirectory(  ) );

            for ( IEntry entry : EntryHome.getEntryList( entryFilter, pluginDirectory ) )
            {
                int nIdEntryType = entry.getEntryType(  ).getIdType(  );

                if ( !isEntryTypeRefused( nIdEntryType ) )
                {
                    listEntries.add( entry );
                }
            }
        }

        return listEntries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReceiver( TaskNotifyMyLuteceConfig config, int nIdRecord, int nIdDirectory )
    {
        String strReceiver = StringUtils.EMPTY;

        if ( config.getPositionEntryDirectoryUserGuid(  ) != DirectoryUtils.CONSTANT_ID_NULL )
        {
            strReceiver = getRecordFieldValue( config.getPositionEntryDirectoryUserGuid(  ), nIdRecord, nIdDirectory );
        }

        return strReceiver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ITask> getListBelowTasks( ITask task, Locale locale )
    {
        List<ITask> listTasks = new ArrayList<ITask>(  );

        if ( task != null )
        {
            for ( ITask otherTask : _taskService.getListTaskByIdAction( task.getAction(  ).getId(  ), locale ) )
            {
                // FIXME : When upgrading to workflow v3.0.2, change this condition to :
                // if ( task.getOrder(  ) <= otherTasK.getOrder(  ) )
                // Indeed, in workflow v3.0.1 and inferior, the task are ordered by id task
                if ( task.getId(  ) == otherTask.getId(  ) )
                {
                    break;
                }

                for ( ITaskInfoProvider provider : TaskInfoManager.getProvidersList(  ) )
                {
                    if ( otherTask.getTaskType(  ).getKey(  ).equals( provider.getTaskType(  ).getKey(  ) ) )
                    {
                        listTasks.add( otherTask );

                        break;
                    }
                }
            }
        }

        return listTasks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getAvailableUsers( TaskNotifyMyLuteceConfig config )
    {
        ReferenceList refAvailableUser = new ReferenceList(  );

        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            for ( LuteceUser user : SecurityService.getInstance(  ).getUsers(  ) )
            {
                if ( ( user != null ) &&
                        ( ( config == null ) || ( config.getListUserGuid(  ) == null ) ||
                        !Arrays.asList( config.getListUserGuid(  ) ).contains( user.getName(  ) ) ) )
                {
                    refAvailableUser.addItem( user.getName(  ), user.getName(  ) );
                }
            }
        }

        return refAvailableUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getSelectedUsers( TaskNotifyMyLuteceConfig config )
    {
        ReferenceList refSelectedUsers = new ReferenceList(  );

        if ( ( config != null ) && ( config.getListUserGuid(  ) != null ) )
        {
            for ( String strUserGuid : config.getListUserGuid(  ) )
            {
                refSelectedUsers.addItem( strUserGuid, strUserGuid );
            }
        }

        return refSelectedUsers;
    }

    // OTHERS

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> fillModel( TaskNotifyMyLuteceConfig config, Record record, Directory directory,
        String strReceiver, HttpServletRequest request, int nIdAction, int nIdHistory )
    {
        Locale locale = getLocale( request );
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( NotifyMyLuteceConstants.MARK_MESSAGE, config.getMessage(  ) );
        model.put( NotifyMyLuteceConstants.MARK_DIRECTORY_TITLE, directory.getTitle(  ) );
        model.put( NotifyMyLuteceConstants.MARK_DIRECTORY_DESCRIPTION, directory.getDescription(  ) );

        RecordFieldFilter recordFieldFilter = new RecordFieldFilter(  );
        recordFieldFilter.setIdRecord( record.getIdRecord(  ) );

        List<RecordField> listRecordField = RecordFieldHome.getRecordFieldList( recordFieldFilter, pluginDirectory );

        for ( RecordField recordField : listRecordField )
        {
            String strNewValue = StringUtils.EMPTY;

            if ( isEntryTypeRefused( recordField.getEntry(  ).getEntryType(  ).getIdType(  ) ) )
            {
                continue;
            }
            else if ( recordField.getEntry(  ) instanceof fr.paris.lutece.plugins.directory.business.EntryTypeGeolocation &&
                    ( ( recordField.getField(  ) == null ) ||
                    StringUtils.isBlank( recordField.getField(  ).getTitle(  ) ) ||
                    !EntryTypeGeolocation.CONSTANT_ADDRESS.equals( recordField.getField(  ).getTitle(  ) ) ) )
            {
                continue;
            }
            else if ( ( recordField.getField(  ) != null ) && ( recordField.getField(  ).getTitle(  ) != null ) &&
                    !( recordField.getEntry(  ) instanceof fr.paris.lutece.plugins.directory.business.EntryTypeGeolocation ) )
            {
                strNewValue = recordField.getField(  ).getTitle(  );
            }
            else if ( recordField.getEntry(  ) instanceof fr.paris.lutece.plugins.directory.business.EntryTypeFile &&
                    ( recordField.getFile(  ) != null ) && ( recordField.getFile(  ).getTitle(  ) != null ) )
            {
                strNewValue = recordField.getFile(  ).getTitle(  );
            }
            else
            {
                strNewValue = recordField.getEntry(  ).convertRecordFieldValueToString( recordField, locale, false,
                        false );
            }

            recordField.setEntry( EntryHome.findByPrimaryKey( recordField.getEntry(  ).getIdEntry(  ), pluginDirectory ) );

            String strKey = NotifyMyLuteceConstants.MARK_POSITION + recordField.getEntry(  ).getPosition(  );
            String strOldValue = ( (String) model.get( strKey ) );

            if ( StringUtils.isNotBlank( strOldValue ) && StringUtils.isNotBlank( strNewValue ) )
            {
                // Add markers for message
                model.put( strKey, strNewValue + NotifyMyLuteceConstants.COMMA + strOldValue );
            }
            else if ( strNewValue != null )
            {
                model.put( strKey, strNewValue );
            }
            else
            {
                model.put( strKey, StringUtils.EMPTY );
            }
        }

        if ( ( directory.getIdWorkflow(  ) != DirectoryUtils.CONSTANT_ID_NULL ) &&
                WorkflowService.getInstance(  ).isAvailable(  ) )
        {
            State state = WorkflowService.getInstance(  )
                                         .getState( record.getIdRecord(  ), Record.WORKFLOW_RESOURCE_TYPE,
                    directory.getIdWorkflow(  ), null );
            model.put( NotifyMyLuteceConstants.MARK_STATUS, state.getName(  ) );
        }

        // Fills the model for user attributes
        fillModelWithUserAttributes( model, strReceiver );

        ITask task = _taskService.findByPrimaryKey( config.getIdTask(  ), locale );

        // Fill the model with the info of other tasks
        for ( ITask otherTask : getListBelowTasks( task, locale ) )
        {
            model.put( NotifyMyLuteceConstants.MARK_TASK + otherTask.getId(  ),
                TaskInfoManager.getTaskResourceInfo( nIdHistory, otherTask.getId(  ), request ) );
        }

        return model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage( HttpServletRequest request, int nIdResourceHistory, Locale locale, Record record,
        Directory directory, TaskNotifyMyLuteceConfig config, ITask task )
    {
        for ( IRetrievalType retrievalType : config.getRetrievalTypes(  ) )
        {
            for ( String strReceiver : retrievalType.getReceiver( config, record.getIdRecord(  ),
                    directory.getIdDirectory(  ) ) )
            {
                Map<String, Object> model = fillModel( config, record, directory, strReceiver, request,
                        task.getAction(  ).getId(  ), nIdResourceHistory );
                HtmlTemplate template = AppTemplateService.getTemplateFromStringFtl( AppTemplateService.getTemplate( 
                            TEMPLATE_TASK_NOTIFY_MYLUTECE_NOTIFICATION, locale, model ).getHtml(  ), locale, model );

                String strObject = AppTemplateService.getTemplateFromStringFtl( config.getSubject(  ), locale, model )
                                                     .getHtml(  );
                String strMessage = template.getHtml(  );
                String strSender = config.getSenderName(  );

                for ( INotificationType notificationType : config.getNotificationTypes(  ) )
                {
                    notificationType.notify( strObject, strMessage, strSender, strReceiver );
                }
            }
        }
    }

    // PRIVATE METHODS

    /**
     * Get the record field value
     * @param nPosition the position of the entry
     * @param nIdRecord the id record
     * @param nIdDirectory the id directory
     * @return the record field value
     */
    private String getRecordFieldValue( int nPosition, int nIdRecord, int nIdDirectory )
    {
        String strRecordFieldValue = StringUtils.EMPTY;
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

        // RecordField
        EntryFilter entryFilter = new EntryFilter(  );
        entryFilter.setPosition( nPosition );
        entryFilter.setIdDirectory( nIdDirectory );

        List<IEntry> listEntries = EntryHome.getEntryList( entryFilter, pluginDirectory );

        if ( ( listEntries != null ) && !listEntries.isEmpty(  ) )
        {
            IEntry entry = listEntries.get( 0 );
            RecordFieldFilter recordFieldFilterEmail = new RecordFieldFilter(  );
            recordFieldFilterEmail.setIdDirectory( nIdDirectory );
            recordFieldFilterEmail.setIdEntry( entry.getIdEntry(  ) );
            recordFieldFilterEmail.setIdRecord( nIdRecord );

            List<RecordField> listRecordFields = RecordFieldHome.getRecordFieldList( recordFieldFilterEmail,
                    pluginDirectory );

            if ( ( listRecordFields != null ) && !listRecordFields.isEmpty(  ) && ( listRecordFields.get( 0 ) != null ) )
            {
                RecordField recordFieldIdDemand = listRecordFields.get( 0 );
                strRecordFieldValue = recordFieldIdDemand.getValue(  );

                if ( recordFieldIdDemand.getField(  ) != null )
                {
                    strRecordFieldValue = recordFieldIdDemand.getField(  ).getTitle(  );
                }
            }
        }

        return strRecordFieldValue;
    }

    /**
     * Get the locale
     * @param request the HTTP request
     * @return the locale
     */
    private Locale getLocale( HttpServletRequest request )
    {
        Locale locale = null;

        if ( request != null )
        {
            locale = request.getLocale(  );
        }
        else
        {
            locale = I18nService.getDefaultLocale(  );
        }

        return locale;
    }

    /**
     * Fills the model with user attributes
     * @param model the model
     * @param strUserGuid the user guid
     */
    private void fillModelWithUserAttributes( Map<String, Object> model, String strUserGuid )
    {
        if ( _userAttributesManager.isEnabled(  ) && StringUtils.isNotBlank( strUserGuid ) )
        {
            Map<String, String> mapUserAttributes = _userAttributesManager.getAttributes( strUserGuid );
            String strFirstName = mapUserAttributes.get( LuteceUser.NAME_GIVEN );
            String strLastName = mapUserAttributes.get( LuteceUser.NAME_FAMILY );
            String strEmail = mapUserAttributes.get( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL );
            String strPhoneNumber = mapUserAttributes.get( LuteceUser.BUSINESS_INFO_TELECOM_TELEPHONE_NUMBER );

            model.put( NotifyMyLuteceConstants.MARK_FIRST_NAME,
                StringUtils.isNotEmpty( strFirstName ) ? strFirstName : StringUtils.EMPTY );
            model.put( NotifyMyLuteceConstants.MARK_LAST_NAME,
                StringUtils.isNotEmpty( strLastName ) ? strLastName : StringUtils.EMPTY );
            model.put( NotifyMyLuteceConstants.MARK_EMAIL,
                StringUtils.isNotEmpty( strEmail ) ? strEmail : StringUtils.EMPTY );
            model.put( NotifyMyLuteceConstants.MARK_PHONE_NUMBER,
                StringUtils.isNotEmpty( strPhoneNumber ) ? strPhoneNumber : StringUtils.EMPTY );
        }
    }

    /**
     * Build the reference entry into String
     * @param entry the entry
     * @param locale the Locale
     * @return the reference entry
     */
    private String buildReferenceEntryToString( IEntry entry, Locale locale )
    {
        StringBuilder sbReferenceEntry = new StringBuilder(  );
        sbReferenceEntry.append( entry.getPosition(  ) );
        sbReferenceEntry.append( NotifyMyLuteceConstants.SPACE + NotifyMyLuteceConstants.OPEN_BRACKET );
        sbReferenceEntry.append( entry.getTitle(  ) );
        sbReferenceEntry.append( NotifyMyLuteceConstants.SPACE + NotifyMyLuteceConstants.HYPHEN +
            NotifyMyLuteceConstants.SPACE );
        sbReferenceEntry.append( I18nService.getLocalizedString( entry.getEntryType(  ).getTitleI18nKey(  ), locale ) );
        sbReferenceEntry.append( NotifyMyLuteceConstants.CLOSED_BRACKET );

        return sbReferenceEntry.toString(  );
    }

    /**
     * Fill the list of entry types
     * @param strPropertyEntryTypes the property containing the entry types
     * @return a list of integer
     */
    private static List<Integer> fillListEntryTypes( String strPropertyEntryTypes )
    {
        List<Integer> listEntryTypes = new ArrayList<Integer>(  );
        String strEntryTypes = AppPropertiesService.getProperty( strPropertyEntryTypes );

        if ( StringUtils.isNotBlank( strEntryTypes ) )
        {
            String[] listAcceptEntryTypesForIdDemand = strEntryTypes.split( NotifyMyLuteceConstants.COMMA );

            for ( String strAcceptEntryType : listAcceptEntryTypesForIdDemand )
            {
                if ( StringUtils.isNotBlank( strAcceptEntryType ) && StringUtils.isNumeric( strAcceptEntryType ) )
                {
                    int nAcceptedEntryType = Integer.parseInt( strAcceptEntryType );
                    listEntryTypes.add( nAcceptedEntryType );
                }
            }
        }

        return listEntryTypes;
    }
}
