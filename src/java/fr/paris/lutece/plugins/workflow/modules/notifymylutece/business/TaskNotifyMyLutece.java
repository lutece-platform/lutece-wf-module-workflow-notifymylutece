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
package fr.paris.lutece.plugins.workflow.modules.notifymylutece.business;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.workflow.business.ResourceHistory;
import fr.paris.lutece.plugins.workflow.business.ResourceHistoryHome;
import fr.paris.lutece.plugins.workflow.business.task.Task;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.notification.INotificationType;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.notification.NotificationTypeFactory;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.retrieval.IRetrievalType;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.retrieval.RetrievalTypeFactory;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.NotifyMyLuteceService;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.service.TaskNotifyMyLuteceConfigService;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.util.constants.NotifyMyLuteceConstants;
import fr.paris.lutece.plugins.workflow.service.WorkflowPlugin;
import fr.paris.lutece.plugins.workflow.service.security.WorkflowUserAttributesManager;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * TaskNotifyMyLutece
 *
 */
public class TaskNotifyMyLutece extends Task
{
    // TEMPLATES
    private static final String TEMPLATE_TASK_NOTIFY_MYLUTECE_CONFIG = "admin/plugins/workflow/modules/notifymylutece/task_notify_mylutece_config.html";
    private static final String TEMPLATE_TASK_NOTIFY_MYLUTECE_NOTIFICATION = "admin/plugins/workflow/modules/notifymylutece/task_notify_mylutece_notification.html";

    /**
     * {@inheritDoc}
     */
    public void init(  )
    {
    }

    /**
     * {@inheritDoc}
     */
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        ResourceHistory resourceHistory = ResourceHistoryHome.findByPrimaryKey( nIdResourceHistory, plugin );
        TaskNotifyMyLuteceConfig config = TaskNotifyMyLuteceConfigService.getService(  ).findByPrimaryKey( getId(  ) );

        if ( ( config != null ) && ( resourceHistory != null ) &&
                Record.WORKFLOW_RESOURCE_TYPE.equals( resourceHistory.getResourceType(  ) ) )
        {
            Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );

            // Record
            Record record = RecordHome.findByPrimaryKey( resourceHistory.getIdResource(  ), pluginDirectory );

            if ( record != null )
            {
                Directory directory = DirectoryHome.findByPrimaryKey( record.getDirectory(  ).getIdDirectory(  ),
                        pluginDirectory );

                if ( directory != null )
                {
                    record.setDirectory( directory );
                    sendMessage( request, nIdResourceHistory, locale, record, directory, config );
                }
            }
        }
    }

    // GET

    /**
     * {@inheritDoc}
     */
    public String getDisplayConfigForm( HttpServletRequest request, Plugin plugin, Locale locale )
    {
        NotifyMyLuteceService notifyMyLuteceService = NotifyMyLuteceService.getService(  );
        TaskNotifyMyLuteceConfigService configService = TaskNotifyMyLuteceConfigService.getService(  );
        TaskNotifyMyLuteceConfig config = configService.findByPrimaryKey( getId(  ) );

        String strDefaultSenderName = AppPropertiesService.getProperty( NotifyMyLuteceConstants.PROPERTY_DEFAULT_SENDER_NAME );
        Plugin pluginWorkflow = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );

        Map<String, Object> model = new HashMap<String, Object>(  );

        model.put( NotifyMyLuteceConstants.MARK_CONFIG, config );
        model.put( NotifyMyLuteceConstants.MARK_DEFAULT_SENDER_NAME, strDefaultSenderName );
        model.put( NotifyMyLuteceConstants.MARK_LIST_ENTRIES, notifyMyLuteceService.getListEntries( getId(  ), locale ) );
        model.put( NotifyMyLuteceConstants.MARK_LIST_DIRECTORIES, notifyMyLuteceService.getListDirectories(  ) );
        model.put( NotifyMyLuteceConstants.MARK_LIST_ENTRIES_FREEMARKER,
            notifyMyLuteceService.getListEntries( getId(  ) ) );
        model.put( NotifyMyLuteceConstants.MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( NotifyMyLuteceConstants.MARK_LOCALE, locale );
        model.put( NotifyMyLuteceConstants.MARK_PLUGIN_WORKFLOW, pluginWorkflow );
        model.put( NotifyMyLuteceConstants.MARK_TASKS_LIST,
            notifyMyLuteceService.getListTasks( getAction(  ).getId(  ), locale ) );
        model.put( NotifyMyLuteceConstants.MARK_IS_USER_ATTRIBUTE_WS_ACTIVE,
            WorkflowUserAttributesManager.getManager(  ).isEnabled(  ) );
        model.put( NotifyMyLuteceConstants.MARK_NOTIFICATION_TYPES,
            NotificationTypeFactory.getFactory(  ).getNotificationTypes(  ) );
        model.put( NotifyMyLuteceConstants.MARK_RETRIEVAL_TYPES,
            RetrievalTypeFactory.getFactory(  ).getRetrievalTypes(  ) );
        model.put( NotifyMyLuteceConstants.MARK_AVAILABLE_USERS_LIST, notifyMyLuteceService.getAvailableUsers( config ) );
        model.put( NotifyMyLuteceConstants.MARK_SELECTED_USERS_LIST, notifyMyLuteceService.getSelectedUsers( config ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFY_MYLUTECE_CONFIG, locale, model );

        return template.getHtml(  );
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayTaskForm( int nIdResource, String strResourceType, HttpServletRequest request,
        Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ReferenceList getTaskFormEntries( Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Plugin plugin, Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getTitle( Plugin plugin, Locale locale )
    {
        String strTitle = StringUtils.EMPTY;
        TaskNotifyMyLuteceConfig config = TaskNotifyMyLuteceConfigService.getService(  ).findByPrimaryKey( getId(  ) );

        if ( config != null )
        {
            strTitle = config.getSubject(  );
        }

        return StringUtils.isNotBlank( strTitle ) ? strTitle : StringUtils.EMPTY;
    }

    // DO

    /**
     * {@inheritDoc}
     */
    public void doRemoveConfig( Plugin plugin )
    {
        TaskNotifyMyLuteceConfigService.getService(  ).remove( getId(  ) );
    }

    /**
     * {@inheritDoc}
     */
    public void doRemoveTaskInformation( int nIdHistory, Plugin plugin )
    {
    }

    /**
     * {@inheritDoc}
     */
    public String doSaveConfig( HttpServletRequest request, Locale locale, Plugin plugin )
    {
        String strError = checkConfigData( request, locale );

        if ( StringUtils.isBlank( strError ) )
        {
            setConfigData( request );
        }

        return strError;
    }

    /**
     * {@inheritDoc}
     */
    public String doValidateTask( int nIdResource, String strResourceType, HttpServletRequest request, Locale locale,
        Plugin plugin )
    {
        return null;
    }

    // CHECK

    /**
     * {@inheritDoc}
     */
    public boolean isConfigRequire(  )
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isFormTaskRequire(  )
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTaskForActionAutomatic(  )
    {
        return true;
    }

    // PRIVATE METHODS

    /**
     * Check the config data
     * @param request the HTTP request
     * @param locale the locale
     * @return error message if there is an error
     */
    private String checkConfigData( HttpServletRequest request, Locale locale )
    {
        String strError = null;

        // Fetch parameters
        String strIdDirectory = request.getParameter( NotifyMyLuteceConstants.PARAMETER_ID_DIRECTORY );
        String strSenderName = request.getParameter( NotifyMyLuteceConstants.PARAMETER_SENDER_NAME );
        String strSubject = request.getParameter( NotifyMyLuteceConstants.PARAMETER_SUBJECT );
        String strMessage = request.getParameter( NotifyMyLuteceConstants.PARAMETER_MESSAGE );
        String strApply = request.getParameter( NotifyMyLuteceConstants.PARAMETER_APPLY );
        String[] listNotificationTypes = request.getParameterValues( NotifyMyLuteceConstants.PARAMETER_NOTIFICATION_TYPE );
        String[] listRetrievalTypes = request.getParameterValues( NotifyMyLuteceConstants.PARAMETER_RETRIEVAL_TYPE );

        // Check if the AdminUser clicked on "Apply" or on "Save"
        if ( StringUtils.isEmpty( strApply ) )
        {
            int nIdDirectory = DirectoryUtils.CONSTANT_ID_NULL;

            if ( StringUtils.isNotBlank( strIdDirectory ) && StringUtils.isNumeric( strIdDirectory ) )
            {
                nIdDirectory = Integer.parseInt( strIdDirectory );
            }

            // Check the required fields
            String strRequiredField = StringUtils.EMPTY;

            if ( nIdDirectory == DirectoryUtils.CONSTANT_ID_NULL )
            {
                strRequiredField = NotifyMyLuteceConstants.PROPERTY_LABEL_DIRECTORY;
            }
            else if ( StringUtils.isBlank( strSenderName ) )
            {
                strRequiredField = NotifyMyLuteceConstants.PROPERTY_LABEL_SENDER_NAME;
            }
            else if ( StringUtils.isBlank( strSubject ) )
            {
                strRequiredField = NotifyMyLuteceConstants.PROPERTY_LABEL_SUBJECT;
            }
            else if ( StringUtils.isBlank( strMessage ) )
            {
                strRequiredField = NotifyMyLuteceConstants.PROPERTY_LABEL_MESSAGE;
            }
            else if ( ( listNotificationTypes == null ) || ( listNotificationTypes.length == 0 ) )
            {
                strRequiredField = NotifyMyLuteceConstants.PROPERTY_LABEL_NOTIFICATION_TYPE;
            }
            else if ( ( listRetrievalTypes == null ) || ( listRetrievalTypes.length == 0 ) )
            {
                strRequiredField = NotifyMyLuteceConstants.PROPERTY_LABEL_RETRIEVAL_TYPE;
            }

            if ( StringUtils.isBlank( strRequiredField ) )
            {
                for ( Entry<String, IRetrievalType> entryRetrievalType : RetrievalTypeFactory.getFactory(  )
                                                                                             .getRetrievalTypes(  )
                                                                                             .entrySet(  ) )
                {
                    strRequiredField = entryRetrievalType.getValue(  ).checkConfigData( request );

                    if ( strRequiredField != null )
                    {
                        break;
                    }
                }
            }

            if ( StringUtils.isNotBlank( strRequiredField ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( strRequiredField, locale ) };
                strError = AdminMessageService.getMessageUrl( request, NotifyMyLuteceConstants.MESSAGE_MANDATORY_FIELD,
                        tabRequiredFields, AdminMessage.TYPE_STOP );
            }
        }

        return strError;
    }

    /**
     * Set the config data
     * @param request the HTTP request
     */
    private void setConfigData( HttpServletRequest request )
    {
        TaskNotifyMyLuteceConfigService configService = TaskNotifyMyLuteceConfigService.getService(  );

        // Fetch parameters
        String strIdDirectory = request.getParameter( NotifyMyLuteceConstants.PARAMETER_ID_DIRECTORY );
        String strPositionEntryDirectoryUserGuid = request.getParameter( NotifyMyLuteceConstants.PARAMETER_POSITION_ENTRY_DIRECTORY_USER_GUID );
        String strSenderName = request.getParameter( NotifyMyLuteceConstants.PARAMETER_SENDER_NAME );
        String strSubject = request.getParameter( NotifyMyLuteceConstants.PARAMETER_SUBJECT );
        String strMessage = request.getParameter( NotifyMyLuteceConstants.PARAMETER_MESSAGE );
        String[] listNotificationTypes = request.getParameterValues( NotifyMyLuteceConstants.PARAMETER_NOTIFICATION_TYPE );
        String[] listRetrievalTypes = request.getParameterValues( NotifyMyLuteceConstants.PARAMETER_RETRIEVAL_TYPE );

        int nIdDirectory = DirectoryUtils.CONSTANT_ID_NULL;
        int nPositionEntryDirectoryUserGuid = DirectoryUtils.CONSTANT_ID_NULL;

        if ( StringUtils.isNotBlank( strIdDirectory ) && StringUtils.isNumeric( strIdDirectory ) )
        {
            nIdDirectory = Integer.parseInt( strIdDirectory );
        }

        if ( StringUtils.isNotBlank( strPositionEntryDirectoryUserGuid ) &&
                StringUtils.isNumeric( strPositionEntryDirectoryUserGuid ) )
        {
            nPositionEntryDirectoryUserGuid = Integer.parseInt( strPositionEntryDirectoryUserGuid );
        }

        List<Integer> listIdsNotificationType = new ArrayList<Integer>(  );

        if ( ( listNotificationTypes != null ) && ( listNotificationTypes.length > 0 ) )
        {
            for ( String strNotificationType : listNotificationTypes )
            {
                if ( StringUtils.isNotBlank( strNotificationType ) && StringUtils.isNumeric( strNotificationType ) )
                {
                    int nIdNotificationType = Integer.parseInt( strNotificationType );
                    listIdsNotificationType.add( nIdNotificationType );
                }
            }
        }

        List<Integer> listIdsRetrievalType = new ArrayList<Integer>(  );

        if ( ( listRetrievalTypes != null ) && ( listRetrievalTypes.length > 0 ) )
        {
            for ( String strRetrievalType : listRetrievalTypes )
            {
                if ( StringUtils.isNotBlank( strRetrievalType ) && StringUtils.isNumeric( strRetrievalType ) )
                {
                    int nIdRetrievalType = Integer.parseInt( strRetrievalType );
                    listIdsRetrievalType.add( nIdRetrievalType );
                }
            }
        }

        // In case there are no errors, then the config is created/updated
        boolean bCreate = false;
        TaskNotifyMyLuteceConfig config = configService.findByPrimaryKey( getId(  ) );

        if ( config == null )
        {
            config = new TaskNotifyMyLuteceConfig(  );
            config.setIdTask( getId(  ) );
            bCreate = true;
        }

        config.setIdDirectory( nIdDirectory );
        config.setPositionEntryDirectoryUserGuid( nPositionEntryDirectoryUserGuid );
        config.setMessage( StringUtils.isNotBlank( strMessage ) ? strMessage : StringUtils.EMPTY );
        config.setSenderName( StringUtils.isNotBlank( strSenderName ) ? strSenderName : StringUtils.EMPTY );
        config.setSubject( StringUtils.isNotBlank( strSubject ) ? strSubject : StringUtils.EMPTY );
        config.setListIdsNotificationType( listIdsNotificationType );
        config.setListIdsRetrievalType( listIdsRetrievalType );
        config.setListUserGuid( getSelectedUsers( request, config ) );

        if ( bCreate )
        {
            configService.create( config );
        }
        else
        {
            configService.update( config );
        }
    }

    /**
     * Get the selected users
     * @param request the HTTP request
     * @param config the config
     * @return a list of User Guid
     */
    private List<String> getSelectedUsers( HttpServletRequest request, TaskNotifyMyLuteceConfig config )
    {
        // Init the list of user guid
        List<String> listUserGuid = config.getListUserGuid(  );

        if ( listUserGuid == null )
        {
            listUserGuid = new ArrayList<String>(  );
        }

        // Remove unselected users from the list
        String[] listUnselectedUsers = request.getParameterValues( NotifyMyLuteceConstants.PARAMETER_UNSELECT_USERS );

        if ( ( listUnselectedUsers != null ) && ( listUnselectedUsers.length > 0 ) )
        {
            if ( ( listUserGuid != null ) && !listUserGuid.isEmpty(  ) )
            {
                for ( String strUserGuid : listUnselectedUsers )
                {
                    listUserGuid.remove( strUserGuid );
                }
            }
        }

        // Add selected users
        String[] listSelectedUsers = request.getParameterValues( NotifyMyLuteceConstants.PARAMETER_SELECT_USERS );

        if ( ( listSelectedUsers != null ) && ( listSelectedUsers.length > 0 ) )
        {
            for ( String strUserGuid : listSelectedUsers )
            {
                listUserGuid.add( strUserGuid );
            }
        }

        return listUserGuid;
    }

    /**
     * Send the message
     * @param request the HTTP request
     * @param nIdResourceHistory the id resource history
     * @param locale the locale
     * @param record the record
     * @param directory the directory
     * @param config the config
     */
    private void sendMessage( HttpServletRequest request, int nIdResourceHistory, Locale locale, Record record,
        Directory directory, TaskNotifyMyLuteceConfig config )
    {
        NotifyMyLuteceService notifyMyLuteceService = NotifyMyLuteceService.getService(  );

        for ( IRetrievalType retrievalType : config.getRetrievalTypes(  ) )
        {
            for ( String strReceiver : retrievalType.getReceiver( config, record.getIdRecord(  ),
                    directory.getIdDirectory(  ) ) )
            {
                Map<String, Object> model = notifyMyLuteceService.fillModel( config, record, directory, strReceiver,
                        request, getAction(  ).getId(  ), nIdResourceHistory );
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
}
