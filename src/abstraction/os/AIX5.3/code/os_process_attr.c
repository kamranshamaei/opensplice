/*
 *                         Vortex OpenSplice
 *
 *   This software and documentation are Copyright 2006 to TO_YEAR ADLINK
 *   Technology Limited, its affiliated companies and licensors. All rights
 *   reserved.
 *
 *   Licensed under the ADLINK Software License Agreement Rev 2.7 2nd October
 *   2014 (the "License"); you may not use this file except in compliance with
 *   the License.
 *   You may obtain a copy of the License at:
 *                      $OSPL_HOME/LICENSE
 *
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

/** \file os/AIX5.3/code/os_process_attr.c
 *  \brief POSIX process attributes
 *
 * Implements os_procAttrInit and sets attributes
 * to AIX specific values:
 * - scheduling class is OS_SCHED_DEFAULT
 * - process priority is 60
 * - locking policy is OS_UNLOCKED
 * - user credentials is default
 */

#include <assert.h>
#include <sched.h>

/** \brief Initialize process attributes
 *
 * Set \b procAttr->schedClass to \b OS_SCHED_DEFAULT
 * (take the platforms default scheduling class, Time-sharing for
 * non realtime platforms, Real-time for realtime platforms)
 * Set \b procAttr->schedPriority to \b 60
 * Set \b procAttr->lockPolicy to \b OS_LOCK_DEFAULT
 * (no locking on non realtime platforms, locking on
 * realtime platforms)
 * Set \b procAttr->userCred.uid to 0
 * (don't change the uid of the process)
 * Set \b procAttr->userCred.gid to 0
 * (don't change the gid of the process)
 */
void
os_procAttrInit (
    os_procAttr *procAttr)
{
    assert (procAttr != NULL);
    procAttr->schedClass = OS_SCHED_DEFAULT;
    procAttr->schedPriority = 60;
    procAttr->lockPolicy = OS_LOCK_DEFAULT;
    procAttr->userCred.uid = 0;
    procAttr->userCred.gid = 0;
    procAttr->activeRedirect = 0;
}
