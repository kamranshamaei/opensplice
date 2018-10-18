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
/**@file api/cm/xml/code/cmx__networkReader.h
 *
 * Offers internal routines on a dataReader.
 */
#ifndef CMX__NETWORKREADER_H
#define CMX__NETWORKREADER_H

#include "c_typebase.h"
#include "v_kernel.h"

#if defined (__cplusplus)
extern "C" {
#endif

/**
 * Initializes the networkReader specific part of the XML representation of the
 * supplied kernel networkReader. This function should only be used by the
 * cmx_entityNewFromWalk function.
 *
 * @param entity The entity to create a XML representation of.
 * @return The networkReader specific part of the XML representation of the entity.
 */
c_char* cmx_networkReaderInit   (v_networkReader entity);

#if defined (__cplusplus)
}
#endif

#endif /* CMX__NETWORKREADER_H */
