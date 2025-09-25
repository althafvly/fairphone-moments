#!/bin/sh

#
# Copyright (C) 2025 FairPhone B.V.
#
# SPDX-FileCopyrightText: 2025. FairPhone B.V.
#
# SPDX-License-Identifier: EUPL-1.2
#
#

#
# Emulate enabling the Spring Launcher.
#
adb shell am start \
    -a android.intent.action.MAIN \
    -n com.fairphone.spring.launcher/com.fairphone.spring.launcher.activity.SwitchStateChangeActivity \
    --es com.fairphone.spring.launcher.extra.switch_button_state "DISABLED"
