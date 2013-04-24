/*

   Derby - Class org.apache.derby.client.net.NetXACallInfo

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/
/**********************************************************************
 *
 *
 *  Component Name =
 *
 *  Package Name = org.apache.derby.client.net
 *
 *  Descriptive Name = XACallInfo class
 *
 *  Function = Handle XA information
 *
 *  List of Classes
 *              - NetXACallInfo
 *
 *  Restrictions : None
 *
 **********************************************************************/
package org.apache.derby.client.net;

import java.io.InputStream;
import java.io.OutputStream;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.apache.derby.client.am.ClientConnection;

public class NetXACallInfo {
    Xid xid_;                         // current xid
    int xaFlags_;                     // current xaFlags
    /** XA transaction timeout in milliseconds. The value less than 0 means
      * that the time out is not specified. The value 0 means infinite timeout. */
    long xaTimeoutMillis_;
    // may not be needed!!!~~~
    int xaFunction_;                  // queued XA function being performed
    int xaRetVal_;                    // xaretval from server
    boolean xaInProgress_;            // set at start(), reset at commit(),
    //  rollback(), or prepare() on RDONLY
    boolean xaWasSuspended;           // used to indicate an XA tyrans was suspended
    //  one or more times, overrides empty transaction
    boolean currConnection_;          // set when actualConn_ is the current connection
    boolean freeEntry_;               // set when no actualConn_, entry is free / available
    boolean convReleased_;            // release coversation, reuse successfull = true
    NetXAResource xaResource_;         // NetXAResource containing this NetXACallInfo
    NetXAConnection actualConn_; // the actual connection object, not necessarily
    // the user's connection object
    /* only the first connection object is actually used. The other connection
     * objects are used only for their TCP/IP variables to simulate
     * suspend / resume
     */

    private byte[] crrtkn_;
    private InputStream in_;
    private OutputStream out_;

    private byte[] uowid_;  // Unit of Work ID

    private boolean readOnlyTransaction_;  // readOnlyTransaction Flag

    public NetXACallInfo() {
        xid_ = null;
        xaFlags_ = XAResource.TMNOFLAGS;
        xaTimeoutMillis_ = -1;
        xaInProgress_ = false;
        currConnection_ = false;
        freeEntry_ = true;
        convReleased_ = false;
        actualConn_ = null;
        readOnlyTransaction_ = true;
        xaResource_ = null;
        xaRetVal_ = 0;
        xaWasSuspended = false;
    }

    public NetXACallInfo(Xid xid, int flags, NetXAResource xares, NetXAConnection actualConn) {
        xid_ = xid;
        xaFlags_ = flags;
        xaTimeoutMillis_ = -1;
        xaInProgress_ = false;
        currConnection_ = false;
        freeEntry_ = true;
        actualConn_ = actualConn;
        readOnlyTransaction_ = true;
        xaResource_ = xares;
        xaRetVal_ = 0;
        xaWasSuspended = false;
    }

    public void saveConnectionVariables() {
        in_ = actualConn_.getNetConnection().getInputStream();
        out_ = actualConn_.getNetConnection().getOutputStream();
        crrtkn_ = actualConn_.getCorrelatorToken();
    }

    public InputStream getInputStream() {
        return in_;
    }

    public OutputStream getOutputStream() {
        return out_;
    }

    protected void setUOWID(byte[] uowid) {
        uowid_ = uowid;
    }

    protected byte[] getUOWID() {
        return uowid_;
    }

    protected void setReadOnlyTransactionFlag(boolean flag) {
        readOnlyTransaction_ = flag;
    }

    protected boolean getReadOnlyTransactionFlag() {
        return readOnlyTransaction_;
    }


}









