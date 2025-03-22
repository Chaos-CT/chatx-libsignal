//
// Copyright 2024 Signal Messenger, LLC.
// SPDX-License-Identifier: AGPL-3.0-only
//

package org.signal.libsignal.zkgroup.backups;

public enum BackupLevel {
  // This must match the Rust version of the enum.
  FREE(200),
  PAID(201);

  private final int value;

  BackupLevel(int value) {
    this.value = value;
  }

  int getValue() {
    return this.value;
  }

  public static BackupLevel fromValue(int value) {
    // A linear scan is simpler than a hash lookup for a set of values this small.
    for (final var backupLevel : BackupLevel.values()) {
      if (backupLevel.getValue() == value) {
        return backupLevel;
      }
    }
    throw new IllegalArgumentException("Invalid backup level: " + value);
  }
}
