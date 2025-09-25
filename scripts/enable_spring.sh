#!/bin/sh

#
# Copyright (C) 2025 FairPhone B.V.
#
# SPDX-FileCopyrightText: 2025. FairPhone B.V.
#
# SPDX-License-Identifier: EUPL-1.2
#

#
# Emulate enabling the Spring Launcher.
#

############################################################
# Help                                                     #
############################################################

show_overlay=false

Help()
{
   # Display Help
   echo "mulate enabling the Spring Launcher."
   echo
   echo "Syntax: enable_spring [-o|-h]"
   echo "options:"
   echo "o [true|false]    Show switch overlay animation."
   echo "h                 Print this Help."
   echo
}

Enable_Spring()
{
  adb shell am start \
      -a android.intent.action.MAIN \
      -n com.fairphone.spring.launcher/com.fairphone.spring.launcher.activity.SwitchStateChangeActivity \
      --es com.fairphone.spring.launcher.extra.switch_button_state "ENABLED" \
      --ez com.fairphone.spring.launcher.extra.show_overlay $show_overlay
}

############################################################
############################################################
# Main program                                             #
############################################################
############################################################
############################################################
# Process the input options. Add options as needed.        #
############################################################
# Get the options
while getopts ":ho:" option; do
   case $option in
      o) # display overlay
         show_overlay=$OPTARG;;
      h) # display Help
        Help
        exit;;
      \?) # Invalid option
        echo "Error: Invalid option"
        exit;;
   esac
done

Enable_Spring