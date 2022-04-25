package org.wikipedia.analytics.eventplatform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.wikipedia.auth.AccountUtil
import org.wikipedia.settings.Prefs

class ArticleLinkPreviewInteractionEvent(private var wikiDb: String,
                                         private var pageId: Int,
                                         private var source: Int) : TimedEvent() {

    fun logLinkClick() {
        submitEvent("linkclick")
    }

    fun logNavigate() {
        submitEvent(if (Prefs.isLinkPreviewEnabled) "navigate" else "disabled")
    }

    fun logCancel() {
        submitEvent("cancel")
    }

    private fun submitEvent(action: String) {
        EventPlatformClient.submit(ArticleLinkPreviewInteractionEventImpl(action, source, !AccountUtil.isLoggedIn, duration, wikiDb, pageId, PROD_LINK_PREVIEW_VERSION))
    }

    @Suppress("unused")
    @Serializable
    @SerialName("/analytics/mobile_apps/android_article_link_preview_interaction/1.0.0")
    class ArticleLinkPreviewInteractionEventImpl(private var action: String,
                                                 private var source: Int,
                                                 @SerialName("is_anon") private val isAnon: Boolean,
                                                 @SerialName("time_spent_ms") private var timeSpentMs: Int,
                                                 @SerialName("wiki_db") private var wikiDb: String,
                                                 @SerialName("page_id") private var pageId: Int,
                                                 private var version: Int) :
        MobileAppsEvent("android.article_link_preview_interaction")

    companion object {
        private const val PROD_LINK_PREVIEW_VERSION = 3
    }
}
