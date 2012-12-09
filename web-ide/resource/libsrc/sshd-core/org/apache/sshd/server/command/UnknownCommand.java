/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sshd.server.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

/**
 * Implementation of an unknown command that can be returned by <code>CommandFactory</code>
 * when the command is not known, as it is supposed to always
 * return a valid <code>Command</code> object.
 *
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public class UnknownCommand implements Command {

    public UnknownCommand(String command) {
    }

    public void setInputStream(InputStream in) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setOutputStream(OutputStream out) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setErrorStream(OutputStream err) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setExitCallback(ExitCallback callback) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void start(Environment env) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
        // TODO: send back an error ?
    }

    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
