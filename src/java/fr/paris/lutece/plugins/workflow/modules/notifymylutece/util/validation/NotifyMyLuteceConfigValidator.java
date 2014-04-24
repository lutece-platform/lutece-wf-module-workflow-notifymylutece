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
package fr.paris.lutece.plugins.workflow.modules.notifymylutece.util.validation;

import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.TaskNotifyMyLuteceConfig;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.retrieval.IRetrievalType;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.retrieval.IRetrievalTypeFactory;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.business.retrieval.RetrievalTypeFactory;
import fr.paris.lutece.plugins.workflow.modules.notifymylutece.util.annotation.NotifyMyLuteceConfig;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Map.Entry;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 *
 * Validator that checks the validity of the configuration.
 *
 */
public class NotifyMyLuteceConfigValidator implements ConstraintValidator<NotifyMyLuteceConfig, TaskNotifyMyLuteceConfig>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize( NotifyMyLuteceConfig constraintAnnotation )
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid( TaskNotifyMyLuteceConfig config, ConstraintValidatorContext context )
    {
        boolean bIsValid = true;
        IRetrievalTypeFactory retrievalTypeFactory = SpringContextService.getBean( RetrievalTypeFactory.BEAN_FACTORY );

        for ( Entry<String, IRetrievalType> entryRetrievalType : retrievalTypeFactory.getRetrievalTypes(  ).entrySet(  ) )
        {
            bIsValid = entryRetrievalType.getValue(  ).checkConfigData( config );

            if ( !bIsValid )
            {
                return false;
            }
        }

        return true;
    }
}
