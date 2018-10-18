/*
 *                         Vortex OpenSplice
 *
 *   This software and documentation are Copyright 2006 to TO_YEAR ADLINK
 *   Technology Limited, its affiliated companies and licensors. All rights
 *   reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.opensplice.dds.dcps;

public class ListenerBase extends ObjectImpl {

    @Override
    protected int deinit() {
        return super.deinit();
    }

    public static class ListenerBaseImpl extends DDS._ListenerLocalBase {

        private static final long serialVersionUID = 819730460005253563L;
    }

    private DDS._ListenerLocalBase base;

    public ListenerBase() {
        base = new ListenerBaseImpl();
    }

    @Override
    public String[] _ids() {
        return base._ids();
    }

}
