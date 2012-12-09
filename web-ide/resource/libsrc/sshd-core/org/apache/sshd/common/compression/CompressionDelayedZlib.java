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
package org.apache.sshd.common.compression;

import org.apache.sshd.common.Compression;
import org.apache.sshd.common.NamedFactory;

/**
 * ZLib delayed compression.
 *
 * @see Compression#isDelayed()
 *
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public class CompressionDelayedZlib extends CompressionZlib {

    /**
     * Named factory for the ZLib Delayed Compression.
     */
    public static class Factory implements NamedFactory<Compression> {
        public String getName() {
            return "zlib@openssh.com";
        }

        public Compression create() {
            return new CompressionDelayedZlib();
        }
    }

    /**
     * Create a new instance of a delayed ZLib compression
     */
    public CompressionDelayedZlib() {
    }

    public boolean isDelayed() {
        return true;
    }

}
