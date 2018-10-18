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
 */
package org.vortex.FACE;

public class TS {
    private TransportServices impl;

    protected TS() {
    }

    private static class LazyHolder {
        private static final TS INSTANCE = new TS();
    }

    protected static TS getInstance() {
        return LazyHolder.INSTANCE;
    }

    public TransportServices getImpl() {
        return impl;
    }

    /**
     * The Initialize function call allows for the Portable Components Segment
     * (PCS) and Platform-Specific Services Segment (PSSS) component to trigger
     * the initialization of the Transport Services Segment (TSS) component.
     *
     * Possible return codes:
     * <ul>
     * <li>INVALID_PARAM - The provided configuration is not valid
     * <li>INVALID_CONFIG - The provided configuration contains invalid settings
     * <li>NO_ACTION - There is already an initialized configuration.
     * </ul>
     * @param configuration
     *        The configuration defined as an xml file which hold the connection
     *        configuration.
     *        This is an input parameter
     * @param return_code
     *        The return_code
     */
    public static void Initialize(String configuration,
            FACE.RETURN_CODE_TYPEHolder return_code) {
        if (return_code == null) {
            return;
        }
        if (TS.getInstance().impl != null) {
            return_code.value = FACE.RETURN_CODE_TYPE.NO_ACTION;
            return;
        }
        Holder<TransportServices> tsHolder = new Holder<TransportServices>();
        org.vortex.FACE.TSFactory.getInstance().getTS(configuration, tsHolder,
                return_code);
        if (return_code.value == FACE.RETURN_CODE_TYPE.NO_ERROR) {
            TS.getInstance().impl = tsHolder.value;
        }
    }

    /**
     * The Transport Services Segment (TSS) provides an interface to create a
     * connection.
     * The parameters for the individual connections are determined through the
     * TSS configuration capability which is set by the Initialize function call.
     *
     * Possible return codes:
     * <ul>
     * <li>NO_ERROR - Successful completion.</li>
     * <li>NO_ACTION - Returned if the participant has already been created and
     * registered. - Type has already been registered.</li>
     * <li>INVALID_CONFIG - Could not allocate enough memory. - Policies are not
     * consistent with each other (e.g., configuration data is invalid, QoS
     * attributes not supported).
     * <ul>
     * <li>Generic, unspecified error.</li>
     * <li>Attempted to modify an immutable QoSPolicy.</li>
     * </ul></li>
     * <li>INVALID_PARAM - Returned under the following conditions: - Could not
     * find topic name associated with the connection.</li>
     * <li>NOT_AVAILABLE - Unsupported operation.</li>
     * <li>INVALID_MODE - Connection could not be created in the current mode or
     * operation performed at an inappropriate type.</li>
     * </ul>
     *
     * @param connection_name
     *        The connection_name which needs to match one of the configured
     *        connection names in the configuration.
     *        This is an input parameter.
     * @param pattern
     *        The pattern set in the connection configuration which for DDS only
     *        can be PUB_SUB.
     *        This is an input parameter.
     * @param connection_id
     *        The connection_id which is generated by DDS and set on successful
     *        creation.
     *        This is an output parameter.
     * @param connection_direction
     *        The connection_direction of the connection that is created. This
     *        can be SOURCE or DESTINATION.
     *        This is an output parameter.
     * @param max_message_size
     *        The max_message_size for DDS this parameter is not relevant.
     *        This is an output parameter.
     * @param timeout
     *        The timeout for DDS this parameter is not relevant.
     *        This is an input parameter.
     * @param return_code
     *        The return_code
     *        This is an output parameter.
     */
    public static void Create_Connection(String connection_name,
            FACE.MESSAGING_PATTERN_TYPE pattern,
            org.omg.CORBA.LongHolder connection_id,
            FACE.CONNECTION_DIRECTION_TYPEHolder connection_direction,
            org.omg.CORBA.IntHolder max_message_size, long timeout,
            FACE.RETURN_CODE_TYPEHolder return_code) {
        if (TS.getInstance().impl == null) {
            return_code.value = FACE.RETURN_CODE_TYPE.NOT_AVAILABLE;
            return;
        }
        TS.getInstance().impl.Create_Connection(connection_name, pattern,
                connection_id,
                connection_direction, max_message_size, timeout, return_code);
    }


    /**
     * The Destroy_Connection function frees up any resources allocated to the
     * connection.
     *
     * Possible return codes:
     * <ul>
     * <li>NO_ERROR - Successful completion.</li>
     * <li>NO_ACTION - Object target of this operation has already been deleted.</li>
     * <li>INVALID_MODE - An operation was invoked on an inappropriate object or
     * at an inappropriate time.</li>
     * <li>INVALID_PARAM - Connection identification (ID) invalid.</li>
     * <li>NOT_AVAILABLE - Unsupported operation.</li>
     * <li>INVALID_CONFIG - Generic, unspecified error.</li>
     * <li>INVALID_MODE - A pre-condition for the operation was not met. Note:
     * In a FACE implementation, this error may imply an implementation problem
     * since the connection is deleted and should clean up all entities/children
     * associated with the connection.</li>
     * </ul>
     *
     * @param connection_id
     *        The connection_id of the connection that needs to be destroyed.
     *        This is an input parameter.
     * @param return_code
     *        The return_code.
     *        This is an output parameter.
     */
    public static void Destroy_Connection(long connection_id,
            FACE.RETURN_CODE_TYPEHolder return_code) {
        if (TS.getInstance().impl == null) {
            return_code.value = FACE.RETURN_CODE_TYPE.NOT_AVAILABLE;
            return;
        }
        TS.getInstance().impl.Destroy_Connection(connection_id, return_code);
    }

    /**
     * The purpose of Unregister_Callback is to provide a mechanism to unregister
     * the callback associated with a connection_id.
     *
     * @param connection_id
     *        The connection_id of the connection where the callback was
     *        registered.
     *        This is an input parameter.
     * @param return_code
     *        The return_code
     *        This is an output parameter.
     */
    public static void Unregister_Callback(long connection_id,
            FACE.RETURN_CODE_TYPEHolder return_code) {
        if (TS.getInstance().impl == null) {
            return_code.value = FACE.RETURN_CODE_TYPE.NOT_AVAILABLE;
            return;
        }
        TS.getInstance().impl
                .Unregister_Callback(connection_id, return_code);
    }

    /**
     * The purpose of Get_Connection_Parameters is to get the information
     * regarding the requested connection.
     *
     * @param connection_name
     *        The connection_name which belongs to the given connection_id.
     *        This is an output parameter.
     * @param connection_id
     *        The connection_id for which this status needs to return
     *        information.
     *        This is an input parameter.
     * @param connection_status
     *        The connection_status which consists of the following settings:
     *        <ul>
     *        <li>MESSAGE - Always 0
     *        <li>MAX_MESSAGE - Always 0.
     *        <li>MAX_MESSAGE_SIZE - Always 0.
     *        <li>CONNECTION_DIRECTION - SOURCE or DESTINATION
     *        <li>WAITING_PROCESSES_OR_MESSAGES -  Not implemented
     *        <li>REFRESH_PERIOD - The configured refresh period.
     *        <li>LAST_MSG_VALIDITY - Whether or not the refresh period of last
     *        taken message has expired or not (DESTINATION)
     *        </ul>
     *        This is an output parameter.
     * @param return_code
     *        The return_code
     *        This is an output parameter.
     */
    public static void Get_Connection_Parameters(
            org.omg.CORBA.StringHolder connection_name,
            org.omg.CORBA.LongHolder connection_id,
            FACE.TRANSPORT_CONNECTION_STATUS_TYPEHolder connection_status,
            FACE.RETURN_CODE_TYPEHolder return_code) {
        if (TS.getInstance().impl == null) {
            return_code.value = FACE.RETURN_CODE_TYPE.NOT_AVAILABLE;
            return;
        }
        TS.getInstance().impl.Get_Connection_Parameters(connection_name,
                connection_id,
                connection_status, return_code);
    }
}
