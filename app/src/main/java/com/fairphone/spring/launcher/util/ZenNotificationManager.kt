/*
 * Copyright (c) 2025. Fairphone B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fairphone.spring.launcher.util

import android.app.AutomaticZenRule
import android.content.ComponentName
import android.content.Context
import android.service.notification.Condition
import android.service.notification.ZenDeviceEffects
import android.service.notification.ZenPolicy
import androidx.core.net.toUri
import com.fairphone.spring.launcher.activity.LauncherSettingsActivity
import com.fairphone.spring.launcher.data.model.ContactType
import com.fairphone.spring.launcher.data.model.UiMode

class ZenNotificationManager(private val context: Context) {

    val ZEN_RULE_CONDITION_ID = "com.fairphone.moments".toUri()

    /**
     * Enables Do Not Disturb mode for the given rule.
     */
    fun enableDnd(zenRuleId: String, name: String) {
        setAutomaticZenRuleState(zenRuleId, name, Condition.STATE_TRUE)
    }

    /**
     * Disables Do Not Disturb mode for the given rule.
     */
    fun disableDnd(zenRuleId: String, name: String) {
        setAutomaticZenRuleState(zenRuleId, name, Condition.STATE_FALSE)
    }

    /**
     * Disables Do Not Disturb mode for all automatic zen rules.
     */
    fun disableAllDnd() {
        context.notificationManager().automaticZenRules.forEach { ruleId, rule ->
            disableDnd(ruleId, rule.name)
        }
    }

    /**
     * Sets the state of the given automatic zen rule.
     */
    private fun setAutomaticZenRuleState(
        zenRuleId: String,
        name: String,
        state: Int
    ) {
        context.notificationManager()
            .setAutomaticZenRuleState(
                zenRuleId,
                Condition(
                    ZEN_RULE_CONDITION_ID,
                    name,
                    state,
                    Condition.SOURCE_USER_ACTION
                )
            )
    }

    /**
     * Creates a new automatic zen rule using the given parameters and adds it to the notification manager.
     */
    fun createAutomaticZenRule(
        name: String,
        allowedContacts: ContactType,
        uiMode: UiMode,
    ): String {
        val zenRule = createZenRule(
            name = name,
            allowedContacts = allowedContacts,
            uiMode = uiMode,
        )
        return context.notificationManager().addAutomaticZenRule(zenRule)
    }

    /**
     * Updates an existing automatic zen rule using the given parameters.
     */
    fun updateAutomaticZenRule(
        zenRuleId: String,
        name: String,
        allowedContacts: ContactType,
        uiMode: UiMode,
    ): Result<AutomaticZenRule> {
        // Disable DND first
        disableDnd(zenRuleId, name)

        // Update rule
        val updatedZenRule = createZenRule(
            name = name,
            allowedContacts = allowedContacts,
            uiMode = uiMode,
        )
        val result = context.notificationManager().updateAutomaticZenRule(zenRuleId, updatedZenRule)

        // Enable DND again
        enableDnd(zenRuleId, name)

        return if (result) {
            Result.success(context.notificationManager().getAutomaticZenRule(zenRuleId))
        } else {
            Result.failure(Exception("Failed to update automatic zen rule"))
        }
    }

    /**
     * Creates an automatic zen rule using the given parameters.
     */
    private fun createZenRule(
        name: String,
        allowedContacts: ContactType,
        uiMode: UiMode,
    ): AutomaticZenRule {
        val configActivity = getConfigurationActivity(context)
        val zenPolicy = createZenPolicy(allowedContacts)
        val zenDeviceEffects = createZenDeviceEffects(uiMode)

        return AutomaticZenRule.Builder(name, ZEN_RULE_CONDITION_ID)
            .setConfigurationActivity(configActivity)
            .setOwner(configActivity)
            .setDeviceEffects(zenDeviceEffects)
            .setZenPolicy(zenPolicy)
            .build()
    }

    private fun getConfigurationActivity(context: Context): ComponentName {
        return ComponentName(
            context.packageName,
            LauncherSettingsActivity::class.java.packageName + ".${LauncherSettingsActivity::class.java.simpleName}"
        )
    }

    private fun createZenPolicy(
        allowedContacts: ContactType,
    ): ZenPolicy {
        val peopleType = when (allowedContacts) {
            ContactType.CONTACT_TYPE_EVERYONE -> ZenPolicy.PEOPLE_TYPE_ANYONE
            ContactType.CONTACT_TYPE_NONE -> ZenPolicy.PEOPLE_TYPE_NONE
            ContactType.CONTACT_TYPE_ALL_CONTACTS -> ZenPolicy.PEOPLE_TYPE_CONTACTS
            ContactType.CONTACT_TYPE_STARRED -> ZenPolicy.PEOPLE_TYPE_STARRED
            ContactType.CONTACT_TYPE_CUSTOM -> ZenPolicy.PEOPLE_TYPE_UNSET
            ContactType.UNRECOGNIZED -> ZenPolicy.PEOPLE_TYPE_NONE
        }

        val builder = ZenPolicy.Builder()
            .allowCalls(peopleType)
            .allowMessages(peopleType)
            .allowConversations(peopleType)
            .allowMedia(true)
        return builder.build()
    }

    private fun createZenDeviceEffects(uiMode: UiMode): ZenDeviceEffects {
        return ZenDeviceEffects.Builder()
            .setShouldUseNightMode(uiMode == UiMode.UI_MODE_DARK)
            .build()
    }

}