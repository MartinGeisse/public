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
package org.apache.sshd.server;

import org.apache.sshd.server.session.ServerSession;

/**
 * The <code>PasswordAuthenticator</code> is used to authenticate
 * users based on a password.
 *
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public interface PasswordAuthenticator {

    /**
     * Check the validity of a password.
     * This method should return null if the authentication fails.
     *
     * @param username the username
     * @param password the password
     * @param session the server session
     * @return a boolean indicating if authentication succeeded or not
     */
    boolean authenticate(String username, String password, ServerSession session);

}
