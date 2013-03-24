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

package org.eclipse.jetty.osgi.boot;

import org.eclipse.jetty.deploy.App;
import org.eclipse.jetty.deploy.bindings.StandardUndeployer;
import org.eclipse.jetty.deploy.graph.Node;
import org.eclipse.jetty.osgi.boot.utils.EventSender;




/**
 * OSGiUndeployer
 *
 *
 */
public class OSGiUndeployer extends StandardUndeployer
{
    /* ------------------------------------------------------------ */
    public void processBinding(Node node, App app) throws Exception
    {
        EventSender.getInstance().send(EventSender.UNDEPLOYING_EVENT, ((AbstractOSGiApp)app).getBundle(), app.getContextPath());
        super.processBinding(node,app);
        EventSender.getInstance().send(EventSender.UNDEPLOYED_EVENT, ((AbstractOSGiApp)app).getBundle(), app.getContextPath());
        ((AbstractOSGiApp)app).deregisterAsOSGiService();
    }
}
