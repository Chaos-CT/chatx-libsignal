//
// Copyright 2024 Signal Messenger, LLC.
// SPDX-License-Identifier: AGPL-3.0-only
//

package org.signal.libsignal.zkgroup.groupsend;

import static org.signal.libsignal.internal.FilterExceptions.filterExceptions;

import java.time.Instant;
import java.util.Collection;
import org.signal.libsignal.internal.Native;
import org.signal.libsignal.protocol.ServiceId;
import org.signal.libsignal.zkgroup.InvalidInputException;
import org.signal.libsignal.zkgroup.VerificationFailedException;
import org.signal.libsignal.zkgroup.internal.ByteArray;

/**
 * A token representing a particular {@link GroupSendEndorsement}, along with the endorsement's
 * expiration.
 *
 * <p>Generated by {@link GroupSendEndorsement.Token#toFullToken}, and verified by the chat server.
 */
public final class GroupSendFullToken extends ByteArray {
  public GroupSendFullToken(byte[] contents) throws InvalidInputException {
    super(contents);
    filterExceptions(
        InvalidInputException.class, () -> Native.GroupSendFullToken_CheckValidContents(contents));
  }

  /** Gets the expiration embedded in the token. */
  public Instant getExpiration() {
    return Instant.ofEpochSecond(
        Native.GroupSendFullToken_GetExpiration(getInternalContentsForJNI()));
  }

  /**
   * Verifies that this token was generated from an endorsement of {@code userIds} by {@code
   * keyPair}.
   *
   * <p>The correct {@code keyPair} must be selected based on {@link #getExpiration}.
   *
   * @throws VerificationFailedException if the token is invalid.
   */
  public void verify(Collection<ServiceId> userIds, GroupSendDerivedKeyPair keyPair)
      throws VerificationFailedException {
    verify(userIds, Instant.now(), keyPair);
  }

  /**
   * Verifies that this token was generated from an endorsement of {@code userIds} by {@code
   * keyPair}, assuming a specific current time.
   *
   * <p>This should only be used for testing purposes.
   *
   * @see #verify(Collection, GroupSendDerivedKeyPair)
   */
  public void verify(Collection<ServiceId> userIds, Instant now, GroupSendDerivedKeyPair keyPair)
      throws VerificationFailedException {
    filterExceptions(
        VerificationFailedException.class,
        () ->
            Native.GroupSendFullToken_Verify(
                getInternalContentsForJNI(),
                ServiceId.toConcatenatedFixedWidthBinary(userIds),
                now.getEpochSecond(),
                keyPair.getInternalContentsForJNI()));
  }
}
