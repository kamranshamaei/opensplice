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
package org.opensplice.dds.topic;

import java.util.List;

import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.policy.DataRepresentation;
import org.omg.dds.core.policy.Deadline;
import org.omg.dds.core.policy.DestinationOrder;
import org.omg.dds.core.policy.Durability;
import org.omg.dds.core.policy.DurabilityService;
import org.omg.dds.core.policy.GroupData;
import org.omg.dds.core.policy.LatencyBudget;
import org.omg.dds.core.policy.Lifespan;
import org.omg.dds.core.policy.Liveliness;
import org.omg.dds.core.policy.Ownership;
import org.omg.dds.core.policy.OwnershipStrength;
import org.omg.dds.core.policy.Partition;
import org.omg.dds.core.policy.Presentation;
import org.omg.dds.core.policy.Reliability;
import org.omg.dds.core.policy.TopicData;
import org.omg.dds.core.policy.TypeConsistencyEnforcement;
import org.omg.dds.core.policy.UserData;
import org.omg.dds.core.policy.TypeConsistencyEnforcement.Kind;
import org.omg.dds.topic.BuiltinTopicKey;
import org.omg.dds.topic.PublicationBuiltinTopicData;
import org.omg.dds.type.typeobject.TypeObject;
import org.opensplice.dds.core.IllegalArgumentExceptionImpl;
import org.opensplice.dds.core.OsplServiceEnvironment;
import org.opensplice.dds.core.UnsupportedOperationExceptionImpl;
import org.opensplice.dds.core.policy.DataRepresentationImpl;
import org.opensplice.dds.core.policy.PolicyConverter;
import org.opensplice.dds.core.policy.TypeConsistencyEnforcementImpl;

public class PublicationBuiltinTopicDataImpl implements
        PublicationBuiltinTopicData {
    private static final long serialVersionUID = -6604763092552114237L;
    private final transient OsplServiceEnvironment environment;
    private DDS.PublicationBuiltinTopicData old;
    private BuiltinTopicKey key;
    private BuiltinTopicKey participantKey;
    private final TypeConsistencyEnforcement typeConsistency;
    private final DataRepresentation dataRepresentation;

    public PublicationBuiltinTopicDataImpl(OsplServiceEnvironment environment,
            DDS.PublicationBuiltinTopicData old) {
        this.environment = environment;
        this.old = old;
        this.key = new BuiltinTopicKeyImpl(this.environment, old.key);
        this.participantKey = new BuiltinTopicKeyImpl(this.environment,
                old.participant_key);
        this.dataRepresentation = new DataRepresentationImpl(this.environment,
                DataRepresentation.Id.XCDR_DATA_REPRESENTATION);
        this.typeConsistency = new TypeConsistencyEnforcementImpl(
                this.environment, Kind.EXACT_TYPE_TYPE_CONSISTENCY);

    }

    @Override
    public ServiceEnvironment getEnvironment() {
        return this.environment;
    }

    @Override
    public BuiltinTopicKey getKey() {
        return this.key;
    }

    @Override
    public BuiltinTopicKey getParticipantKey() {
        return this.participantKey;
    }

    @Override
    public String getTopicName() {
        return old.topic_name;
    }

    @Override
    public String getTypeName() {
        return old.type_name;
    }

    @Override
    public List<String> getEquivalentTypeName() {
        throw new UnsupportedOperationExceptionImpl(this.environment,
                "PublicationBuiltinTopicData.getEquivalentTypeName() not supported.");
    }

    @Override
    public List<String> getBaseTypeName() {
        throw new UnsupportedOperationExceptionImpl(this.environment,
                "PublicationBuiltinTopicData.getBaseTypeName() not supported.");
    }

    @Override
    public TypeObject getType() {
        throw new UnsupportedOperationExceptionImpl(this.environment,
                "PublicationBuiltinTopicData.getType() not supported.");
    }

    @Override
    public Durability getDurability() {
        return PolicyConverter.convert(this.environment, old.durability);
    }

    @Override
    public DurabilityService getDurabilityService() {
        // TODO: Fix Java 5 PSM specification; DurabilityServiceQos is NOT part
        // of DataWriterQos
        throw new UnsupportedOperationExceptionImpl(
                this.environment,
                "Java 5 PSM specification issue; DurabilityServiceQos is only part of the TopicQos not of DataWriterQos.");
    }

    @Override
    public Deadline getDeadline() {
        return PolicyConverter.convert(this.environment, old.deadline);
    }

    @Override
    public LatencyBudget getLatencyBudget() {
        return PolicyConverter.convert(this.environment, old.latency_budget);
    }

    @Override
    public Liveliness getLiveliness() {
        return PolicyConverter.convert(this.environment, old.liveliness);
    }

    @Override
    public Reliability getReliability() {
        return PolicyConverter.convert(this.environment, old.reliability);
    }

    @Override
    public Lifespan getLifespan() {
        return PolicyConverter.convert(this.environment, old.lifespan);
    }

    @Override
    public UserData getUserData() {
        return PolicyConverter.convert(this.environment, old.user_data);
    }

    @Override
    public Ownership getOwnership() {
        return PolicyConverter.convert(this.environment, old.ownership);
    }

    @Override
    public OwnershipStrength getOwnershipStrength() {
        return PolicyConverter
                .convert(this.environment, old.ownership_strength);
    }

    @Override
    public DestinationOrder getDestinationOrder() {
        return PolicyConverter.convert(this.environment, old.destination_order);
    }

    @Override
    public Presentation getPresentation() {
        return PolicyConverter.convert(this.environment, old.presentation);
    }

    @Override
    public Partition getPartition() {
        return PolicyConverter.convert(this.environment, old.partition);
    }

    @Override
    public TopicData getTopicData() {
        return PolicyConverter.convert(this.environment, old.topic_data);
    }

    @Override
    public GroupData getGroupData() {
        return PolicyConverter.convert(this.environment, old.group_data);
    }

    @Override
    public DataRepresentation getRepresentation() {
        return this.dataRepresentation;
    }

    @Override
    public TypeConsistencyEnforcement getTypeConsistency() {
        return this.typeConsistency;
    }

    @Override
    public void copyFrom(PublicationBuiltinTopicData src) {
        if (src == null) {
            throw new IllegalArgumentExceptionImpl(this.environment,
                    "Invalid PublicationBuiltinTopicData (null) provided.");
        }
        try {
            PublicationBuiltinTopicDataImpl impl = (PublicationBuiltinTopicDataImpl) src;
            this.old = impl.old;
            this.key = new BuiltinTopicKeyImpl(this.environment, this.old.key);
            this.participantKey = new BuiltinTopicKeyImpl(this.environment,
                    this.old.participant_key);
        } catch (ClassCastException e) {
            throw new IllegalArgumentExceptionImpl(
                    this.environment,
                    "PublicationBuiltinTopicData.copyFrom() on non-OpenSplice PublicationBuiltinTopicData implementation is not supported.");
        }
    }

    @Override
    public PublicationBuiltinTopicData clone() {
        return new PublicationBuiltinTopicDataImpl(this.environment, this.old);
    }

}
