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

import fr.paris.lutece.portal.service.i18n.I18nService;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;


/**
 *
 * AbstractNotificationType
 *
 */
public abstract class AbstractNotificationType implements INotificationType
{
    private int _nIdType;
    private String _strTitleKey;
    private String _strCommentKey;

    /**
     * {@inheritDoc}
     */
    public void setIdType( int nIdType )
    {
        _nIdType = nIdType;
    }

    /**
     * {@inheritDoc}
     */
    public int getIdType(  )
    {
        return _nIdType;
    }

    /**
     * {@inheritDoc}
     */
    public void setTitleKey( String strTitleKey )
    {
        _strTitleKey = strTitleKey;
    }

    /**
     * {@inheritDoc}
     */
    public String getTitleKey(  )
    {
        return _strTitleKey;
    }

    /**
     * {@inheritDoc}
     */
    public String getTitle( Locale locale )
    {
        if ( StringUtils.isNotBlank( _strTitleKey ) && ( locale != null ) )
        {
            return I18nService.getLocalizedString( _strTitleKey, locale );
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    public void setCommentKey( String strCommentKey )
    {
        _strCommentKey = strCommentKey;
    }

    /**
    * {@inheritDoc}
    */
    public String getCommentKey(  )
    {
        return _strCommentKey;
    }

    /**
    * {@inheritDoc}
    */
    public String getComment( Locale locale )
    {
        if ( StringUtils.isNotBlank( _strCommentKey ) && ( locale != null ) )
        {
            return I18nService.getLocalizedString( _strCommentKey, locale );
        }

        return StringUtils.EMPTY;
    }
}
