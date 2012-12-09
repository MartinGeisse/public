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
package org.apache.sshd.agent.unix;

import java.io.IOException;

import org.apache.sshd.client.channel.AbstractClientChannel;
import org.apache.sshd.client.future.OpenFuture;
import org.apache.sshd.common.SshConstants;
import org.apache.sshd.common.channel.ChannelOutputStream;
import org.apache.tomcat.jni.Socket;
import org.apache.tomcat.jni.Status;

public class AgentForwardedChannel extends AbstractClientChannel implements Runnable {

    private final long socket;

    public AgentForwardedChannel(long socket) {
        super("auth-agent@openssh.com");
        this.socket = socket;
    }

    public void run() {
        try {
            byte[] buf = new byte[1024];
            while (true) {
                int result = Socket.recv(socket, buf, 0, buf.length);
                if (result == Status.APR_EOF) {
                    break;
                } else if (result < Status.APR_SUCCESS) {
                    AgentServerProxy.throwException(result);
                }
                getOut().write(buf, 0, result);
                getOut().flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(false);
        }
    }

    public synchronized OpenFuture open() throws Exception {
        return internalOpen();
    }

    @Override
    protected synchronized void doOpen() throws Exception {
        out = new ChannelOutputStream(this, remoteWindow, log, SshConstants.Message.SSH_MSG_CHANNEL_DATA);
    }

    @Override
    protected synchronized void doClose() {
        Socket.close(socket);
        super.doClose();
    }

    protected synchronized void doWriteData(byte[] data, int off, int len) throws IOException {
        localWindow.consumeAndCheck(len);
        int result = Socket.send(socket, data, off, len);
        if (result < Status.APR_SUCCESS) {
            AgentServerProxy.throwException(result);
        }

    }

}
