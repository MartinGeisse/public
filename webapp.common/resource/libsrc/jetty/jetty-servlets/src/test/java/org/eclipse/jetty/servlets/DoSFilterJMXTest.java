//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.servlets;

import java.lang.management.ManagementFactory;
import java.util.EnumSet;
import java.util.Set;
import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.DispatcherType;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Assert;
import org.junit.Test;

public class DoSFilterJMXTest
{
    @Test
    public void testDoSFilterJMX() throws Exception
    {
        Server server = new Server();
        Connector connector = new SelectChannelConnector();
        connector.setPort(0);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        DoSFilter filter = new DoSFilter();
        FilterHolder holder = new FilterHolder(filter);
        String name = "dos";
        holder.setName(name);
        holder.setInitParameter(DoSFilter.MANAGED_ATTR_INIT_PARAM, "true");
        context.addFilter(holder, "/*", EnumSet.of(DispatcherType.REQUEST));
        context.setInitParameter(ServletContextHandler.MANAGED_ATTRIBUTES, name);

        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        MBeanContainer mbeanContainer = new MBeanContainer(mbeanServer);
        server.addBean(mbeanContainer);
        server.getContainer().addEventListener(mbeanContainer);

        server.start();

        String domain = DoSFilter.class.getPackage().getName();
        Set<ObjectName> mbeanNames = mbeanServer.queryNames(ObjectName.getInstance(domain + ":*"), null);
        Assert.assertEquals(1, mbeanNames.size());
        ObjectName objectName = mbeanNames.iterator().next();

        boolean value = (Boolean)mbeanServer.getAttribute(objectName, "enabled");
        mbeanServer.setAttribute(objectName, new Attribute("enabled", !value));
        Assert.assertEquals(!value, filter.isEnabled());

        String whitelist = (String)mbeanServer.getAttribute(objectName, "whitelist");
        String address = "127.0.0.1";
        Assert.assertFalse(whitelist.contains(address));
        boolean result = (Boolean)mbeanServer.invoke(objectName, "addWhitelistAddress", new Object[]{address}, new String[]{String.class.getName()});
        Assert.assertTrue(result);
        whitelist = (String)mbeanServer.getAttribute(objectName, "whitelist");
        Assert.assertTrue(whitelist.contains(address));

        result = (Boolean)mbeanServer.invoke(objectName, "removeWhitelistAddress", new Object[]{address}, new String[]{String.class.getName()});
        Assert.assertTrue(result);
        whitelist = (String)mbeanServer.getAttribute(objectName, "whitelist");
        Assert.assertFalse(whitelist.contains(address));

        server.stop();
    }
}
