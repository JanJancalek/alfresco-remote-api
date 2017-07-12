/*
 * #%L
 * Alfresco Remote API
 * %%
 * Copyright (C) 2005 - 2016 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software. 
 * If the software was purchased under a paid Alfresco license, the terms of 
 * the paid license agreement will prevail.  Otherwise, the software is 
 * provided under the following open source license terms:
 * 
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.alfresco.rest.api.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.alfresco.rest.AbstractSingleNetworkSiteTest;
import org.alfresco.rest.api.tests.client.PublicApiClient;
import org.alfresco.rest.api.tests.client.PublicApiClient.AuditApps;
import org.alfresco.rest.api.tests.client.PublicApiClient.ListResponse;
import org.alfresco.rest.api.tests.client.PublicApiClient.Paging;
import org.alfresco.rest.api.tests.client.data.AuditApp;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.PermissionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuditAppTest extends AbstractSingleNetworkSiteTest
{

    protected PermissionService permissionService;
    protected AuthorityService authorityService;

    @Before
    public void setup() throws Exception
    {
        super.setup();

        permissionService = applicationContext.getBean("permissionService", PermissionService.class);
        authorityService = (AuthorityService) applicationContext.getBean("AuthorityService");
    }

    @After
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Test
    public void testGetAuditApp() throws Exception
    {
        try
        {

            setRequestContext(DEFAULT_ADMIN);
            testGetAuditAppSkipPaging();

        }
        finally
        {

        }
    }

    private void testGetAuditAppSkipPaging() throws Exception
    {
        // +ve: check skip count.
        {

            // Paging and list auditApp

            int skipCount = 0;
            int maxItems = 4;
            Paging paging = getPaging(skipCount, maxItems);

            ListResponse<AuditApp> resp = getAuditApps(paging);

            // Paging and list groups with skip count.

            skipCount = 2;
            maxItems = 2;
            paging = getPaging(skipCount, maxItems);

            ListResponse<AuditApp> sublistResponse = getAuditApps(paging);

            List<AuditApp> expectedSublist = sublist(resp.getList(), skipCount, maxItems);
            checkList(expectedSublist, sublistResponse.getPaging(), sublistResponse);
        }

        // -ve: check skip count.
        {
            getAuditApps(getPaging(-1, null), "", HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    private ListResponse<AuditApp> getAuditApps(final PublicApiClient.Paging paging, String errorMessage, int expectedStatus) throws Exception
    {
        final AuditApps auditAppsProxy = publicApiClient.auditApps();
        return auditAppsProxy.getAuditApps(createParams(paging), errorMessage, expectedStatus);
    }

    private ListResponse<AuditApp> getAuditApps(final PublicApiClient.Paging paging) throws Exception
    {
        return getAuditApps(paging, "Failed to get groups", HttpServletResponse.SC_OK);
    }

    protected Map<String, String> createParams(Paging paging)
    {
        Map<String, String> params = new HashMap<String, String>(2);
        if (paging != null)
        {
            if (paging.getSkipCount() != null)
            {
                params.put("skipCount", String.valueOf(paging.getSkipCount()));
            }
            if (paging.getMaxItems() != null)
            {
                params.put("maxItems", String.valueOf(paging.getMaxItems()));
            }
        }

        return params;
    }

}
